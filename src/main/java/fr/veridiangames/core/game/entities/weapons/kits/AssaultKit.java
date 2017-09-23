package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;

/**
 * Created by Tybau on 23/09/2017.
 */
public class AssaultKit extends Kit
{
	public AssaultKit(WeaponManager weaponManager)
	{
		super("Assault", weaponManager);
		this.addWeapon(Weapon.AK47);
		this.addWeapon(Weapon.GRENADE);
		this.addWeapon(Weapon.SHOVEL);
	}

	@Override
	public void reset()
	{
		((WeaponGrenade) weaponManager.get(Weapon.GRENADE)).resetGrenades();
	}
}
