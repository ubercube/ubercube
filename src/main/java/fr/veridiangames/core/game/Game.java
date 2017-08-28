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

package fr.veridiangames.core.game;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.data.world.WorldGen;
import fr.veridiangames.core.game.data.world.WorldType;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesManager;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.PhysicsEngine;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;
import fr.veridiangames.core.game.modes.GameMode;
import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.utils.Log;

/**
 * Created by Marccspro on 28 janv. 2016.
 */
public class Game
{
	private GameCore core;

	private GameData		    data;
	private EntityManager	    entityManager;
	private World			    world;
	private PhysicsEngine	    physics;
	private ClientPlayer	    clientPlayer;
	private GameMode			gameMode;

	public Game(GameCore core)
	{
		this.core = core;
		this.data = new GameData();
		this.physics = new PhysicsEngine();
		this.entityManager = new EntityManager();
		this.gameMode = new TDMGameMode(data);
	}

	public void createWorld(long seed)
	{
		Log.println("Generating world with seed: " + seed);
		this.data.createWorld(new WorldGen(seed, data.getWorldSize()), (seed % 2 == 0) ? WorldType.SNOWY : WorldType.NORMAL);
		this.world = new World(core);
	}

	public void update()
	{
		if (clientPlayer == null)
			return;

		entityManager.update(core);
//		world.update();
	}

	public void updatePhysics()
	{
		physics.update(core, 10);
	}

	public void spawn(Entity e)
	{
		e.init(core);
		entityManager.add(e);
	}

	public void remove(int id)
	{
		entityManager.remove(id);
	}

	public ClientPlayer getPlayer()
	{
		return clientPlayer;
	}

	public void setPlayer(ClientPlayer clientPlayer)
	{
		this.clientPlayer = clientPlayer;
		spawn(this.clientPlayer);
	}

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

    public GameData getData()
	{
		return data;
	}

	public World getWorld()
	{
		return world;
	}

	public PhysicsEngine getPhysics()
	{
		return physics;
	}

	public GameMode getGameMode(){ return gameMode; }

	public void setGameMode(GameMode mode){ gameMode = mode; }
}