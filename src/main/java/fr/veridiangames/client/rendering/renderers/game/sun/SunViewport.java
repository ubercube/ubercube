package fr.veridiangames.client.rendering.renderers.game.sun;

import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4;

import static fr.veridiangames.core.maths.Mathf.max;
import static fr.veridiangames.core.maths.Mathf.min;

public class SunViewport
{
	private Vec3	direction;
	private Mat4[]	lightMatrix;
	private Mat4	rotation;

	private float[]	shadowSascadeDistances;

	public SunViewport(GameCore core)
	{
		this.direction = core.getGame().getWorld().getSunDirection();
		this.shadowSascadeDistances = new float[]{0.1f, 10.0f, 30.0f, 60.0f};
		this.lightMatrix = new Mat4[shadowSascadeDistances.length - 1];
		for (int i = 0; i < shadowSascadeDistances.length - 1; i++)
			this.lightMatrix[i] = Mat4.identity();
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

		rotation = Mat4.rotate(direction, new Vec3(0, 1, 0));

		float aspect = Display.getInstance().getAspect();
		float tanHalfHFOV = Mathf.tan(Mathf.toRadians(playerCam.getFov() / 2.0f));
		float tanHalfVFOV = Mathf.tan(Mathf.toRadians((playerCam.getFov() * aspect) / 2.0f));

		for (int i = 0; i < shadowSascadeDistances.length - 1; i++)
		{
			float shadowNear = shadowSascadeDistances[i];
			float shadowFar = shadowSascadeDistances[i + 1];

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

			float minX = Float.MAX_VALUE;
			float maxX = Float.MIN_VALUE;
			float minY = Float.MAX_VALUE;
			float maxY = Float.MIN_VALUE;
			float minZ = Float.MAX_VALUE;
			float maxZ = Float.MIN_VALUE;

			Vec4 frustumCornersL[] = new Vec4[8];

			for (int j = 0; j < 8; j++) {
				Vec4 vW = frustumCorners[j].mul(camInversedViewMatrix);

				frustumCornersL[j] = vW.mul(rotation);

				minX = min(minX, frustumCornersL[j].x);
				maxX = max(maxX, frustumCornersL[j].x);
				minY = min(minY, frustumCornersL[j].y);
				maxY = max(maxY, frustumCornersL[j].y);
				minZ = min(minZ, -frustumCornersL[j].z);
				maxZ = max(maxZ, -frustumCornersL[j].z);
			}

			Mat4 projection = Mat4.orthographic(
				maxX,
				minX,
				maxY,
				minY,
				maxZ + 20,
				minZ - 20);

			lightMatrix[i] = projection.mul(rotation);
		}
	}

	public Mat4 getRotation() {
		return rotation;
	}

	public Mat4[] getLightMatrix() {
		return lightMatrix;
	}

	public float[] getShadowSascadeDistances() {
		return shadowSascadeDistances;
	}

	public int getCascadesCount()
	{
		return shadowSascadeDistances.length - 1;
	}
}
