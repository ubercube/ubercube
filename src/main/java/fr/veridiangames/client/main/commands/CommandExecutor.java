package fr.veridiangames.client.main.commands;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

/**
 * Created by Jimi Vacarians on 23/09/2017.
 */
public class CommandExecutor {
	private Map<String, Command> commands = new HashMap<>();

	public CommandExecutor()
	{
		commands.put("exit", () -> {
			exit(0);
		});
	}
	public boolean isCommand(String s)
	{
		if(s.startsWith("/"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void exec(String command)
	{
		command = command.substring(1);
		commands.get(command).exec();
	}
}
