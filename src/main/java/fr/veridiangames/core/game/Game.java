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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.data.world.WorldGen;
import fr.veridiangames.core.game.data.world.WorldType;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.game.gamemodes.TDMGameMode;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.physics.PhysicsEngine;
import fr.veridiangames.core.game.gamemodes.GameMode;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.server.ServerMain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

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
		this.data.createWorld(new WorldGen(seed, data.getWorldSize()), WorldType.NORMAL);//(seed % 2 == 0) ? WorldType.SNOWY : WorldType.NORMAL);
		this.world = new World(core);
	}

	public void loadWorld(String filePath)
	{

		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath)))
		{
			long seed = ois.readLong();
			List<Vec4i> mb = new ArrayList<Vec4i>();
			long bn = ois.readLong();

			for(int i = 0; i<bn; i++)
			{
				mb.add(new Vec4i(ois.readInt(), ois.readInt(), ois.readInt(), ois.readInt()));
			}

			Log.println("Loading world " + filePath);
			this.data.createWorld(new WorldGen(seed, data.getWorldSize()), WorldType.NORMAL);//(seed % 2 == 0) ? WorldType.SNOWY : WorldType.NORMAL);
			this.world = new World(core, mb);
		}
		catch(Exception e)
		{
			Log.println("Can't load the world in " + filePath);
			createWorld(abs(new Random().nextInt()));
		}
	}

	public void saveWorld(String filePath)
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath)))
		{
			oos.writeLong(world.getWorldGen().getSeed());
			oos.writeLong(world.getModifiedBlocks().size());

			Vec4i v;
			for(int i = 0; i<world.getModifiedBlocks().size(); i++)
			{
				v = world.getModifiedBlocks().get(i);
				oos.writeInt(v.x);
				oos.writeInt(v.y);
				oos.writeInt(v.z);
				oos.writeInt(v.w);
			}

			Log.println("World saved in " + filePath);
		}
		catch(Exception e)
		{
			Log.exception(e);
			Log.println("Can't save the world in " + filePath);
		}
	}

	public void update()
	{
		if (clientPlayer == null)
		{
			gameMode.serverUpdate(ServerMain.getInstance().getNet());
			Log.println("server");
		}
		else
		{
			entityManager.update(core);
			//		world.clientUpdate();
			gameMode.clientUpdate(Ubercube.getInstance().getNet());
		}
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