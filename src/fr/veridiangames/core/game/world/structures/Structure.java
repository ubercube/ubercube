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

package fr.veridiangames.core.game.world.structures;

import fr.veridiangames.core.game.data.models.ModelVox;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.loaders.ModelLoader;

/**
 * Created by Marccspro on 26 mars 2016.
 */
public class Structure
{
	public static final Structure DESTROYED_BUILDING_0 = new Structure(ModelLoader.loadVox("res/structs/Bat1.vox"));

	private ModelVox model;

	public Structure(ModelVox model)
	{
		this.model = model;
	}

	public ModelVox getModel()
	{
		return model;
	}
	
	public void placeStructure(World world, int xp, int yp, int zp)
	{
		int xSize = model.getxMax() - model.getxMin();
		int ySize = model.getyMax() - model.getyMin();
		int zSize = model.getzMax() - model.getzMin();
		
		for (int x = 0; x < xSize; x++)
		{
			for (int y = 0; y < ySize; y++)
			{
				for (int z = 0; z < zSize; z++)
				{
					int xx = x + xp;
					int yy = y + yp;
					int zz = z + zp;
					
					if (xx < 0 || yy < 0 || zz < 0 || xx >= Chunk.SIZE || yy >= Chunk.SIZE || zz >= Chunk.SIZE)
						continue;
					
					world.addBlock(xx, yy, zz, model.getData()[x][y][z]); 
				}	
			}	
		}
	}
}