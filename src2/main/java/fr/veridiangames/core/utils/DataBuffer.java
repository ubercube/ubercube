/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Mimus.
 */
public class DataBuffer
{
	public static final int	HALF_BYTE	= 0xf;
	public static final int	ONE_BYTE	= 0xff;
	public static final int	TWO_BYTE	= 0xffff;
	public static final int	THREE_BYTE	= 0xffffff;
	public static final int	FOUR_BYTE	= 0xffffffff;

	private int		writeID;
	private int		readID;
	private byte[]	data;
	private int		dataSize;

	public DataBuffer()
	{
		this(512);
	}

	public DataBuffer(byte[] data)
	{
		this.dataSize = data.length;
		this.data = data;
		this.writeID = 0;
		this.readID = 0;
	}

	public DataBuffer(int size)
	{
		this.dataSize = size;
		this.data = new byte[this.dataSize];
		this.writeID = 0;
		this.readID = 0;
	}

	public void flip()
	{
		if (this.writeID != 0)
		{
			byte[] nData = new byte[this.writeID];
			for (int i = 0; i < this.writeID; i++)
				nData[i] = this.data[i];
			this.data = nData;
		}
		else if (this.readID != 0)
		{
			byte[] nData = new byte[this.readID];
			for (int i = 0; i < this.readID; i++)
				nData[i] = this.data[i];
			this.data = nData;
		}
	}

	public void clear()
	{
		if (this.data != null)
			for (int i = 0; i < this.data.length; i++)
				this.data[i] = 0;
		this.data = new byte[this.dataSize];
		this.writeID = 0;
		this.readID = 0;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public byte[] getData()
	{
		return this.data;
	}

	public int size()
	{
		return this.getData().length;
	}

	public void put(byte value)
	{
		if (this.writeID >= this.data.length)
		{
			System.err.println("Write Overflow..." + this.writeID + "\n\tMax capacity: " + this.data.length);
			return;
		}
		this.data[this.writeID] = value;
		this.writeID++;
	}

	public void put(byte... values)
	{
		for (int i = 0; i < values.length; i++)
			this.put(values[i]);
	}

	public byte getByte()
	{
		if (this.readID >= this.data.length)
		{
			System.err.println("Read Overflow..." + this.readID + "\n\tMax capacity: " + this.data.length);
			return -1;
		}
		return this.data[this.readID++];
	}

	public void put(short w)
	{
		this.put((byte) (w >> 8));
		this.put((byte) (w));
	}

	public short getShort()
	{
		return ByteBuffer.wrap(new byte[]
		{ this.getByte(), this.getByte() }).getShort();
	}

	public void put(int w)
	{
		this.put((byte) (w >> 24));
		this.put((byte) (w >> 16));
		this.put((byte) (w >> 8));
		this.put((byte) (w));
	}

	public int getInt()
	{
		return ByteBuffer.wrap(new byte[]
		{ this.getByte(), this.getByte(), this.getByte(), this.getByte() }).getInt();
	}

	public void put(long w)
	{
		this.put((byte) (w >> 56));
		this.put((byte) (w >> 48));
		this.put((byte) (w >> 40));
		this.put((byte) (w >> 32));
		this.put((byte) (w >> 24));
		this.put((byte) (w >> 16));
		this.put((byte) (w >> 8));
		this.put((byte) w);
	}

	public long getLong()
	{
		return ByteBuffer.wrap(new byte[]
		{ this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte() }).getLong();
	}

	public void put(float w)
	{
		this.put(Float.floatToIntBits(w));
	}

	public float getFloat()
	{
		return ByteBuffer.wrap(new byte[]
		{ this.getByte(), this.getByte(), this.getByte(), this.getByte() }).getFloat();
	}

	public void put(double w)
	{
		this.put(Double.doubleToLongBits(w));
	}

	public double getDouble()
	{
		return ByteBuffer.wrap(new byte[]
		{ this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte(), this.getByte() }).getDouble();
	}

	public void put(String w)
	{
		byte b[] = w.getBytes();
		this.put(b.length);
		this.put(b);
	}

	public String getString()
	{
		byte[] b = new byte[this.getInt()];
		for (int i = 0; i < b.length; i++)
			b[i] = this.getByte();
		return new String(b);
	}

	public void write(String path)
	{
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(path);
			fos.write(this.getData());
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			Log.exception(e);
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
	}

	public boolean read(String path)
	{
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(path);
			fis.read(this.data);
			fis.close();

			return true;
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
	}
}
