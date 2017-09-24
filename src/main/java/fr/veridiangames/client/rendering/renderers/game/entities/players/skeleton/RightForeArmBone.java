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

public class RightForeArmBone extends Bone
{
	private float rotVelocity = 0;
	private float time = 0;

	public RightForeArmBone(Bone parent) {
		super(new Transform(new Vec3(0, 0.5f, 0), FORE_ARM_SIZE), parent);
		super.setColor(FORE_ARM_COLOR);
	}

	public void update(NetworkedPlayer p)
	{
		time++;
		float pvel = p.getMovementVelocity();

		if (p.getWeaponComponent().getWeapon() instanceof FireWeapon)
			rotVelocity = (Mathf.sin(time * 0.1f) * 10) * pvel - 140;
		else
			rotVelocity = (Mathf.sin(time * 0.1f) * 10) * pvel - 90;

		this.transform.setLocalRotation(Quat.deuler(rotVelocity, 0, 0));
		Transform rightHandPosition = new Transform(new Vec3(0, 0.5f, 0));
		rightHandPosition.setParent(this.getTransform());
		p.setRightHandPosition(rightHandPosition.getPosition());
	}
}
