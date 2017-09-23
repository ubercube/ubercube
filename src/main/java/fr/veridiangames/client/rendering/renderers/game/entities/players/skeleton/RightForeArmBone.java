package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class RightForeArmBone extends Bone
{
	public RightForeArmBone(Bone parent) {
		super(new Transform(new Vec3(0, 0.5f, 0), new Vec3(0.1f, 0.5f, 0.1f)), parent);
		super.setColor(Color4f.GRAY);
	}
}
