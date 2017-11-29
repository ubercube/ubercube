package fr.veridiangames.client.rendering.renderers.game.sun;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;

public class SunViewport
{
	private Vec3	direction;
	private Mat4	projection;
	private Mat4	rotation;
	private Mat4	position;
	private Mat4	lightMatrix;

	public SunViewport(GameCore core)
	{
		direction = core.getGame().getWorld().getSunDirection();
		projection = Mat4.orthographic(100, -100, 100, -100, 100, -100);
		rotation = Mat4.rotate(-45, 0, 0);
		position = Mat4.translate(0, -100, 0);
		lightMatrix = projection.mul(position.mul(rotation));
	}

	public void update(Player player)
	{
		// Shadow projection at the position of the player, good enougth for now but will need to only be at it's viewport
		projection = Mat4.orthographic(80, -80, 80, -80, 80, -80);
		rotation = Mat4.rotate(-45, 45, 0);
		position = Mat4.translate(-player.getPosition().x, -player.getPosition().z * 0.707f, -player.getPosition().z * 0.707f);
		lightMatrix = projection.mul(position.mul(rotation));
	}

	public Vec3 getDirection() {
		return direction;
	}

	public Mat4 getProjection() {
		return projection;
	}

	public Mat4 getRotation() {
		return rotation;
	}

	public Mat4 getLightMatrix() {
		return lightMatrix;
	}
}
