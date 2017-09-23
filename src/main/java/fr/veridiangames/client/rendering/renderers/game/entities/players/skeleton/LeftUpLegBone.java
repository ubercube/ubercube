package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class LeftUpLegBone extends Bone
{
	public LeftUpLegBone(Bone parent) {
		super(new Transform(new Vec3(-0.25f, 0, 0), Quat.deuler(180, 0, 0), new Vec3(0.1f, 0.5f, 0.1f)), parent);
		super.setColor(Color4f.GRAY);

		super.addChild(new LeftLegBone(this));
	}
}
