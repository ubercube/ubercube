package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;

public class BuilderKit extends Kit
{
	public BuilderKit()
	{
		super("Builder");
		this.addWeapon(weaponManager.get(Weapon.SHOVEL));
	}

	@Override
	public void reset()
	{

	}
}
