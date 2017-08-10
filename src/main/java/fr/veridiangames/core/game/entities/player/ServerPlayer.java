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

package fr.veridiangames.core.game.entities.player;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.DeathPacket;

/**
 * Created by Marccspro on 23 f�vr. 2016.
 */
public class ServerPlayer extends Player
{
	private int life;
	private int timeOutTests;
	private long pingTime;
	private boolean pinged;
	private boolean hitable;

	private float high;
	private boolean falling;

	private Vec3 onlineVelocity;

	public ServerPlayer(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id, name, position, rotation, address, port);
		this.life = 100;
		this.timeOutTests = 0;
		this.pingTime = 0;
		this.pinged = false;

		this.falling = false;
		this.high = 0.0f;
	}

	public boolean applyDamage (int damage, NetworkableServer server)
	{
		this.life -= damage;

		if(this.life <= 0 && !this.isDead())
		{
			server.tcpSendToAll(new DeathPacket(this.getID()));

			/* GAME MODE */
			server.getCore().getGame().getGameMode().onPlayerDeath(this, server);

			this.setDead(true);
			String name = ((ECName) this.get(EComponent.NAME)).getName();
			server.log(name + " was killed !");
			return true;
		}
		return false;
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public int getTimeOutTests()
	{
		return timeOutTests;
	}

	public void setTimeOutTests(int timeOutTests)
	{
		this.timeOutTests = timeOutTests;
	}

	public long getPingTime()
	{
		return pingTime;
	}

	public void setPingTime(long pingTime)
	{
		this.pingTime = pingTime;
	}

	public boolean isPinged()
	{
		return pinged;
	}

	public void setPinged(boolean pinged)
	{
		this.pinged = pinged;
	}

	@Override
	public boolean isHitable()
	{
		return hitable;
	}

	public void setHitable(boolean hitable)
	{
		this.hitable = hitable;
	}

	public void setOnlineVelocity(Vec3 onlineVelocity)
	{
		this.onlineVelocity = onlineVelocity;
	}

	public Vec3 getOnlineVelocity()
	{
		return onlineVelocity;
	}

	public void setFalling(boolean falling)
	{
		this.falling = falling;
	}

	public boolean isFalling()
	{
		return falling;
	}

	public void setHigh(float high)
	{
		this.high = high;
	}

	public float getHigh()
	{
		return high;
	}
}
