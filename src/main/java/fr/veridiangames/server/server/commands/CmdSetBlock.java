package fr.veridiangames.server.server.commands;

import java.math.BigInteger;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.packets.CurrentBlockPacket;
import fr.veridiangames.server.server.NetworkServer;

public class CmdSetBlock extends Command
{
	public CmdSetBlock()
	{
		super("setBlock", "Set player's current block to the hexa value given");
	}
	@Override
	public void process(NetworkServer server, String[] params)
	{
		if (params.length == 3)
		{
			String para = params[1];
			if (para.length() > 8)
				server.log("Incorrect syntax : the value should have 8 hexit (e.g CF1756FA)");
			else
			{
				try
				{
					int value = new BigInteger(para, 16).intValue();
					int id = -1;
	                String name = "";
	                for (Map.Entry<Integer, Entity> e : GameCore.getInstance().getGame().getEntityManager().getEntities().entrySet())
	                {
	                    name = ((ECName) GameCore.getInstance().getGame().getEntityManager().get(e.getKey()).get(EComponent.NAME)).getName();
	                    if (params[2].equals(name))
	                    {
	                        id = e.getKey();
	                    }
	                }
					if (id == -1)
					{
						server.log("Player not found !");
	                    return;
					}
					server.tcpSendToAll(new CurrentBlockPacket(value, id));
					server.log(params[2] + " has now the block "+Integer.toHexString(value));
				} catch(NumberFormatException e){server.log("Incorrect syntax : the value should be composed of hexit (0..9 A..F)");}
			}
		}
		else
			server.log("Incorrect syntax : setBlock [value] [player]");
	}

}
