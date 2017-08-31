package fr.veridiangames.server;

import java.io.DataInputStream;
import java.io.IOException;

public class NBTReader
{
	private final DataInputStream stream;
	private String tagName = null;
	public NBTReader(DataInputStream is)
	{
		this.stream = is;
		//We know that the input isn't a file input
		//so we can read only one byte per one byte, it won't be slow as a file would
	}
	public byte getNextByteTagData(String name) throws IOException
	{
		int i;
		while ((i = this.getNextTagTypeAndName()) != 1 || !this.tagName.equals(name))
			if (i == 9 && this.stream.readByte() == 10) // List of compound
				this.stream.skip(4); // Skip list size
			else if (i != 10)
				this.skipNextTagData(i);
		return this.stream.readByte();
	}
	public byte[] getNextByteArrayTagData(String name) throws IOException
	{
		int i;
		while ((i = this.getNextTagTypeAndName()) != 7 || !this.tagName.equals(name))
			if (i == 9 && this.stream.readByte() == 10) // List of compound
				this.stream.skip(4); // Skip list size
			else if (i != 10)
				this.skipNextTagData(i);
		byte[] b = new byte[this.stream.readInt()];
		this.stream.readFully(b);
		return b;
	}
	public byte getNextTagTypeAndName() throws IOException
	{
		byte type = this.stream.readByte();
		if (type != 0) // TAG_END has no name
		{
			short size = this.stream.readShort();
			if (size < 0)
				System.out.println(type);
			byte[] b = new byte[size];
			this.stream.readFully(b);
			this.tagName = new String(b);
		}
		else
			this.tagName = null;
		return type;
	}
	public int skipNextTag() throws IOException
	{
		int type = this.getNextTagTypeAndName();
		this.skipNextTagData(type);
		return type;
	}
	public void skipNextTagData(int type) throws IOException
	{
		//System.out.println("Type : "+type);
		if (type == 1)	//Byte
			this.stream.skip(1);
		if (type == 2)	//Short
			this.stream.skip(2);
		if (type == 3)	//Int
			this.stream.skip(4);
		if (type == 4)	//Long
			this.stream.skip(8);
		if (type == 5)	//Float
			this.stream.skip(4);
		if (type == 6)	//Double
			this.stream.skip(8);
		if (type == 7)	//Byte Array
			this.stream.skip(this.stream.readInt());
		if (type == 8) //String
			this.stream.skip(this.stream.readShort());
		if (type == 9)//List of tag
		{
			byte tagId = this.stream.readByte();
			this.skipListData(tagId);
		}
		if (type == 10)//Compound
			while (this.skipNextTag() != 0);
		if (type == 11)//Int array
			this.stream.skip(this.stream.readInt() * 4);
		if (type == 12)//Long array
			this.stream.skip(this.stream.readInt() * 8);
	}
	private void skipListData(byte tagData) throws IOException
	{
		int length = this.stream.readInt();
		for (int i=0;i<length;i++)
			this.skipNextTagData(tagData);
	}
}
