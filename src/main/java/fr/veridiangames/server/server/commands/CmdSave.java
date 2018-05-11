package fr.veridiangames.server.server.commands;

import fr.veridiangames.core.utils.Log;
import fr.veridiangames.server.server.NetworkServer;

public class CmdSave extends Command
{
	public CmdSave()
	{
		super("save", "Save the current world in filePath arg.");
	}

	@Override
	public void process(NetworkServer server, String[] params)
	{
		try
		{
			server.getCore().getGame().saveWorld(params[1]);
		}
		catch(Exception e)
		{
			Log.println("Can't save world !");
		}
	}
}
