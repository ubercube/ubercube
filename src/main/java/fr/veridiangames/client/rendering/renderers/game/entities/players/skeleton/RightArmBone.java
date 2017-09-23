package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class RightArmBone extends Bone
{
	public RightArmBone(Bone parent) {
		super(new Transform(new Vec3(0.35f, 0.85f, 0), Quat.deuler(180, 0, 0), new Vec3(0.1f, 0.5f, 0.1f)), parent);
		super.setColor(Color4f.GRAY);

		super.addChild(new RightForeArmBone(this));
	}
}
