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

package fr.veridiangames.core.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class EntityManager
{
	private List<Integer>						keys;
	private ConcurrentHashMap<Integer, Entity>	entities;
	private List<Integer>						renderableEntities;
	private List<Integer>						networkableEntities;
	private List<Integer>						playerEntities;
	private List<Integer>						particleEntities;
	private List<Integer>						audioEntities;

	public EntityManager()
	{
		this.keys = new ArrayList<>();
		this.entities = new ConcurrentHashMap<>();
		this.renderableEntities = new ArrayList<>();
		this.networkableEntities = new ArrayList<>();
		this.playerEntities = new ArrayList<>();
		this.particleEntities = new ArrayList<>();
		this.audioEntities = new ArrayList<>();
	}

	public void update(GameCore core)
	{
		for (int i = 0; i < this.keys.size(); i++)
		{
			Entity e = this.get(this.keys.get(i));
			if (e == null)
			{
				this.remove(this.keys.get(i));
				continue;
			}
			e.update(core);

			if (e.isDestroyed())
				this.remove(this.keys.get(i));
		}
	}

	public void add(Entity e)
	{
		if (e.contains(EComponent.RENDER))
			this.renderableEntities.add(e.getID());

		if (e.contains(EComponent.NETWORK))
			this.networkableEntities.add(e.getID());

		if (e instanceof Player)
			this.playerEntities.add(e.getID());

		if (e instanceof ParticleSystem)
			this.particleEntities.add(e.getID());

		if (e instanceof AudioSource)
			this.audioEntities.add(e.getID());

		this.keys.add(e.getID());
		this.entities.put(e.getID(), e);
	}

	public void remove(int id)
	{
		if (this.renderableEntities.contains(id))
			this.renderableEntities.remove((Integer) id);

		if (this.networkableEntities.contains(id))
			this.networkableEntities.remove((Integer) id);

		if (this.playerEntities.contains(id))
		{
			Entity e = this.entities.get(id);
			if (e instanceof NetworkedPlayer)
			{
				NetworkedPlayer p = (NetworkedPlayer) e;
				GameCore.getInstance().getGame().getPhysics().removeBody(p.getBody());
			}
			this.playerEntities.remove((Integer) id);
		}

		if (this.particleEntities.contains(id))
			this.particleEntities.remove((Integer) id);

		if (this.audioEntities.contains(id))
			this.audioEntities.remove((Integer) id);

		this.entities.remove(id);
		this.keys.remove((Integer) id);
	}

	public Entity get(int id)
	{
		return this.entities.get(id);
	}

	public Map<Integer, Entity> getEntities()
	{
		return this.entities;
	}

	public List<Entity> getEntitiesInRange(Vec3 pos, float range)
	{
		List<Entity> result = new ArrayList<Entity>();
		for (Entry<Integer, Entity> entry : this.entities.entrySet())
		{
			Entity e = entry.getValue();
			if (e.contains(EComponent.RENDER))
			{
				Vec3 epos = ((ECRender) e.get(EComponent.RENDER)).getTransform().getPosition();
				float xx = epos.x - pos.x;
				float yy = epos.y - pos.y;
				float zz = epos.z - pos.z;
				float sqrt = xx * xx + yy * yy + zz * zz;

				if (sqrt < range * range)
					result.add(e);
			}
		}

		return result;
	}

	public List<Entity> getEntitiesAt(Vec3 point)
	{
		List<Entity> result = new ArrayList<>();
		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity e = this.entities.get(this.keys.get(i));
			if (e.contains(EComponent.RENDER))
			{
				Vec3 epos = ((ECRender) e.get(EComponent.RENDER)).getTransform().getPosition();
				Vec3 esize = ((ECRender) e.get(EComponent.RENDER)).getScale();

				if (point.x > epos.x - esize.x && point.x < epos.x + esize.x &&
						point.y > epos.y - esize.y && point.y < epos.y + esize.y &&
						point.z > epos.z - esize.z && point.z < epos.z + esize.z)
				{
					result.add(e);
					break;
				}
			}
		}
		return result;
	}

	// TODO: remove completely this fucked up function
	public Entity getEntityAt(Vec3 point, String... targetTags)
	{
		Entity result = null;
		try
		{
			for (int i = 0; i < this.entities.size(); i++)
			{
				Entity e = this.entities.get(this.keys.get(i));
				boolean jumpIteration = false;
				for (int j = 0; j < targetTags.length; j++)
				{
					String target = targetTags[j];
					if (!e.getTags().contains(target))
						jumpIteration = true;
					else
					{
						jumpIteration = false;
						continue;
					}
				}
				if (jumpIteration)
					continue;

				if (e.contains(EComponent.RENDER))
				{
					Vec3 epos = ((ECRender) e.get(EComponent.RENDER)).getTransform().getPosition();
					Vec3 esize = ((ECRender) e.get(EComponent.RENDER)).getScale();

					if (point.x > epos.x - esize.x && point.x < epos.x + esize.x &&
						point.y > epos.y - esize.y && point.y < epos.y + esize.y &&
						point.z > epos.z - esize.z && point.z < epos.z + esize.z)
					{
						result = e;
						break;
					}
				}
			}
		}
		catch (Exception e)
		{}
		return result;
	}

	public List<Integer> getKeys()
	{
		return this.keys;
	}

	public List<Integer> getRenderableEntites()
	{
		return this.renderableEntities;
	}

	public List<Integer> getNetworkableEntites()
	{
		return this.networkableEntities;
	}

	public List<Integer> getPlayerEntites()
	{
		return this.playerEntities;
	}

	public List<Integer> getParticleEntities()
	{
		return this.particleEntities;
	}

	public List<Integer> getAudioEntities()
	{
		return this.audioEntities;
	}
}
