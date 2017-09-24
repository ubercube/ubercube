package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;

/**
 * Created by Jimi Vacarians on 21/09/2017.
 */
public class MedicKit extends Kit {
	public MedicKit(WeaponManager weaponManager)
	{
		super("Medic", weaponManager);
		this.addWeapon(Weapon.MEDICBAG);
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
