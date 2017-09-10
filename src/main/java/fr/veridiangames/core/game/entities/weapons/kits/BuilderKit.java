package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;

public class BuilderKit extends Kit
{
	public BuilderKit(WeaponManager weaponManager)
	{
		super("Builder", weaponManager);
		this.addWeapon(Weapon.SHOVEL);
	}

	@Override
	public void reset()
	{

	}
}
