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

import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

public class WeaponAK47 extends FireWeapon
{
	public WeaponAK47()
	{
		super(Weapon.AK47, Model.AK47);
		this.transform.setLocalScale(new Vec3(-1, 1, 1));
		this.setIdlePosition(new Transform(new Vec3(0.50f, -0.1f, 2f)));
		this.setHidePosition(new Transform(new Vec3(0.3f, -0.05f - 1f, 0)));
		this.setZoomPosition(new Transform(new Vec3(0, 0, -0.1f)));
		this.setShootPoint(new Transform(new Vec3(0, -0.3f, -1.5f)));
		this.setAudioGain(0.5f);
		this.setPosition(0);
	}
}