/*
 ** 2011 January 5
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **/

/*
 * 2011 February 16
 *
 * This source code is based on the work of Scaevolus (see notice above).
 * It has been slightly modified by Mojang AB (constants instead of magic
 * numbers, a chunk timestamp header, and auto-formatted according to our
 * formatter template).
 *
 */

// Interfaces with region files on the disk

/*

 Region File Format

 Concept: The minimum unit of storage on hard drives is 4KB. 90% of Minecraft
 chunks are smaller than 4KB. 99% are smaller than 8KB. Write a simple
 container to store chunks in single files in runs of 4KB sectors.

 Each region file represents a 32x32 group of chunks. The conversion from
 chunk number to region number is floor(coord / 32): a chunk at (30, -3)
 would be in region (0, -1), and one at (70, -30) would be at (3, -1).
 Region files are named "r.x.z.data", where x and z are the region coordinates.

 A region file begins with a 4KB header that describes where chunks are stored
 in the file. A 4-byte big-endian integer represents sector offsets and sector
 counts. The chunk offset for a chunk (x, z) begins at byte 4*(x+z*32) in the
 file. The bottom byte of the chunk offset indicates the number of sectors the
 chunk takes up, and the top 3 bytes represent the sector number of the chunk.
 Given a chunk offset o, the chunk data begins at byte 4096*(o/256) and takes up
 at most 4096*(o%256) bytes. A chunk cannot exceed 1MB in size. If a chunk
 offset is 0, the corresponding chunk is not stored in the region file.

 Chunk data begins with a 4-byte big-endian integer representing the chunk data
 length in bytes, not counting the length field. The length must be smaller than
 4096 times the number of sectors. The next byte is a version field, to allow
 backwards-compatible updates to how chunks are encoded.

 A version of 1 represents a gzipped NBT file. The gzipped data is the chunk
 length - 1.

 A version of 2 represents a deflated (zlib compressed) NBT file. The deflated
 data is the chunk length - 1.

 */

