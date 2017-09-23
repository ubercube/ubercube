package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

public class PlayerSkeleton extends Bone
{
	public PlayerSkeleton()
	{
		super(new Transform(new Vec3(0, 0, 0), new Vec3(0.1f, 1.0f, 0.1f)), null);
		super.addChild(new HeadBone(this));
		super.addChild(new LeftArmBone(this));
		super.addChild(new RightArmBone(this));
		super.addChild(new LeftUpLegBone(this));
		super.addChild(new RightUpLegBone(this));
	}
}