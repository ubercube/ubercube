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
		this(2048);
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
		this.data = new byte[dataSize];
		this.writeID = 0;
		this.readID = 0;
	}

	public void flip()
	{
		if (writeID != 0)
		{
			byte[] nData = new byte[writeID];
			for (int i = 0; i < writeID; i++)
			{
				nData[i] = data[i];
			}
			data = nData;
		}
		else if (readID != 0)
		{
			byte[] nData = new byte[readID];
			for (int i = 0; i < readID; i++)
			{
				nData[i] = data[i];
			}
			data = nData;
		}
	}

	public void clear()
	{
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				data[i] = 0;
			}
		}
		data = new byte[dataSize];
		writeID = 0;
		readID = 0;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public byte[] getData()
	{
		return data;
	}

	public int size()
	{
		return getData().length;
	}

	public void put(byte value)
	{
		if (writeID >= data.length)
		{
			System.err.println("Write Overflow..." + writeID + "\n\tMax capacity: " + data.length);
			return;
		}
		data[writeID] = value;
		writeID++;
	}

	public void put(byte... values)
	{
		for (int i = 0; i < values.length; i++)
		{
			put(values[i]);
		}
	}

	public byte getByte()
	{
		if (readID >= data.length)
		{
			System.err.println("Read Overflow..." + readID + "\n\tMax capacity: " + data.length);
			return -1;
		}
		return data[readID++];
	}

	public void put(short w)
	{
		put((byte) (w >> 8));
		put((byte) (w));
	}

	public short getShort()
	{
		return ByteBuffer.wrap(new byte[]
		{ getByte(), getByte() }).getShort();
	}

	public void put(int w)
	{
		put((byte) (w >> 24));
		put((byte) (w >> 16));
		put((byte) (w >> 8));
		put((byte) (w));
	}

	public int getInt()
	{
		return ByteBuffer.wrap(new byte[]
		{ getByte(), getByte(), getByte(), getByte() }).getInt();
	}

	public void put(long w)
	{
		put((byte) (w >> 56));
		put((byte) (w >> 48));
		put((byte) (w >> 40));
		put((byte) (w >> 32));
		put((byte) (w >> 24));
		put((byte) (w >> 16));
		put((byte) (w >> 8));
		put((byte) w);
	}

	public long getLong()
	{
		return ByteBuffer.wrap(new byte[]
		{ getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte() }).getLong();
	}

	public void put(float w)
	{
		put(Float.floatToIntBits(w));
	}

	public float getFloat()
	{
		return ByteBuffer.wrap(new byte[]
		{ getByte(), getByte(), getByte(), getByte() }).getFloat();
	}

	public void put(double w)
	{
		put(Double.doubleToLongBits(w));
	}

	public double getDouble()
	{
		return ByteBuffer.wrap(new byte[]
		{ getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte() }).getDouble();
	}

	public void put(String w)
	{
		byte b[] = w.getBytes();
		put(b.length);
		put(b);
	}

	public String getString()
	{
		byte[] b = new byte[getInt()];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = getByte();
		}
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
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean read(String path)
	{
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(path);
			fis.read(data);
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
