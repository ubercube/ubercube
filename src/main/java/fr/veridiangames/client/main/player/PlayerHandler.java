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
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBlood;
import fr.veridiangames.core.game.entities.particles.ParticlesBulletHit;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.healthWeapon.WeaponMedicBag;
import fr.veridiangames.core.game.entities.weapons.meleeWeapon.WeaponShovel;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec2i;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.*;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;

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
		ECWeapon weapon = player.getWeaponComponent();
		ECDebug debug = player.getDebugComponent();

		AudioListener.setTransform(player.getTransform());

		mouse.setDX(0);
		mouse.setDY(0);

		Ubercube.getInstance().getMinimapHandler().setSize(new Vec2i(Display.getInstance().getWidth() - 20, Display.getInstance().getHeight() - 20));

		if (player.isDead())
		{
			player.setRotation(new Quat());
			player.setPosition(new Vec3((core.getGame().getWorld().getWorldSize() * Chunk.SIZE) / 2, -10000, (core.getGame().getWorld().getWorldSize() * Chunk.SIZE) / 2));
			return;
		}

		Ubercube.getInstance().getMinimapHandler().setSize(new Vec2i(300, 200));

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

		mouse.useZoomSpeed(weapon.getWeapon().isZoomed(), weapon.getWeapon().getMouseSpeedOnZoom());

//		if (input.getKeyDown(Input.KEY_F))
//			key.setFly(!key.isFly());

		mouse.setDX(input.getMouse().getDX());
		mouse.setDY(input.getMouse().getDY());

		ray.setPosition(player.getEyePosition().copy());
		ray.setDirection(player.getViewDirection());

		/** Weapon switch **/

		if(input.getMouse().getDWheel() > 0)
		{
			if(weapon.getWeaponIndex() >= weapon.getWeapons().size() - 1)
				weapon.setWeapon(0);
			else
				weapon.setWeapon(weapon.getWeaponIndex() + 1);
			weapon.getWeapon().setNet(this.net);
			this.net.send(new WeaponChangePacket(player), Protocol.UDP);
		}
		if(input.getMouse().getDWheel() < 0)
		{
			if(weapon.getWeaponIndex() <= 0)
				weapon.setWeapon(weapon.getWeapons().size() - 1);
			else
				weapon.setWeapon(weapon.getWeaponIndex() - 1);
			weapon.getWeapon().setNet(this.net);
			this.net.send(new WeaponChangePacket(player), Protocol.UDP);
		}

		selection.setShow(false);
		if(weapon.getWeapon() instanceof WeaponShovel)
			applySelectionActions(ray, input, (WeaponShovel)weapon.getWeapon());

		if (ray.getHit() != null)
		{
			if((ray.getHit().getEntity() != null) && (player.getWeaponComponent().getWeapon() instanceof WeaponMedicBag)) {
				if ((ray.getHit().getEntity() instanceof NetworkedPlayer)) {
					if (input.getMouse().getButtonDown(0)) {
						net.send(new ApplyHealingPacket((NetworkedPlayer) ray.getHit().getEntity(), 10), Protocol.TCP);
					}
				}
			}
		}
	}
	
	private void applySelectionActions(ECRaycast ray, Input input, WeaponShovel weapon)
	{
		if (ray.getHit() != null)
		{
			if ((ray.getHit().getBlock() != 0) && (player.getWeaponComponent().getWeapon() instanceof WeaponShovel))
			{
				selection.setShow(true);
				selection.update(ray.getHit());
				Vec3i blockPosition = ray.getHit().getBlockPosition();
				Vec3  hitPoint = ray.getExactHitPoint();
				if (input.getMouse().getButtonDown(0)) {
					net.send(new ShovelHitBlockPacket( blockPosition, 0.50f, ray.getHit().getBlock()), Protocol.TCP);
					ParticleSystem hitParticles = new ParticlesBulletHit(Indexer.getUniqueID(), ray.getExactHitPoint(), new Color4f(ray.getHit().getBlock()));
					hitParticles.useCollision(false);
					GameCore.getInstance().getGame().spawn(hitParticles);
				}
				else if (input.getMouse().getButtonDown(1))
				{
					placeBlock(hitPoint);
				}
			}
			else if (ray.getHit().getEntity() != null && ray.getDistance() < 2)
			{
				Entity e = ray.getHit().getEntity();
				if (e instanceof NetworkedPlayer && input.getMouse().getButtonDown(0))
				{
					Player p = (Player) e;
					if (p.getID() == player.getID())
						return;
					ParticleSystem blood = new ParticlesBlood(Indexer.getUniqueID(), ray.getExactHitPoint().copy());
					blood.setParticleVelocity(player.getRotation().getBack().copy().mul(0.02f));
					blood.setNetwork(net);
					int damage = weapon.getDamage();
					float height = ray.getExactHitPoint().y;
					this.net.send(new BulletHitPlayerPacket(p, player.getID(), height, damage), Protocol.TCP);
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
