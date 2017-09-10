package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;

public class AssaultKit extends Kit
{
	public AssaultKit(WeaponManager weaponManager)
	{
		super("Assault", weaponManager);
		this.addWeapon(weaponManager.get(Weapon.AK47));
		this.addWeapon(weaponManager.get(Weapon.GRENADE));
	}

	@Override
	public void reset()
	{
		((WeaponGrenade) weaponManager.get(Weapon.GRENADE)).resetGrenades();
	}
}
