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

package fr.veridiangames.client.rendering.shaders;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public class WorldShader extends Shader
{
	public static final String VERTEX_PATH = "/world.vert";
	public static final String FRAGMENT_PATH = "/world.frag";

	private int projectionMatrixLocation;
	private int modelViewMatrixLocation;
	private int cameraPositionLocation;
	private int colorLocation;
	private int fogDistanceLocation;

	public WorldShader()
	{
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public WorldShader(String vertexPath, String fragmentPath)
	{
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void getUniformLocations()
	{
		this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		this.modelViewMatrixLocation = super.getUniformLocation("modelViewMatrix");
		this.cameraPositionLocation = super.getUniformLocation("cameraPosition");
		this.colorLocation = super.getUniformLocation("in_color");
		this.fogDistanceLocation = super.getUniformLocation("fogDistance");
	}

	@Override
	protected void bindAttributeLocations()
	{
		super.bindAttribLocation(0, "in_position");
		super.bindAttribLocation(1, "in_color");
		super.bindAttribLocation(2, "in_normal");
	}

	public void setShaderBase(Mat4 projectionMatrix, Vec3 cameraPosition, float fogDistance)
	{
		this.setProjectionMatrix(projectionMatrix);
		this.setCameraPosition(cameraPosition);
		this.setFogDistance(fogDistance);
		this.setColor(-1, -1, -1, -1);
	}

	@Override
	public void setProjectionMatrix(Mat4 projectionMatrix)
	{
		super.loadMat4(this.projectionMatrixLocation, projectionMatrix);
	}

	@Override
	public void setModelViewMatrix(Mat4 modelViewMatrix)
	{
		super.loadMat4(this.modelViewMatrixLocation, modelViewMatrix);
	}

	public void setCameraPosition(Vec3 cameraPosition)
	{
		super.loadVec3(this.cameraPositionLocation, cameraPosition);
	}

	public void setColor(float r, float g, float b, float a)
	{
		super.load4f(this.colorLocation, r, g, b, a);
	}

	public void setFogDistance(float distance)
	{
		super.loadFloat(this.fogDistanceLocation, distance);
	}

	@Override
	public void setUseTexture(boolean useTexture)
	{

	}
}