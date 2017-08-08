/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.rendering;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.client.rendering.renderers.cullings.DistanceCulling;
import fr.veridiangames.client.rendering.renderers.cullings.FrustumCulling;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public class Camera
{
	private DistanceCulling	distanceCulling;
	private FrustumCulling	frustumCulling;
	private Transform		transform;
	private Display			display;
	
	private float width, height;
	private float fov, aspect, near, far;
	
	public Camera(Display display)
	{
		this(new Vec3(), display);
	}

	public Camera(Vec3 position, Display display)
	{
		this(position, new Quat(), display);
	}

	public Camera(Vec3 position, Quat rotation, Display display)
	{
		this(new Transform(position, rotation), display);
	}

	public Camera(Transform transform, Display display)
	{
		this.transform = transform;
		this.display = display;
		this.distanceCulling = new DistanceCulling();
		this.frustumCulling = new FrustumCulling();
		
		this.fov = 70.0f;
		this.aspect = (float) display.getWidth() / (float) display.getHeight();
		this.near = 0.1f;
		this.far = 1000.0f;

		this.frustumCulling.init(this);
	}
	
	public Camera(float fov, float width, float height, float near, float far)
	{
		this.transform = new Transform();
		this.fov = fov;
		this.aspect = width / height;
		this.near = near;
		this.far = far;
	}
	
	public Mat4 getProjection()
	{
		Mat4 translationMatrix = Mat4.translate(-transform.getPosition().x, -transform.getPosition().y, -transform.getPosition().z);
		Mat4 rotationMatrix = Mat4.rotate(transform.getRotation().getForward(), transform.getRotation().getUp());
		Mat4 projectionMatrix = Mat4.perspective(fov, aspect, near, far);

		return projectionMatrix.mul(rotationMatrix.mul(translationMatrix));
	}
	
	public void update(float distance)
	{
		aspect = display.getAspect();
		distanceCulling.update(transform.getPosition(), distance);
		frustumCulling.update(this);
	}
	
	public boolean isInViewDistance(Vec3 pos, float radius)
	{
		return distanceCulling.isInViewDistance(pos, radius);
	}
	
	public boolean isInViewFrustum(Vec3 pos, float radius)
	{
		return frustumCulling.isInViewFrustum(pos, radius);
	}
	
	public Transform getTransform()
	{
		return transform;
	}

	public void setTransform(Transform transform)
	{
		this.transform = transform;
	}

	public Display getDisplay()
	{
		return display;
	}

	public void setDisplay(Display display)
	{
		this.display = display;
	}

	public float getFov()
	{
		return fov;
	}

	public void setFov(float fov)
	{
		this.fov = fov;
	}

	public float getAspect()
	{
		return aspect;
	}

	public void setAspect(float aspect)
	{
		this.aspect = aspect;
	}

	public float getNear()
	{
		return near;
	}

	public void setNear(float near)
	{
		this.near = near;
	}

	public float getFar()
	{
		return far;
	}

	public void setFar(float far)
	{
		this.far = far;
	}
}