package fr.veridiangames.server;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class MinecraftFile {

    private static final int VERSION_GZIP = 1;
    private static final int VERSION_DEFLATE = 2;

    private static final int SECTOR_BYTES = 4096;
    private static final int SECTOR_INTS = SECTOR_BYTES / 4;

    static final int CHUNK_HEADER_SIZE = 5;
    private static final byte emptySector[] = new byte[4096];

    private final File fileName;
    private RandomAccessFile file;
    private final int offsets[];
    private final int chunkTimestamps[];
    private ArrayList<Boolean> sectorFree;
    private int sizeDelta;
    private long lastModified = 0;

    public MinecraftFile(File path) {
        this.offsets = new int[SECTOR_INTS];
        this.chunkTimestamps = new int[SECTOR_INTS];

        this.fileName = path;
        this.debugln("REGION LOAD " + this.fileName);

        this.sizeDelta = 0;

        try {
            if (path.exists())
				this.lastModified = path.lastModified();

            this.file = new RandomAccessFile(path, "rw");

            if (this.file.length() < SECTOR_BYTES) {
                /* we need to write the chunk offset table */
                for (int i = 0; i < SECTOR_INTS; ++i)
					this.file.writeInt(0);
                // write another sector for the timestamp info
                for (int i = 0; i < SECTOR_INTS; ++i)
					this.file.writeInt(0);

                this.sizeDelta += SECTOR_BYTES * 2;
            }

            if ((this.file.length() & 0xfff) != 0)
				/* the file size is not a multiple of 4KB, grow it */
                for (int i = 0; i < (this.file.length() & 0xfff); ++i)
					this.file.write((byte) 0);

            /* set up the available sector map */
            int nSectors = (int) this.file.length() / SECTOR_BYTES;
            this.sectorFree = new ArrayList<Boolean>(nSectors);

            for (int i = 0; i < nSectors; ++i)
				this.sectorFree.add(true);

            this.sectorFree.set(0, false); // chunk offset table
            this.sectorFree.set(1, false); // for the last modified info

            this.file.seek(0);
            for (int i = 0; i < SECTOR_INTS; ++i) {
                int offset = this.file.readInt();
                this.offsets[i] = offset;
                if (offset != 0 && (offset >> 8) + (offset & 0xFF) <= this.sectorFree.size())
					for (int sectorNum = 0; sectorNum < (offset & 0xFF); ++sectorNum)
						this.sectorFree.set((offset >> 8) + sectorNum, false);
            }
            for (int i = 0; i < SECTOR_INTS; ++i) {
                int lastModValue = this.file.readInt();
                this.chunkTimestamps[i] = lastModValue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* the modification date of the region file when it was first opened */
    public long lastModified() {
        return this.lastModified;
    }

    /* gets how much the region file has grown since it was last checked */
    public synchronized int getSizeDelta() {
        int ret = this.sizeDelta;
        this.sizeDelta = 0;
        return ret;
    }

    // various small debug printing helpers
    private void debug(String in) {
//        System.out.print(in);
    }

    private void debugln(String in) {
        this.debug(in + "\n");
    }

    private void debug(String mode, int x, int z, String in) {
        this.debug("REGION " + mode + " " + this.fileName.getName() + "[" + x + "," + z + "] = " + in);
    }

    private void debug(String mode, int x, int z, int count, String in) {
        this.debug("REGION " + mode + " " + this.fileName.getName() + "[" + x + "," + z + "] " + count + "B = " + in);
    }

    private void debugln(String mode, int x, int z, String in) {
        this.debug(mode, x, z, in + "\n");
    }

    /*
     * gets an (uncompressed) stream representing the chunk data returns null if
     * the chunk is not found or an error occurs
     */
    public synchronized DataInputStream getChunkDataInputStream(int x, int z) {
        if (this.outOfBounds(x, z)) {
            this.debugln("READ", x, z, "out of bounds");
            return null;
        }

        try {
            int offset = this.getOffset(x, z);
            if (offset == 0)
				// debugln("READ", x, z, "miss");
                return null;

            int sectorNumber = offset >> 8;
            int numSectors = offset & 0xFF;

            if (sectorNumber + numSectors > this.sectorFree.size()) {
                this.debugln("READ", x, z, "invalid sector");
                return null;
            }

            this.file.seek(sectorNumber * SECTOR_BYTES);
            int length = this.file.readInt();

            if (length > SECTOR_BYTES * numSectors) {
                this.debugln("READ", x, z, "invalid length: " + length + " > 4096 * " + numSectors);
                return null;
            }

            byte version = this.file.readByte();
            if (version == VERSION_GZIP) {
                byte[] data = new byte[length - 1];
                this.file.read(data);
                DataInputStream ret = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));
                // debug("READ", x, z, " = found");
                return ret;
            } else if (version == VERSION_DEFLATE) {
                byte[] data = new byte[length - 1];
                this.file.read(data);
                DataInputStream ret = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(data)));
                // debug("READ", x, z, " = found");
                return ret;
            }

            this.debugln("READ", x, z, "unknown version " + version);
            return null;
        } catch (IOException e) {
            this.debugln("READ", x, z, "exception");
            return null;
        }
    }

    public DataOutputStream getChunkDataOutputStream(int x, int z) {
        if (this.outOfBounds(x, z)) return null;

        return new DataOutputStream(new DeflaterOutputStream(new ChunkBuffer(x, z)));
    }

    /*
     * lets chunk writing be multithreaded by not locking the whole file as a
     * chunk is serializing -- only writes when serialization is over
     */
    class ChunkBuffer extends ByteArrayOutputStream {
        private int x, z;

        public ChunkBuffer(int x, int z) {
            super(8096); // initialize to 8KB
            this.x = x;
            this.z = z;
        }

        @Override
		public void close() {
            MinecraftFile.this.write(this.x, this.z, this.buf, this.count);
        }
    }

    /* write a chunk at (x,z) with length bytes of data to disk */
    protected synchronized void write(int x, int z, byte[] data, int length) {
        try {
            int offset = this.getOffset(x, z);
            int sectorNumber = offset >> 8;
            int sectorsAllocated = offset & 0xFF;
            int sectorsNeeded = (length + CHUNK_HEADER_SIZE) / SECTOR_BYTES + 1;

            // maximum chunk size is 1MB
            if (sectorsNeeded >= 256)
				return;

            if (sectorNumber != 0 && sectorsAllocated == sectorsNeeded) {
                /* we can simply overwrite the old sectors */
                this.debug("SAVE", x, z, length, "rewrite");
                this.write(sectorNumber, data, length);
            } else {
                /* we need to allocate new sectors */

                /* mark the sectors previously used for this chunk as free */
                for (int i = 0; i < sectorsAllocated; ++i)
					this.sectorFree.set(sectorNumber + i, true);

                /* scan for a free space large enough to store this chunk */
                int runStart = this.sectorFree.indexOf(true);
                int runLength = 0;
                if (runStart != -1)
					for (int i = runStart; i < this.sectorFree.size(); ++i) {
                        if (runLength != 0) {
                            if (this.sectorFree.get(i)) runLength++;
                            else runLength = 0;
                        } else if (this.sectorFree.get(i)) {
                            runStart = i;
                            runLength = 1;
                        }
                        if (runLength >= sectorsNeeded)
							break;
                    }

                if (runLength >= sectorsNeeded) {
                    /* we found a free space large enough */
                    this.debug("SAVE", x, z, length, "reuse");
                    sectorNumber = runStart;
                    this.setOffset(x, z, (sectorNumber << 8) | sectorsNeeded);
                    for (int i = 0; i < sectorsNeeded; ++i)
						this.sectorFree.set(sectorNumber + i, false);
                    this.write(sectorNumber, data, length);
                } else {
                    /*
                     * no free space large enough found -- we need to grow the
                     * file
                     */
                    this.debug("SAVE", x, z, length, "grow");
                    this.file.seek(this.file.length());
                    sectorNumber = this.sectorFree.size();
                    for (int i = 0; i < sectorsNeeded; ++i) {
                        this.file.write(emptySector);
                        this.sectorFree.add(false);
                    }
                    this.sizeDelta += SECTOR_BYTES * sectorsNeeded;

                    this.write(sectorNumber, data, length);
                    this.setOffset(x, z, (sectorNumber << 8) | sectorsNeeded);
                }
            }
            this.setTimestamp(x, z, (int) (System.currentTimeMillis() / 1000L));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* write a chunk data to the region file at specified sector number */
    private void write(int sectorNumber, byte[] data, int length) throws IOException {
        this.debugln(" " + sectorNumber);
        this.file.seek(sectorNumber * SECTOR_BYTES);
        this.file.writeInt(length + 1); // chunk length
        this.file.writeByte(VERSION_DEFLATE); // chunk version number
        this.file.write(data, 0, length); // chunk data
    }

    /* is this an invalid chunk coordinate? */
    private boolean outOfBounds(int x, int z) {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    private int getOffset(int x, int z) {
        return this.offsets[x + z * 32];
    }

    public boolean hasChunk(int x, int z) {
        return this.getOffset(x, z) != 0;
    }

    private void setOffset(int x, int z, int offset) throws IOException {
        this.offsets[x + z * 32] = offset;
        this.file.seek((x + z * 32) * 4);
        this.file.writeInt(offset);
    }

    private void setTimestamp(int x, int z, int value) throws IOException {
        this.chunkTimestamps[x + z * 32] = value;
        this.file.seek(SECTOR_BYTES + (x + z * 32) * 4);
        this.file.writeInt(value);
    }

    public void close() throws IOException {
        this.file.close();
    }
}