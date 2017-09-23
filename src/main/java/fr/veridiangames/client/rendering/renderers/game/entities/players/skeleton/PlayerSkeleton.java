package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class PlayerSkeleton extends Bone
{
	public static final Vec3 HEAD_SIZE = new Vec3(0.5f, 0.5f, 0.5f);
	public static final Color4f HEAD_COLOR = new Color4f(208, 164, 134, 255);

	public static final Vec3 BODY_SIZE = new Vec3(0.65f, 1.10f, 0.3f);
	public static final Color4f BODY_COLOR = new Color4f(6, 46, 6, 255);

	public static final Vec3 ARM_SIZE = new Vec3(0.25f, 0.5f, 0.25f);
	public static final Color4f ARM_COLOR = new Color4f(3, 34, 3, 255);

	public static final Vec3 FORE_ARM_SIZE = new Vec3(0.2f, 0.5f, 0.2f);
	public static final Color4f FORE_ARM_COLOR = new Color4f(208, 164, 134, 255);

	public static final Vec3 UP_LEG_SIZE = new Vec3(0.3f, 0.65f, 0.3f);
	public static final Color4f UP_LEG_COLOR = new Color4f(3, 34, 3, 255);

	public static final Vec3 LEG_SIZE = new Vec3(0.3f, 0.65f, 0.3f);
	public static final Color4f LEG_COLOR = new Color4f(3, 34, 3, 255);

	public PlayerSkeleton()
	{
		super(new Transform(new Vec3(0, -0.15f, 0), BODY_SIZE), null);
		this.setColor(BODY_COLOR);

		super.addChild(new HeadBone(this));
		super.addChild(new LeftArmBone(this));
		super.addChild(new RightArmBone(this));
		super.addChild(new LeftUpLegBone(this));
		super.addChild(new RightUpLegBone(this));
	}

	protected void update(NetworkedPlayer p)
	{}
}