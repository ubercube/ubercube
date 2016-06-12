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

package fr.veridiangames.client.rendering.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Mat4;

public class BufferUtil {
	
	public static ByteBuffer toByteBuffer(FloatBuffer buffer) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() * 4);
		byteBuffer.asFloatBuffer().put(buffer);
		
		return byteBuffer;
	}
	
	public static ByteBuffer toByteBuffer(IntBuffer buffer) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() * 4);
		byteBuffer.asIntBuffer().put(buffer);
		byteBuffer.flip();
		
		return byteBuffer;
	}
	
	public static FloatBuffer createBuffer(float[] v) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(v.length);
		buffer.put(v);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createBuffer(int[] v) {
		IntBuffer buffer = BufferUtils.createIntBuffer(v.length);
		buffer.put(v);
		buffer.flip();
		return buffer;
	}
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
	public static FloatBuffer toMatrixBuffer(Mat4 v) {
		matrixBuffer.clear();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				matrixBuffer.put(v.matrix[x][y]);
			}	
		}
		matrixBuffer.flip();
		
		return matrixBuffer;
	}
}
