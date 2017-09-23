package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.LEG_SIZE;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.UP_LEG_COLOR;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.UP_LEG_SIZE;

public class RightUpLegBone extends Bone
{
	private float rotVelocity = 0;
	private float time = 0;

	public RightUpLegBone(Bone parent) {
		super(new Transform(new Vec3(0.2f, 0f, 0), Quat.deuler(180, 0, 0), UP_LEG_SIZE), parent);
		super.setColor(UP_LEG_COLOR);

		super.addChild(new RightLegBone(this));
	}

	protected void update(NetworkedPlayer p)
	{
		time++;
		float pvel = p.getMovementVelocity();
		float speed = (pvel > 0.7f ? 1 : 0.65f);
		rotVelocity = (-Mathf.sin(time * 0.15f * speed) * 40 - 10) * pvel;
		this.transform.setLocalRotation(Quat.deuler(rotVelocity + 180, 3, 0));
	}
}
