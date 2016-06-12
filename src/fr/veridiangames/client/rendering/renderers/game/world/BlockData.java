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

package fr.veridiangames.client.rendering.renderers.game.world;

import fr.veridiangames.core.utils.Color4f;

public class BlockData
{
	public static float[] blockDataFront(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x + s, y, z,				r * 0.9f * s1, g * 0.9f * s1, b * 0.9f * s1, 1,			0, 0, 1,	//1
			x, y, z,					r * 0.9f * s0, g * 0.9f * s0, b * 0.9f * s0, 1,			0, 0, 1,	//0
			x, y + s, z,				r * 0.9f * s3, g * 0.9f * s3, b * 0.9f * s3, 1,			0, 0, 1,	//3
			x + s, y + s, z,			r * 0.9f * s2, g * 0.9f * s2, b * 0.9f * s2, 1,			0, 0, 1	//2
		};
	}
	
	public static float[] blockDataBack(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x, y, z + s,				r * 0.9f * s1, g * 0.9f * s1, b * 0.9f * s1, 1,			0, 0, -1,	 //5
			x + s, y, z + s,			r * 0.9f * s0, g * 0.9f * s0, b * 0.9f * s0, 1,			0, 0, -1,	 //4	
			x + s, y + s, z + s,		r * 0.9f * s3, g * 0.9f * s3, b * 0.9f * s3, 1,			0, 0, -1,		//7
			x, y + s, z + s,			r * 0.9f * s2, g * 0.9f * s2, b * 0.9f * s2, 1,			0, 0, -1		//6
		};
	}
	
	public static float[] blockDataLeft(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x, y + s, z,				r * 0.8f * s1, g * 0.8f * s1, b * 0.8f * s1, 1,			-1, 0, 0,		//3
			x, y, z,					r * 0.8f * s0, g * 0.8f * s0, b * 0.8f * s0, 1,			-1, 0, 0,		//0
			x, y, z + s,				r * 0.8f * s3, g * 0.8f * s3, b * 0.8f * s3, 1,			-1, 0, 0,		//5
			x, y + s, z + s,			r * 0.8f * s2, g * 0.8f * s2, b * 0.8f * s2, 1,			-1, 0, 0		//6
		};
	}
	
	public static float[] blockDataRight(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x + s, y, z,				r * 0.8f * s1, g * 0.8f * s1, b * 0.8f * s1, 1,			1, 0, 0,		//1
			x + s, y + s, z,			r * 0.8f * s0, g * 0.8f * s0, b * 0.8f * s0, 1,			1, 0, 0,	 //2	
			x + s, y + s, z + s,		r * 0.8f * s3, g * 0.8f * s3, b * 0.8f * s3, 1,			1, 0, 0,		//7
			x + s, y, z + s,			r * 0.8f * s2, g * 0.8f * s2, b * 0.8f * s2, 1,			1, 0, 0		//4
		};
	}
	
	public static float[] blockDataDown(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x, y, z,					r * 0.7f * s1, g * 0.7f * s1, b * 0.7f * s1, 1,			0, -1, 0,		//0
			x + s, y, z,				r * 0.7f * s0, g * 0.7f * s0, b * 0.7f * s0, 1,			0, -1, 0,	 //1
			x + s, y, z + s,			r * 0.7f * s3, g * 0.7f * s3, b * 0.7f * s3, 1,			0, -1, 0,	 //4
			x, y, z + s,				r * 0.7f * s2, g * 0.7f * s2, b * 0.7f * s2, 1,			0, -1, 0	 //5
		};
	}
	
	public static float[] blockDataUp(float x, float y, float z, float brightness, float[] shading, Color4f color) {
		float s0 = shading[0] * brightness;
		float s1 = shading[1] * brightness;
		float s2 = shading[2] * brightness;
		float s3 = shading[3] * brightness;
		
		float r = color.r;
		float g = color.g;
		float b = color.b;
		
		float s = 1;
		return new float[] {
			x + s, y + s, z,			r * 1f * s1, g * 1f * s1, b * 1f * s1, 1,			0, 1, 0,	 //2
			x, y + s, z,				r * 1f * s0, g * 1f * s0, b * 1f * s0, 1,			0, 1, 0,	 //3
			x, y + s, z + s, 			r * 1f * s3, g * 1f * s3, b * 1f * s3, 1,			0, 1, 0,	 //6
			x + s, y + s, z + s,		r * 1f * s2, g * 1f * s2, b * 1f * s2, 1,			0, 1, 0	 //7
		};
	}
}
