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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.utils;

import fr.veridiangames.core.maths.Vec3i;

public class Indexer
{
	public static void main(String[] args)
	{
		int x = 1;
		int y = 1;
		int z = 1;
		
		int index = index3i(new Vec3i(x, y, z));
		Vec3i solve = solve3i(index);
		
		System.out.println("Start: " + x + " " + y + " " + z);
		System.out.println("Index: " + index);
		System.out.println("Solve: " + solve);
	}
	
	public static int index3i(Vec3i pos)
	{
		return pos.x << 16 | pos.y << 8 | pos.z;
	}
	
	public static int index3i(int x, int y, int z)
	{
		return x << 16 | y << 8 | z;
	}
	
	public static Vec3i solve3i(int index)
	{
		int x = (index & 0xff0000) >> 16;
		int y = (index & 0xff00) >> 8;
		int z = (index & 0xff);
		
		return new Vec3i(x, y, z);
	}
	
	private static int currentIndex = 0;
	
	public static int getUniqueID()
	{
		currentIndex++;
		return (int) (System.nanoTime() * currentIndex);
	}
}
