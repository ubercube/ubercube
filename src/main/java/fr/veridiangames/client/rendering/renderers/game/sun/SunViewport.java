package fr.veridiangames.client.rendering.renderers.game.sun;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4;

import static fr.veridiangames.core.maths.Mathf.max;
import static fr.veridiangames.core.maths.Mathf.min;

public class SunViewport
{
	private Vec3	direction;
	private Mat4	projection;
	private Mat4	rotation;
	private Mat4	position;
	private Mat4	lightMatrix;

	private float	shadowNear;
	private float	shadowFar;

	public SunViewport(GameCore core)
	{
		direction = core.getGame().getWorld().getSunDirection();
		projection = Mat4.orthographic(10, -10, 10, -10, 100, -100);
		rotation = Mat4.rotate(-45, 0, 0);
		position = Mat4.translate(0, -100, 0);
		lightMatrix = projection.mul(position.mul(rotation));
	}

	public void update(PlayerViewport playerViewport)
	{
		calcLightProjection(playerViewport);
	}

	public void calcLightProjection(PlayerViewport playerViewport)
	{
		Camera playerCam = playerViewport.getCamera();

		Mat4 camViewMatrix = playerCam.getViewMatrix();
		Mat4 camInversedViewMatrix = Mat4.invert(camViewMatrix, new Mat4());

		rotation = Mat4.rotate(new Vec3(1, -1.5f, 0.5f).normalize(), new Vec3(0, 1, 0));

		float aspect = Display.getInstance().getAspect();
		float tanHalfHFOV = Mathf.tan(Mathf.toRadians(playerCam.getFov() / 2.0f));
		float tanHalfVFOV = Mathf.tan(Mathf.toRadians((playerCam.getFov() * aspect) / 2.0f));

		shadowNear = 0.1f;
		shadowFar = 55.0f;

		float xn = shadowNear * tanHalfHFOV;
		float xf = shadowFar * tanHalfHFOV;
		float yn = shadowNear * tanHalfVFOV;
		float yf = shadowFar * tanHalfVFOV;

		Vec4 frustumCorners[] = {
			new Vec4(xn, yn, shadowNear, 1.0f),
			new Vec4(-xn, yn, shadowNear, 1.0f),
			new Vec4(xn, -yn, shadowNear, 1.0f),
			new Vec4(-xn, -yn, shadowNear, 1.0f),

			new Vec4(xf, yf, shadowFar, 1.0f),
			new Vec4(-xf, yf, shadowFar, 1.0f),
			new Vec4(xf, -yf, shadowFar, 1.0f),
			new Vec4(-xf, -yf, shadowFar, 1.0f)
		};

		float minX = 90000;
		float maxX = -90000;
		float minY = 90000;
		float maxY = -90000;
		float minZ = 90000;
		float maxZ = -90000;

		Vec4 frustumCornersL[] = new Vec4[8];

		for (int i = 0; i < 8; i++)
		{
			Vec4 vW = frustumCorners[i].mul(camInversedViewMatrix);

			frustumCornersL[i] = vW.mul(rotation);

			minX = min(minX, frustumCornersL[i].x);
			maxX = max(maxX, frustumCornersL[i].x);
			minY = min(minY, frustumCornersL[i].y);
			maxY = max(maxY, frustumCornersL[i].y);
			minZ = min(minZ, -frustumCornersL[i].z);
			maxZ = max(maxZ, -frustumCornersL[i].z);
		}

		projection = Mat4.orthographic(maxX + 5, minX - 5, maxY + 5, minY - 5, maxZ + 40, minZ - 40);
		lightMatrix = projection.mul(rotation);
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

	public float getShadowFar() {
		return shadowFar;
	}

	public float getShadowNear() {
		return shadowNear;
	}
}
