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

package fr.veridiangames.client.main.player;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.packets.BlockActionPacket;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.network.NetworkClient;

/**
 * Created by Marccspro on 14 f�vr. 2016.
 */
public class PlayerHandler
{
	private ClientPlayer	player;
	private PlayerSelection	selection;
	private GameCore 		core;
	private NetworkClient 	net;
	
	private boolean 		showEnvSphere;
	private Vec3 			envSpherePos;

	public PlayerHandler(GameCore core, NetworkClient net)
	{
		this.core = core;
		this.selection = new PlayerSelection();
		this.player = core.getGame().getPlayer();
		this.net = net;
		this.envSpherePos = new Vec3();
	}

	public void update(Input input)
	{
		ECKeyMovement key = player.getKeyComponent();
		ECMouseLook mouse = player.getMouseComponent();
		ECRaycast ray = player.getRaycast();
		ECWeapon weapon = player.getWeaponManager();
		ECDebug debug = player.getDebugComponent();
		
		mouse.setDX(0);
		mouse.setDY(0);
		
		if (input.getMouse().getButton(0))
			input.getMouse().setGrabbed(true);
		if (input.getKey(Input.KEY_ESCAPE))
			input.getMouse().setGrabbed(false);
		if (!input.getMouse().isGrabbed())
			return;
		
		if (input.getKeyUp(Input.KEY_W))
		{
			showEnvSphere = !showEnvSphere;
			envSpherePos = player.getPosition().copy().add(0, 2.5f * 0.5f, 0).copy().add(player.getRotation().getForward().copy().mul(5));
		}
		
		
		if (input.getMouse().getButton(0))
			weapon.getWeapon().shoot();
		
		if (input.getMouse().getButton(1))
			weapon.getWeapon().setPosition(1);
		else
			weapon.getWeapon().setPosition(0);

		key.setUp(input.getKey(Input.KEY_Z));
		key.setDown(input.getKey(Input.KEY_S));
		key.setLeft(input.getKey(Input.KEY_Q));
		key.setRight(input.getKey(Input.KEY_D));
		key.setJump(input.getKey(Input.KEY_SPACE));
		key.setCrouche(input.getKey(Input.KEY_LEFT_SHIFT));

		mouse.setDX(input.getMouse().getDX());
		mouse.setDY(input.getMouse().getDY());

		ray.setPosition(player.getPosition().copy().add(0, 2.5f * 0.5f, 0));
		ray.setDirection(player.getViewDirection());

		selection.update(ray.getHit());
		applySelectionActions(ray, input);

		/** Debug system **/
		debug.setParticleSpawn(input.getKeyDown(input.KEY_P));
		debug.setParticleRemove(input.getKey(input.KEY_O));
	}
	
	private void applySelectionActions(ECRaycast ray, Input input)
	{
		if (ray.getHit() != null)
		{
			if (ray.getHit().getBlock() != 0)
			{
				Vec3i blockPosition = ray.getHit().getBlockPosition();
				Vec3  hitPoint = ray.getExactHitPoint();
				if (input.getMouse().getButtonDown(0))
				{
					removeBlock(blockPosition);
				}
				else if (input.getMouse().getButtonDown(1))
				{
					placeBlock(hitPoint);
				}
			}
		}
	}
	
	private void removeBlock(Vec3i block)
	{
//		core.getGame().getWorld().removeBlock(block.x, block.y, block.z);
//		core.getGame().getWorld().updateRequest(block.x, block.y, block.z);
		net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 0, block.x, block.y, block.z, 0));
	}
	
	private void placeBlock(Vec3 point)
	{
		int x = (int) point.x;
		int y = (int) point.y;
		int z = (int) point.z;
		float rx = point.x;
		float ry = point.y;
		float rz = point.z;
		
		float vx = rx - x;
		float vy = ry - y;
		float vz = rz - z;

		Vec3 check = new Vec3(vx, vy, vz).checkNormals();

		int xp = (int) check.x;
		int yp = (int) check.y;
		int zp = (int) check.z;

//		core.getGame().getWorld().addBlock(x + xp, y + yp, z + zp, 0x555555);
//		core.getGame().getWorld().updateRequest(x + xp, y + yp, z + zp);
		net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 1, x + xp, y + yp, z + zp, 0x555555));
	}
	
	public PlayerSelection getSelection()
	{
		return selection;
	}

	public ClientPlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(ClientPlayer player)
	{
		this.player = player;
	}

	public boolean isShowEnvSphere()
	{
		return showEnvSphere;
	}

	public Vec3 getEnvSpherePos()
	{
		return envSpherePos;
	}
}
