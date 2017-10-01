package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.FireWeapon;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.ARM_COLOR;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.ARM_SIZE;

public class LeftArmBone extends Bone
{
	private Vec3 rotVelocity = new Vec3();
	private float time = 0;

	public LeftArmBone(Bone parent) {
		super(new Transform(new Vec3(-0.35f, 1f, 0), Quat.deuler(180, 0, 0), ARM_SIZE), parent);
		super.setColor(ARM_COLOR);

		super.addChild(new LeftForeArmBone(this));
	}

	protected void update(NetworkedPlayer p)
	{
		time++;
		float pvel = p.getMovementVelocity();
		if (p.getWeaponComponent().getWeapon() instanceof FireWeapon)
			rotVelocity = new Vec3((Mathf.sin(time * 0.1f) * 10) * pvel - 20 + 180, 0, 20);
		else
			rotVelocity = new Vec3((Mathf.sin(time * 0.1f) * 10) * pvel + 5 + 180, -5, 0);
		float zoomed = p.getWeaponComponent().getWeapon().isZoomed() ? 1 : 0.4f;
		float view = (p.getXRotation() - 90) * zoomed;
		Vec3 zoomRot = new Vec3();
		if (p.getWeaponComponent().getWeapon().isZoomed())
			zoomRot.set(72.5f, 0, 30);
		this.transform.setLocalRotation(Quat.deuler(rotVelocity.x - view - zoomRot.x, rotVelocity.y - zoomRot.y, rotVelocity.z - zoomRot.z));
	}
}
