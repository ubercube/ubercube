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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.kits.AssaultKit;
import fr.veridiangames.core.game.entities.weapons.kits.Kit;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.profiler.Profiler;

import java.util.List;

/**
 * Created by Marccspro on 7 f�vr. 2016.
 */
public class ECWeapon extends EComponent
{
	private int weaponIndex;
	private Weapon weapon;
	//private List<Weapon> weapons;
	private Profiler profiler;

	private Kit kit;

	public ECWeapon(int weapon)
	{
		super(WEAPON);
		super.addDependencies(RENDER);
		this.weaponIndex = weapon;

		this.kit = new AssaultKit();

		/*this.weapons = new ConcurrentHashMap<>();
		this.weapons.put(Weapon.AK47, new WeaponAK47());
		this.weapons.put(Weapon.SHOVEL, new WeaponShovel());
		this.weapons.put(Weapon.GRENADE, new WeaponGrenade(10));*/

		this.profiler = new Profiler("WEAPON", true);
	}
	
	public void init(GameCore core)
	{
		this.weaponIndex = 0;
		this.weapon = this.getWeapons().get(0);
		this.weapon.setHolder((Player) parent);
		this.weapon.onChange();
		Transform parentTransform = ((ECRender) this.parent.get(RENDER)).getEyeTransform();
		this.weapon.getTransform().setParent(parentTransform);
	}
	
	public void update(GameCore core)
	{
		profiler.start();
		weapon.update(core);
		profiler.end();
	}
	
	public int getWeaponIndex()
	{
		return weaponIndex;
	}
	
	public Weapon getWeapon()
	{
		return weapon;
	}

	public void setWeapon(int weaponIndex)
	{
		this.weaponIndex = weaponIndex;
		this.weapon.onChange();
		this.weapon = this.getWeapons().get(weaponIndex);
		this.weapon.setHolder((Player) parent);
		this.weapon.init();
		Transform parentTransform = ((ECRender) this.parent.get(RENDER)).getEyeTransform();
		this.weapon.getTransform().setParent(parentTransform);
	}

	public Kit getKit()
	{
		return kit;
	}

	public void setKit(Kit kit)
	{
		this.kit = kit;
		this.init(GameCore.getInstance());
	}

	public List<Weapon> getWeapons()
	{
		return this.kit.getWeapons();
	}
}
