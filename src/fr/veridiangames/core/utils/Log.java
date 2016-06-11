/*
 * Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Mimus.
 */
public class Log
{
	public static void resetLog()
	{
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try
		{
			fileWriter = new FileWriter(FileUtils.LOG_PATH, false);
			bufWriter = new BufferedWriter(fileWriter);
			bufWriter.write("New log file created at: " + SystemUtils.getDate());
			bufWriter.newLine();
			bufWriter.close();
		}
		catch (IOException ex)
		{
			System.err.println(ex.getLocalizedMessage());
		}
		finally
		{
			try
			{
				bufWriter.close();
				fileWriter.close();
			}
			catch (IOException ex)
			{
				System.err.println(ex.getLocalizedMessage());
			}
		}
	}

	public static void addLine(String filename, String text)
	{
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try
		{
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			bufWriter.write(text);
			bufWriter.newLine();
			bufWriter.close();
		}
		catch (IOException ex)
		{
			System.err.println(ex.getLocalizedMessage());
		}
		finally
		{
			try
			{
				bufWriter.close();
				fileWriter.close();
			}
			catch (IOException ex)
			{
				System.err.println(ex.getLocalizedMessage());
			}
		}
	}

	public static void print(String msg)
	{
		System.out.print(msg);
		addLine(FileUtils.LOG_PATH, msg);
	}

	public static void println(String msg)
	{
		System.out.println(msg);
		addLine(FileUtils.LOG_PATH, msg);
	}

	public static void print(String prefix, String msg)
	{
		System.err.print("[" + prefix + "] " + msg);
		addLine(FileUtils.LOG_PATH, "[" + prefix + "] " + SystemUtils.getDate() + " >> " + msg);
	}

	public static void println(String prefix, String msg)
	{
		System.err.println("[" + prefix + "] " + msg);
		addLine(FileUtils.LOG_PATH, "[" + prefix + "] " + SystemUtils.getDate() + " >> " + msg);
	}

	public static void info(String info)
	{
		System.out.println("[INF0] " + info);
		addLine(FileUtils.LOG_PATH, "[INFO] " + SystemUtils.getDate() + " >> " + info);
	}

	public static void error(String error)
	{
		System.err.println("[ERROR] " + error);
		addLine(FileUtils.LOG_PATH, "[ERROR] " + SystemUtils.getDate() + " >> " + error);
	}

	public static void sever(String sever)
	{
		System.err.println("[SEVER] " + sever);
		System.err.println("[CRASH] Application ended");
		addLine(FileUtils.LOG_PATH, "[SEVER] " + SystemUtils.getDate() + " >> " + sever);
		addLine(FileUtils.LOG_PATH, "[CRASH] " + SystemUtils.getDate() + " >> Application ended");
		System.exit(1);
	}
	
	public static void crash(String error)
	{
		throw new RuntimeException(error);
	}
}
