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

package fr.veridiangames.core.game.entities.weapons.fireWeapons;

import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.loaders.TextureID;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

public class WeaponAWP extends FireWeapon
{
	public WeaponAWP()
	{
		super(Weapon.AWP, Model.AWP);
		this.transform.setLocalScale(new Vec3(0.65f, 0.65f, 0.65f));
		this.setIdlePosition(new Transform(new Vec3(0.2f, 0f, -0.3f)));
		this.setHidePosition(new Transform(new Vec3(0.2f, 0f - 1f, 1.5f)));
		this.setZoomPosition(new Transform(new Vec3(0, 0, 0.3f)));
		this.setShootPoint(new Transform(new Vec3(0, -0.16f, 1.7f)));
		this.setAudioGain(0.5f);
		this.setZoomAmnt(150);
		this.setShootForce(10.0f);
		this.setShootPecisionIdle(0.025f);
		this.setShootPecisionZoomed(0.001f);
		this.setCrosshairTexture(TextureID.AWP_CROSSHAIR);
		this.setPosition(0);
		this.setFireFrequency(1);
		this.setMaxBullets(5);
		this.setRecoil(0.3f);
		this.setRecoilOnZoom(0.1f);
		this.setDamage(70);
		this.setMouseSpeedOnZoom(0.3f);
		this.setSound(Sound.AWP_SHOOT);

		this.setCenterPosition(new Transform(new Vec3(0, 5, -30)));
		this.setPreviewScale(new Vec3(0.1f, -0.1f, 0.1f));
		this.setPreviewDistance(30);
	}
}