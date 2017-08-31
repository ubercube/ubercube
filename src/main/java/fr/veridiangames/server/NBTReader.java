package fr.veridiangames.server;

import java.io.DataInputStream;
import java.io.IOException;

public class NBTReader 
{
	private final DataInputStream stream;
	private String tagName = null;
	public NBTReader(DataInputStream is)
	{
		stream = is;
		//We know that the input isn't a file input
		//so we can read only one byte per one byte, it won't be slow as a file would
	}
	public byte getNextByteTagData(String name) throws IOException
	{
		int i;
		while ((i = getNextTagTypeAndName()) != 1 || !tagName.equals(name))
		{
			if (i == 9 && stream.readByte() == 10) // List of compound
				stream.skip(4); // Skip list size
			else if (i != 10)
				this.skipNextTagData(i);
		}
		return stream.readByte();
	}
	public byte[] getNextByteArrayTagData(String name) throws IOException 
	{
		int i;
		while ((i = getNextTagTypeAndName()) != 7 || !tagName.equals(name))
		{
			if (i == 9 && stream.readByte() == 10) // List of compound
				stream.skip(4); // Skip list size
			else if (i != 10)
				this.skipNextTagData(i);
		}
		byte[] b = new byte[stream.readInt()];
		stream.readFully(b);
		return b;
	}
	public byte getNextTagTypeAndName() throws IOException 
	{
		byte type = stream.readByte();
		if (type != 0) // TAG_END has no name
		{
			short size = stream.readShort();
			byte[] b = new byte[size];
			stream.readFully(b);
			tagName = new String(b);
		}
		else
			tagName = null;
		//System.out.println(type + " ; " + tagName);
		return type;
	}
	public int skipNextTag() throws IOException
	{
		int type = getNextTagTypeAndName();
		skipNextTagData(type);
		return type;
	}
	public void skipNextTagData(int type) throws IOException 
	{
		//System.out.println("Type : "+type);
		if (type == 1)	//Byte
			stream.skip(1);
		if (type == 2)	//Short
			stream.skip(2);
		if (type == 3)	//Int
			stream.skip(4);
		if (type == 4)	//Long
			stream.skip(8);
		if (type == 5)	//Float
			stream.skip(4);
		if (type == 6)	//Double
			stream.skip(8);
		if (type == 7)	//Byte Array
			stream.skip(stream.readInt());
		if (type == 8) //String
			stream.skip(stream.readShort());
		if (type == 9)//List of tag
		{
			byte tagId = stream.readByte();
			skipListData(tagId);
		}
		if (type == 10)//Compound
			while (this.skipNextTag() != 0);
		if (type == 11)//Int array
			stream.skip(stream.readInt() * 4);
		if (type == 12)//Long array
			stream.skip(stream.readInt() * 8);
	}
	private void skipListData(byte tagData) throws IOException
	{
		int length = stream.readInt();
		for (int i=0;i<length;i++)
			skipNextTagData(tagData);
	}
}
