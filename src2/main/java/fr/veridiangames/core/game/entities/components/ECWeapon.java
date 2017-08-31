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

package fr.veridiangames.core.game.entities.components;

import java.util.concurrent.ConcurrentHashMap;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.WeaponAK47;
import fr.veridiangames.core.game.entities.weapons.meleeWeapon.WeaponShovel;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.profiler.Profiler;

/**
 * Created by Marccspro on 7 f�vr. 2016.
 */
public class ECWeapon extends EComponent
{
	private int weaponID;
	private Weapon weapon;
	private ConcurrentHashMap<Integer, Weapon> weapons;
	private Profiler profiler;

	public ECWeapon(int weapon)
	{
		super(WEAPON);
		super.addDependencies(RENDER);
		this.weaponID = weapon;

		this.weapons = new ConcurrentHashMap<>();
		this.weapons.put(Weapon.AK47, new WeaponAK47());
		this.weapons.put(Weapon.SHOVEL, new WeaponShovel());
		this.weapons.put(Weapon.GRENADE, new WeaponGrenade(10));

		this.profiler = new Profiler("WEAPON", true);
	}

	@Override
	public void init(GameCore core)
	{
		this.weapon = this.weapons.get(this.weaponID);
		this.weapon.setHolder((Player) this.parent);
		this.weapon.onChange();
		Transform parentTransform = ((ECRender) this.parent.get(RENDER)).getEyeTransform();
		this.weapon.getTransform().setParent(parentTransform);
	}

	@Override
	public void update(GameCore core)
	{
		this.profiler.start();
		this.weapon.update(core);
		this.profiler.end();
	}

	public int getWeaponID()
	{
		return this.weaponID;
	}

	public Weapon getWeapon()
	{
		return this.weapon;
	}

	public void setWeapon(int weapon)
	{
		this.weaponID = weapon;
		this.weapon.onChange();
		this.weapon = this.weapons.get(weapon);
		this.weapon.setHolder((Player) this.parent);
		this.weapon.init();
		Transform parentTransform = ((ECRender) this.parent.get(RENDER)).getEyeTransform();
		this.weapon.getTransform().setParent(parentTransform);
	}

	public ConcurrentHashMap<Integer, Weapon> getWeapons()
	{
		return this.weapons;
	}
}
