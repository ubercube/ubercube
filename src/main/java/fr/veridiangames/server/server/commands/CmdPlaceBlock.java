package fr.veridiangames.server.server.commands;

import java.math.BigInteger;
import java.util.ArrayList;

import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.network.packets.SyncBlocksPacket;
import fr.veridiangames.server.server.NetworkServer;

public class CmdPlaceBlock extends Command
{
	public CmdPlaceBlock()
	{
		super("placeBlock", "Place a block at specified coordinates");
	}
	@Override
	public void process(NetworkServer server, String[] params)
	{
		if (params.length == 5)
		{
			String para = params[1];
			if (para.length() > 8)
				server.log("Incorrect syntax : the value should have 8 hexit (e.g CF1756FA)");
			else
			{
				try
				{
					int value = new BigInteger(para, 16).intValue();
					int x = Integer.valueOf(params[2]), y = Integer.valueOf(params[3]), z = Integer.valueOf(params[4]);
					ArrayList<Vec4i> blocks = new ArrayList<Vec4i>();
					blocks.add(new Vec4i(x, y, z, value));
					server.getCore().getGame().getWorld().addModifiedBlock(x, y, z, value);
					server.tcpSendToAll(new SyncBlocksPacket(blocks));
					server.log("Setting block "+Integer.toHexString(value)+" at pos "+x+" "+y+" "+z);
				} catch(NumberFormatException e){server.log("Incorrect syntax : the value should be composed of hexit (0..9 A..F)");}
			}
		}
		else
			server.log("Incorrect syntax : placeBlock [value] [x] [y] [z]");
	}

}
