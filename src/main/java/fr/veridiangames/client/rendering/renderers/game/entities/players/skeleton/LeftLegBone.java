package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.LEG_COLOR;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.LEG_SIZE;

public class LeftLegBone extends Bone
{
	private float rotVelocity = 0;
	private float time = 0;

	public LeftLegBone(Bone parent)
	{
		super(new Transform(new Vec3(0, 0.65f, 0), LEG_SIZE), parent);
		super.setColor(LEG_COLOR);
	}

	protected void update(NetworkedPlayer p)
	{
		time++;
		float pvel = p.getMovementVelocity();
		float speed = (pvel > 0.7f ? 1 : 0.65f);
		rotVelocity = (-Mathf.cos(time * 0.15f * speed) * 40 + 40) * pvel;
		this.transform.setLocalRotation(Quat.deuler(rotVelocity, 0, 0));
	}
}
