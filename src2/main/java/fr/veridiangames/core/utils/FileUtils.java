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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marccspro on 29 janv. 2016.
 */
public class FileUtils
{
	public static final String LOG_PATH = "log.txt";

	public static String getHomeDir()
	{
		if (System.getProperty("os.name").indexOf("win") >= 0 || System.getProperty("os.name").indexOf("Win") >= 0)
			return System.getenv("APPDATA");
		else
			return System.getProperty("user.home");
	}

	public static BufferedWriter writeFile(String dir, boolean a)
	{
		try
		{
			FileWriter fileWriter = null;
			fileWriter = new FileWriter(dir, a);
			BufferedWriter w =  new BufferedWriter(fileWriter);
		return w;
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
		return null;
	}

	public static String getGitCommitsCount()
	{
		String result = "";
		File file = new File(".version");
		if (!file.exists())
			return "-42";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			result = reader.readLine();
			if (result == null)
				return "-42";
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
		return result.trim();
	}

	public static boolean newFile(String directory)
	{
		File file = new File(directory);
		if (file.exists()) {
			file = null;
			return true;
		}
		file.mkdirs();
		if (file.exists())
			return true;
		return false;
	}

	public static boolean removeFile(String directory)
	{
		File file = new File(directory);
		if (file.exists())
		{
			file.delete();
			if (!file.exists())
				return true;
		}
		return false;
	}

	public static String[] readInFile(String directory)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(directory));
			try
			{
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();

				while (line != null)
				{
					sb.append(line);
					sb.append("/n");
					line = reader.readLine();
				}
				return sb.toString().split("/n");
			}
			finally
			{
				reader.close();
			}
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
		return null;
	}

	public static int getFileSize(URL url)
	{
		HttpURLConnection conn = null;
		try
		{
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			conn.getInputStream();
			return conn.getContentLength();
		}
		catch (IOException e)
		{
			return -1;
		}
		finally
		{
			conn.disconnect();
		}
	}

	public static boolean fileExist(String path)
	{
		boolean result = false;
		if (new File(path).exists())
			result = true;
		return result;
	}

	public static String getTextFile(String path)
	{
		try
		{
			URL url = new URL(path);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			String lines = "";
			while ((line = in.readLine()) != null)
				lines += line;
			in.close();
			return lines;
		}
		catch (IOException e)
		{
			System.out.println("I/O Error: " + e.getMessage());
		}
		return "";
	}

	public static File getFile(String path)
	{
		return new File(path);
	}
}