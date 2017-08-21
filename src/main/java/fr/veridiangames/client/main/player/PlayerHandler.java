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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.audio.AudioListener;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.meleeWeapon.WeaponShovel;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.BlockActionPacket;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.core.network.packets.WeaponChangePacket;

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

		AudioListener.setTransform(player.getTransform());

		mouse.setDX(0);
		mouse.setDY(0);

		if (Ubercube.getInstance().isInConsole() || Ubercube.getInstance().isInMenu())
			return;

		if (input.getMouse().getButton(0) && !Ubercube.getInstance().isInMenu())
			input.getMouse().setGrabbed(true);
		if (input.getKey(Input.KEY_ESCAPE) && !Ubercube.getInstance().isInConsole())
			input.getMouse().setGrabbed(false);
		if (!input.getMouse().isGrabbed())
			return;

		key.setUp(input.getKey(Input.KEY_Z));
		key.setDown(input.getKey(Input.KEY_S));
		key.setLeft(input.getKey(Input.KEY_Q));
		key.setRight(input.getKey(Input.KEY_D));
		key.setJump(input.getKey(Input.KEY_SPACE));
		key.setRun(input.getKey(Input.KEY_LEFT_SHIFT));
		key.setCrouche(input.getKey(Input.KEY_LEFT_CONTROL));
		key.setCtrl(input.getKey(Input.KEY_LEFT_CONTROL));

		if (input.getMouse().getButton(0))
			weapon.getWeapon().onAction();
		if (input.getMouse().getButtonDown(0))
			weapon.getWeapon().onActionDown();
		if (input.getMouse().getButtonUp(0))
			weapon.getWeapon().onActionUp();

		if (input.getMouse().getButton(1))
			weapon.getWeapon().setPosition(1);
		else
			weapon.getWeapon().setPosition(0);

		if (input.getKeyDown(Input.KEY_F))
			key.setFly(!key.isFly());

		mouse.setDX(input.getMouse().getDX());
		mouse.setDY(input.getMouse().getDY());

		ray.setPosition(player.getEyePosition().copy());
		ray.setDirection(player.getViewDirection());

		/** Weapon switch **/

		if(input.getMouse().getDWheel() > 0)
		{
			if(weapon.getWeaponID() >= weapon.getWeapons().size() - 1)
			{
				weapon.setWeapon(0);
			}
			else
			{
				weapon.setWeapon(weapon.getWeaponID() + 1);
			}
			weapon.getWeapon().setNet(this.net);
			this.net.send(new WeaponChangePacket(player), Protocol.UDP);
		}
		if(input.getMouse().getDWheel() < 0)
		{
			if(weapon.getWeaponID() <= 0)
			{
				weapon.setWeapon(weapon.getWeapons().size() - 1);
			}
			else
			{
				weapon.setWeapon(weapon.getWeaponID() - 1);
			}
			weapon.getWeapon().setNet(this.net);
			this.net.send(new WeaponChangePacket(player), Protocol.UDP);
		}

		selection.setShow(false);
		if(weapon.getWeapon() instanceof WeaponShovel)
		{
			selection.setShow(true);
			selection.update(ray.getHit());
			applySelectionActions(ray, input);
		}
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
		net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 0, block.x, block.y, block.z, 0), Protocol.TCP);
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

		net.send(new BlockActionPacket(core.getGame().getPlayer().getID(), 1, x + xp, y + yp, z + zp, 0x7f555555), Protocol.TCP);
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
