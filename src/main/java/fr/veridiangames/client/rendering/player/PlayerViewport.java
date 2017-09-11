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

package fr.veridiangames.client.rendering.player;

import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.main.player.PlayerHandler;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.Display;

/**
 * Created by Marccspro on 9 f�vr. 2016.
 */
public class PlayerViewport
{
	private Camera			camera;
	private GameData		gameData;
	private PlayerHandler	player;

	public PlayerViewport(Display display, Ubercube main)
	{
		this.gameData = main.getGameCore().getGame().getData();
		this.player = main.getPlayerHandler();
		this.camera = new Camera(player.getPlayer().getPosition().add(0, 2.5f * 0.5f, 0), player.getPlayer().getRotation(), display);
	}

	public void update()
	{
		camera.setFov(camera.getFov() - player.getPlayer().getWeaponComponent().getWeapon().getCurrentZoom());
		camera.getTransform().setLocalPosition(player.getPlayer().getPosition().copy().add(0, 2.5f * 0.5f, 0));
		camera.getTransform().setLocalRotation(player.getPlayer().getRotation());
		camera.update(gameData.getViewDistance());
	}

	public Camera getCamera()
	{
		return camera;
	}
	
	public PlayerHandler getPlayerHandler()
	{
		return player;
	}
	
	public Vec3 getEyePosition()
	{
		return camera.getTransform().getPosition();
	}
}
