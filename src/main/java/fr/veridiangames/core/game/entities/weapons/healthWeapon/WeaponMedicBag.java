package fr.veridiangames.core.game.entities.weapons.healthWeapon;

import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Jimi Vacarians on 21/09/2017.
 */
public class WeaponMedicBag extends Weapon {

	public WeaponMedicBag() {
		super(Weapon.MEDICBAG, Model.MEDICBAG);
		this.transform.setLocalScale(new Vec3(3, 3, 3));
		this.setIdlePosition(new Transform(new Vec3(0.09f, -0.4f, 0.85f), new Quat(new Vec3(0,1,0), 38)));
		this.setHidePosition(new Transform(new Vec3(0f, 0f, 0)));
		this.setZoomPosition(new Transform(new Vec3(0, 0, 0f)));
	}

	void heal(){

	}

	@Override
	public void onAction() {

	}

	@Override
	public void onActionUp() {

	}

	@Override
	public void onActionDown() {

	}
}
