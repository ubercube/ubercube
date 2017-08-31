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
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.modes.GameMode;
import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.physics.PhysicsEngine;

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
		this.gameMode = new TDMGameMode(this.data);
	}

	public void createWorld(long seed)
	{
		this.world = new World(this.core, seed);
	}

	public void update()
	{
		if (this.clientPlayer == null)
			return;

		this.entityManager.update(this.core);
//		world.update();
	}

	public void updatePhysics()
	{
		this.physics.update(this.core, 10);
	}

	public void spawn(Entity e)
	{
		e.init(this.core);
		this.entityManager.add(e);
	}

	public void remove(int id)
	{
		this.entityManager.remove(id);
	}

	public ClientPlayer getPlayer()
	{
		return this.clientPlayer;
	}

	public void setPlayer(ClientPlayer clientPlayer)
	{
		this.clientPlayer = clientPlayer;
		this.spawn(this.clientPlayer);
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

    public GameData getData()
	{
		return this.data;
	}

	public World getWorld()
	{
		return this.world;
	}

	public PhysicsEngine getPhysics()
	{
		return this.physics;
	}

	public GameMode getGameMode(){ return this.gameMode; }

	public void setGameMode(GameMode mode){ this.gameMode = mode; }
}