/*
 *   Copyright (C) 2016 Team Ubercube
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
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.rendering.renderers.game.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.client.rendering.Camera;

/**
 * Created by Marccspro on 27 f�vr. 2016.
 */
public class WorldRenderer
{
	private World						world;
	private Map<Integer, Chunk>			chunks;
	private Map<Integer, ChunkRenderer>	chunkRenderers;
	private ChunkDebugRenderer			chunkDebug;
	private List<Integer>				garbage;
	private List<Integer>				updateRequests;
	
	public WorldRenderer(GameCore core)
	{
		world = core.getGame().getWorld();
		chunks = world.getRenderableChunks();
		chunkRenderers = new HashMap<Integer, ChunkRenderer>();
		chunkDebug = new ChunkDebugRenderer();
		garbage = world.getChunkGarbage();
		updateRequests = world.getUpdateRequests();
	}

	public void update(Camera camera)
	{
		for (ChunkRenderer cr : chunkRenderers.values())
		{
			cr.updateCulling(camera);
		}
		for (Entry<Integer, Chunk> e : chunks.entrySet())
		{
			if (chunkRenderers.containsKey(e.getKey()))
				continue;
			Chunk c = e.getValue();
			ChunkRenderer cr = new ChunkRenderer(c, world);
			chunkRenderers.put(e.getKey(), cr);
		}
		for (int i = 0; i < garbage.size(); i++)
		{
			int index = garbage.get(i);
			if (chunkRenderers.containsKey(index))
			{
				chunks.remove(index);
				chunkRenderers.get(index).dispose();
				chunkRenderers.remove(index);
			}
			garbage.remove(i);
		}
		chunkDebug.updateInstances(chunks);
		for (int i = 0;  i < updateRequests.size(); i++)
		{
			int index = updateRequests.get(i);
			chunkRenderers.get(index).update();
		}
		updateRequests.clear();
	}

	public void render()
	{
		//glBindTexture(GL_TEXTURE_CUBE_MAP, CubeMap.DEFAULT_CUBEMAP.getTexture());
		for (ChunkRenderer e : chunkRenderers.values())
		{
			e.render();
		}
		//glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
		
		//chunkDebug.render(chunks);
	}
}
