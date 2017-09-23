package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.HEAD_COLOR;
import static fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton.HEAD_SIZE;

public class HeadBone extends Bone
{
	public HeadBone(Bone parent) {
		super(new Transform(new Vec3(0, 1.15f, 0), HEAD_SIZE), parent);
		super.setColor(HEAD_COLOR);
	}

	public void update(NetworkedPlayer p)
	{

	}
}
