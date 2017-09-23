package fr.veridiangames.core.game.entities.weapons.kits;

import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.WeaponManager;

/**
 * Created by Jimi Vacarians on 21/09/2017.
 */
public class MedicKit extends Kit {
	public MedicKit(WeaponManager weaponManager) {
		super("Medic", weaponManager);
		this.addWeapon(Weapon.AK47);
		this.addWeapon(Weapon.MEDICBAG);
	}

	@Override
	public void reset() {

	}
}
