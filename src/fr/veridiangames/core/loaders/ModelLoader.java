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

package fr.veridiangames.core.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fr.veridiangames.core.game.data.models.ModelVox;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marccspro on 26 mars 2016.
 */
public class ModelLoader
{
	public static ModelVox loadVox(String modelPath)
	{
		ModelVox result = null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(modelPath));
			String line = reader.readLine();
			
			String minData[] = line.split("//")[1].split(",");
			String maxData[] = line.split("//")[2].split(","); 
			
			int size = Integer.valueOf(line.split("//")[0]);
			
			int xMin = (int) Float.parseFloat(minData[0]);
			int yMin = (int) Float.parseFloat(minData[1]);
			int zMin = (int) Float.parseFloat(minData[2]);

			int xMax = (int) Float.parseFloat(maxData[0]);
			int yMax = (int) Float.parseFloat(maxData[1]);
			int zMax = (int) Float.parseFloat(maxData[2]);
			
			int xSize = xMax - xMin + 1;
			int ySize = yMax - yMin + 1;
			int zSize = zMax - zMin + 1;
			
			int[][][] blockdata = new int[xSize][ySize][zSize];
			
			while ((line = reader.readLine()) != null)
			{
				String data[] = line.split("//");
				String posData[] = data[0].split(",");
				String colorData[] = data[1].split(",");

				int x = Integer.valueOf(posData[0]) - xMin;
				int y = Integer.valueOf(posData[1]) - yMin;
				int z = Integer.valueOf(posData[2]) - zMin;

				float r = Float.valueOf(colorData[0]);
				float g = Float.valueOf(colorData[1]);
				float b = Float.valueOf(colorData[2]);
				float a = Float.valueOf(colorData[3]);
				
				Color4f rgba = new Color4f(r, g, b, a);
				int color = rgba.getARGB();
				
				blockdata[x][y][z] = color;
			}
			reader.close();
			
			result = new ModelVox(size, blockdata, xMin, yMin, zMin, xMax, yMax, zMax, xSize, ySize, zSize);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
}
