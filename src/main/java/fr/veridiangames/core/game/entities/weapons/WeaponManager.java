package fr.veridiangames.core.game.entities.weapons;

import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.WeaponAK47;
import fr.veridiangames.core.game.entities.weapons.meleeWeapon.WeaponShovel;

import java.util.concurrent.ConcurrentHashMap;

public class WeaponManager
{
	private ConcurrentHashMap<Integer, Weapon> weapons;

	public static Weapon AK47 = new WeaponAK47();
	public static Weapon GRENADE = new WeaponGrenade(10); //TODO modify grenade count
	public static Weapon SHOVEL = new WeaponShovel();

	public WeaponManager()
	{
		this.weapons = new ConcurrentHashMap<>();

		this.weapons.put(Weapon.AK47, AK47);
		this.weapons.put(Weapon.GRENADE, GRENADE);
		this.weapons.put(Weapon.SHOVEL, SHOVEL);
	}

	public Weapon get(int id)
	{
		return weapons.get(id);
	}
}
