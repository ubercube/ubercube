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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mimus.
 */
public class SystemUtils
{
	public static OS getOS()
	{
		if (isWindows())
			return OS.WINDOWS;
		else if (isMac())
			return OS.MACOSX;
		else if (isUnix())
			return OS.LINUX;
		else
			return OS.UNKNOW;
	}

	public static boolean isWindows()
	{
		String osName = System.getProperty("os.name");
		return (osName.indexOf("win") >= 0 || osName.indexOf("Win") >= 0);
	}

	public static boolean isMac()
	{
		String osName = System.getProperty("os.name");
		return (osName.indexOf("mac") >= 0 || osName.indexOf("Mac") >= 0);
	}

	public static boolean isUnix()
	{
		String osName = System.getProperty("os.name");
		return (osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0);
	}

	public static boolean isSolaris()
	{
		String osName = System.getProperty("os.name");
		return (osName.indexOf("sunos") >= 0 || osName.indexOf("Sunos") >= 0);
	}

	public static int availableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}

	public static long freeMemory()
	{
		return Runtime.getRuntime().freeMemory();
	}

	public static long maxMemory()
	{
		return Runtime.getRuntime().maxMemory();
	}

	public static long totalMemory()
	{
		return Runtime.getRuntime().totalMemory();
	}

	public static String getDate()
	{
		Date date = new Date();
		SimpleDateFormat dateFormatComp;
		dateFormatComp = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
		return dateFormatComp.format(date);
	}

	public static void printRootInfos()
	{
		File[] roots = File.listRoots();
		for (File root : roots)
		{
			System.out.println("File system root: " + root.getAbsolutePath());
			System.out.println("Total space (bytes): " + root.getTotalSpace());
			System.out.println("Free space (bytes): " + root.getFreeSpace());
			System.out.println("Usable space (bytes): " + root.getUsableSpace());
		}
	}

	public static void printInfos()
	{
		System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());

		System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

		System.out.println("Total memory (bytes): " + Runtime.getRuntime().totalMemory());

		File[] roots = File.listRoots();
		for (File root : roots)
		{
			System.out.println("File system root: " + root.getAbsolutePath());
			System.out.println("Total space (bytes): " + root.getTotalSpace());
			System.out.println("Free space (bytes): " + root.getFreeSpace());
			System.out.println("Usable space (bytes): " + root.getUsableSpace());
		}
	}

	public static String getOSName() { return System.getProperty("os.name"); };
	public static String getOSVersion() { return System.getProperty("os.version"); };
	public static String getOSArchitecture() { return System.getProperty("os.arch"); };
	public static String getJavaVersion() { return System.getProperty("java.version"); };
}
