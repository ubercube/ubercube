package fr.veridiangames.server.server;

import fr.veridiangames.core.utils.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration
{
	private Map<String, String> properties = new HashMap<>();

	public void load(String filePath)
	{
		try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{
			String s;
			String p[];
			for(Object o : br.lines().toArray())
			{
				s = (String) o;

				p = s.split("=");

				properties.put(p[0], p[1]);
			}
		}
		catch(Exception e)
		{
			Log.println("Can't load config file !");
			reset(filePath);
		}
	}

	public void reset(String filePath)
	{
		try(PrintWriter pw = new PrintWriter(new FileWriter(filePath)))
		{
			pw.println("port=4242");
			pw.println("maxPlayer=20");
			pw.println("mapPath=map.data");
		}
		catch(Exception e)
		{
			Log.println("Can't reset config file !");
		}
	}

	public String get(String key)
	{
		return properties.get(key);
	}
}
