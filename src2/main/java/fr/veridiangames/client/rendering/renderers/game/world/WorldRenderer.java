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

package fr.veridiangames.client.rendering.renderers.game.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;

/**
 * Created by Marccspro on 27 f�vr. 2016.
 */
public class WorldRenderer
{
	private World						world;
	private Map<Integer, Chunk>			chunks;
	private Map<Integer, ChunkRenderer>	chunkRenderers;
	private ChunkDebugRenderer			chunkDebug;
	private List<Integer>				updateRequests;

	public WorldRenderer(GameCore core)
	{
		this.world = core.getGame().getWorld();
		this.chunks = this.world.getRenderableChunks();
		this.chunkRenderers = new HashMap<Integer, ChunkRenderer>();
		this.chunkDebug = new ChunkDebugRenderer();
		this.updateRequests = this.world.getUpdateRequests();
	}

	public void update(Camera camera)
	{
		for (ChunkRenderer cr : this.chunkRenderers.values())
			cr.updateCulling(camera);
		if (this.chunkRenderers.size() != this.chunks.size())
			for (Entry<Integer, Chunk> e : this.chunks.entrySet()) {
				if (this.chunkRenderers.containsKey(e.getKey()))
					continue;
				Chunk c = e.getValue();
				ChunkRenderer cr = new ChunkRenderer(c, this.world);
				this.chunkRenderers.put(e.getKey(), cr);
			}
		for (int i = 0; i < this.updateRequests.size(); i++)
		{
			int index = this.updateRequests.get(i);
			this.chunkRenderers.get(index).update();
		}
		this.updateRequests.clear();
	}

	public void render()
	{
		//glBindTexture(GL_TEXTURE_CUBE_MAP, CubeMap.DEFAULT_CUBEMAP.getTexture());
		for (ChunkRenderer e : this.chunkRenderers.values())
		 e.render();

		//chunkDebug.render(chunks);
	}
}
