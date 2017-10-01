package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.FireWeapon;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.ARM_SIZE;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.FORE_ARM_COLOR;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.FORE_ARM_SIZE;

public class LeftForeArmBone extends Bone
{
	private Vec3 rotVelocity = new Vec3();
	private float time = 0;

	public LeftForeArmBone(Bone parent) {
		super(new Transform(new Vec3(0, 0.5f, 0), FORE_ARM_SIZE), parent);
		super.setColor(FORE_ARM_COLOR);
	}

	public void update(NetworkedPlayer p)
	{
		time++;
		float pvel = p.getMovementVelocity();

		if (p.getWeaponComponent().getWeapon() instanceof FireWeapon)
			rotVelocity = new Vec3((-Mathf.sin(time * 0.1f) * 10) * pvel - 60, 0, 0);
		else
			rotVelocity = new Vec3((-Mathf.sin(time * 0.1f) * 10) * pvel - 10, 0, 0);
		Vec3 zoomRot = new Vec3();
		if (p.getWeaponComponent().getWeapon().isZoomed())
			zoomRot.set(10, 0, 70);
		this.transform.setLocalRotation(Quat.deuler(rotVelocity.x - zoomRot.x, rotVelocity.y - zoomRot.y, rotVelocity.z - zoomRot.z));

		Transform leftHandPosition = new Transform(new Vec3(0, 0.5f, 0));
		leftHandPosition.setParent(this.getTransform());
		p.setLeftHandPosition(leftHandPosition.getPosition());
	}
}
