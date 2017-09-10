package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Kit
{
	protected List<Weapon> weapons;
	protected String name;
	protected WeaponManager weaponManager;

	public Kit(String name, WeaponManager weaponManager)
	{
		this.name = name;
		this.weaponManager = weaponManager;

		this.weapons = new ArrayList<>();
	}

	public void addWeapon(Weapon weapon)
	{
		this.weapons.add(weapon);
	}

	public abstract void reset();

	public String getName()
	{
		return name;
	}

	public List<Weapon> getWeapons()
	{
		return weapons;
	}

	public WeaponManager getWeaponManager()
	{
		return weaponManager;
	}
}
