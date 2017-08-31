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
public class Gui3DShader extends Shader
{
	public static final String VERTEX_PATH = "/gui_3d.vert";
	public static final String FRAGMENT_PATH = "/gui_3d.frag";

	private int projectionMatrixLocation;
	private int modelViewMatrixLocation;
	private int colorLocation;
	private int useTextureLocation;
	private int useVColorLocation;

	public Gui3DShader()
	{
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	public Gui3DShader(String vertexPath, String fragmentPath)
	{
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void getUniformLocations()
	{
		this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		this.modelViewMatrixLocation = super.getUniformLocation("modelViewMatrix");
		this.colorLocation = super.getUniformLocation("in_color");
		this.useTextureLocation = super.getUniformLocation("useTexture");
		this.useVColorLocation = super.getUniformLocation("useVColor");
	}

	@Override
	protected void bindAttributeLocations()
	{
		super.bindAttribLocation(0, "in_position");
		super.bindAttribLocation(1, "in_coords");
	}

	public void setShaderBase(Mat4 projectionMatrix)
	{
		this.setProjectionMatrix(projectionMatrix);
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

	public void setColor(float r, float g, float b, float a)
	{
		super.load4f(this.colorLocation, r, g, b, a);
	}

	public void setColor(Color4f color)
	{
		super.load4f(this.colorLocation, color.r, color.g, color.b, color.a);
	}

	@Override
	public void setUseTexture(boolean useTexture)
	{
		super.loadInt(this.useTextureLocation, useTexture ? 1 : 0);
	}

	public void enableVColor(boolean useVColor)
	{
		super.loadInt(this.useVColorLocation, useVColor ? 1 : 0);
	}
}
