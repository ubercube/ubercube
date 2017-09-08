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
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public class MinimapFboShader extends Shader
{
	public static final String VERTEX_PATH = "/minimap_fbo.vert";
	public static final String FRAGMENT_PATH = "/minimap_fbo.frag";

	private int projectionMatrixLocation;
	private int modelViewMatrixLocation;

	public MinimapFboShader()
	{
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public MinimapFboShader(String vertexPath, String fragmentPath)
	{
		super(vertexPath, fragmentPath);
	}

	protected void bindAttributeLocations()
	{
		super.bindAttribLocation(0, "in_position");
		super.bindAttribLocation(1, "in_coords");
	}

	protected void getUniformLocations()
	{
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		modelViewMatrixLocation = super.getUniformLocation("modelViewMatrix");
	}
	
	public void setOrtho(float right, float left, float top, float bottom, float zNear, float zFar)
	{
		this.setProjectionMatrix(Mat4.orthographic(right, left, top, bottom, zNear, zFar));		
	}
	
	public void setProjectionMatrix(Mat4 projectionMatrix)
	{
		super.loadMat4(projectionMatrixLocation, projectionMatrix);
	}

	public void setModelViewMatrix(Mat4 modelViewMatrix)
	{
		super.loadMat4(modelViewMatrixLocation, modelViewMatrix);
	}

	public void setColor(Color4f color)
	{
		super.load4f(super.getUniformLocation("in_color"), color.r, color.g, color.b, color.a);
	}

	public void setUseTexture(boolean useTexture) {}
}
