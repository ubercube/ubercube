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

package fr.veridiangames.core.game.data.models;

/**
 * Created by Marccspro on 26 mars 2016.
 */
public class ModelVox
{
	private int			size;
	private int[][][]	data;
	private int			xMin, yMin, zMin;
	private int			xMax, yMax, zMax;
	private int			xSize, ySize, zSize;
	
	public ModelVox(int size, int[][][] data, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int xSize, int ySize, int zSize)
	{
		this.size = size;
		this.data = data;
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.zMax = zMax;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public int getBlock(int x, int y, int z)
	{
		if (x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize)
			return 0;
		
		return data[x][y][z];
	}
	public int[][][] getData()
	{
		return data;
	}
	public void setData(int[][][] data)
	{
		this.data = data;
	}
	public int getxMin()
	{
		return xMin;
	}
	public void setxMin(int xMin)
	{
		this.xMin = xMin;
	}
	public int getyMin()
	{
		return yMin;
	}
	public void setyMin(int yMin)
	{
		this.yMin = yMin;
	}
	public int getzMin()
	{
		return zMin;
	}
	public void setzMin(int zMin)
	{
		this.zMin = zMin;
	}
	public int getxMax()
	{
		return xMax;
	}
	public void setxMax(int xMax)
	{
		this.xMax = xMax;
	}
	public int getyMax()
	{
		return yMax;
	}
	public void setyMax(int yMax)
	{
		this.yMax = yMax;
	}
	public int getzMax()
	{
		return zMax;
	}
	public void setzMax(int zMax)
	{
		this.zMax = zMax;
	}
	public int getXSize()
	{
		return xSize;
	}
	public void setxSize(int xSize)
	{
		this.xSize = xSize;
	}
	public int getYSize()
	{
		return ySize;
	}
	public void setySize(int ySize)
	{
		this.ySize = ySize;
	}
	public int getZSize()
	{
		return zSize;
	}
	public void setzSize(int zSize)
	{
		this.zSize = zSize;
	}
}
