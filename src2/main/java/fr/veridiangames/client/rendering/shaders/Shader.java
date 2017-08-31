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

import static fr.veridiangames.client.FileManager.getResource;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.client.rendering.utils.BufferUtil;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Log;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public abstract class Shader
{
	public static final String SHADER_PATH = getShaderPath();

	private static String getShaderPath()
	{
		return getResource("shaders");
	}

	private int	program;

	public Shader(String vertexShader, String fragmentShader)
	{
		this.createShaders(vertexShader, fragmentShader);
		this.getUniformLocations();
	}

	protected abstract void getUniformLocations();
	public abstract void setProjectionMatrix(Mat4 projectionMatrix);
	public abstract void setModelViewMatrix(Mat4 modelViewMatrix);
	public abstract void setUseTexture(boolean useTexture);

	protected int getUniformLocation(String name)
	{
		return glGetUniformLocation(this.program, name);
	}

	protected abstract void bindAttributeLocations();

	protected void bindAttribLocation(int loc, String name) { glBindAttribLocation(this.program, loc, name); }

	protected void loadInt(int location, int v)
	{
		glUniform1i(location, v);
	}

	protected void loadFloat(int location, float v)
	{
		glUniform1f(location, v);
	}

	protected void loadVec2(int location, Vec2 v)
	{
		glUniform2f(location, v.x, v.y);
	}

	protected void loadVec3(int location, Vec3 v)
	{
		glUniform3f(location, v.x, v.y, v.z);
	}

	protected void load2f(int location, float x, float y)
	{
		glUniform2f(location, x, y);
	}

	protected void load3f(int location, float x, float y, float z)
	{
		glUniform3f(location, x, y, z);
	}

	protected void load4f(int location, float x, float y, float z, float w)
	{
		glUniform4f(location, x, y, z, w);
	}

	protected void loadMat4(int location, Mat4 v)
	{
		glUniformMatrix4fv(location, true, BufferUtil.toMatrixBuffer(v));
	}

	public void bind()
	{
		glUseProgram(this.program);
	}

	public void unbind()
	{
		glUseProgram(0);
	}

	private void createShaders(String vertexShader, String fragmentShader)
	{
		this.program = glCreateProgram();

		if (this.program == GL_FALSE)
		{
			System.err.println("Shader program error");
			System.exit(1);
		}

		this.createShader(this.loadShader(vertexShader), GL_VERTEX_SHADER);
		this.createShader(this.loadShader(fragmentShader), GL_FRAGMENT_SHADER);

		this.bindAttributeLocations();

		glLinkProgram(this.program);
		glValidateProgram(this.program);
	}

	private int createShader(String source, int type)
	{
		int shader = glCreateShader(type);
		if (shader == GL_FALSE)
			Log.sever("Shader error: " + shader);
		glShaderSource(shader, source);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(shader, 2048));
			System.exit(1);
		}
		glAttachShader(this.program, shader);

		return shader;
	}

	private String shader120compatible(String ligne)
	{
		String result = ligne;

		result = result.replaceAll("^#version 330 core", "#version 120");
		result = result.replaceAll("^#extension GL_NV_shadow_samplers_cube : enable", "// #extension GL_NV_shadow_samplers_cube : enable");
		result = result.replaceAll("^in", "varying");
		result = result.replaceAll("^out", "varying");
		result = result.replaceAll("^layout \\(location = [0-9]\\) in", "attribute");
		result = result.replaceAll("^varying vec4 fragColor;", "// varying vec4 fragColor;");
		result = result.replaceAll("fragColor", "gl_FragColor");
		result = result.replaceAll("texture\\(", "texture2D(");
		return result;
	}

	private String loadShader(String path)
	{
		final String INCLUDE_FUNC = "#include";
		String result = "";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(SHADER_PATH + path));
			String buffer = "";
			while ((buffer = reader.readLine()) != null)
			{
				if (!Renderer.isGL33())
					buffer = this.shader120compatible(buffer);
				if (buffer.startsWith(INCLUDE_FUNC))
				{
					String[] fileDir = path.split("/");
					String dir = path.substring(0, path.length() - fileDir[fileDir.length - 1].length());
					result += this.loadShader(dir + buffer.substring(INCLUDE_FUNC.length() + 2, buffer.length() - 1));
				} else
					result += buffer + "\n";
			}
			reader.close();
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
		return result;
	}
}
