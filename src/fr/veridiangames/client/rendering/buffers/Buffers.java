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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.rendering.buffers;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

public class Buffers {
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	public static int createVertexArray() {
		int vao = glGenVertexArrays();
		vaos.add(vao);
		
		return vao;
	}
	
	public static int createVertexBuffer() {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		
		return vbo;
	}
	
	public static void clean() {
		for (Integer vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (Integer vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		
		vaos.clear();
		vbos.clear();
	}
	
	public static void deleteVertexArray(int id) {
		vaos.remove(vaos.indexOf(id));
		glDeleteVertexArrays(id);
	}
	
	public static void deleteVertexBuffer(int id) {
		vbos.remove(vbos.indexOf(id));
		glDeleteBuffers(id);
	}

	public static List<Integer> getVaos() {
		return vaos;
	}

	public static List<Integer> getVbos() {
		return vbos;
	}
}