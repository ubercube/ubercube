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

package fr.veridiangames.core.maths;

public class Vec3i {
	
	public static final Vec3i UP = new Vec3i(0, 1, 0);
	public static final Vec3i FRONT = new Vec3i(0, 0, 1);
	public static final Vec3i RIGHT = new Vec3i(1, 0, 0);
	
	public Vec3 toVec3() {return new Vec3(x, y, z); }
	
	public int x, y, z;

	public Vec3i() {
		this(0, 0, 0);
	}
	
	public Vec3i(int v) {
		this(v, v, v);
	}
	
	public Vec3i(Vec3i v) {
		this(v.x, v.y, v.z);
	}

	public Vec3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vec3i vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int sqrt() {
		return x * x + y * y + z * z;
	}
	
	public Vec3i cross(Vec3i r) {
		int nx = y * r.z - z * r.y;
		int ny = z * r.x - x * r.z;
		int nz = x * r.y - y * r.x;
		
		return new Vec3i(nx, ny, nz);
	}
	
	public int dot(Vec3i r) {
		return x * r.x + y * r.y + z * r.z;
	}
	
	public int max() {
		return Math.max(x, Math.max(y, z));
	}
	
	public int min() {
		return Math.min(x, Math.min(y, z));
	}
	
	public int squaredDistance(Vec3i v) {
		return (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z);
	}
	
	public float distance(Vec3i v) {
		return (float) Math.sqrt((float)squaredDistance(v));
	}
	
	public Vec3i negate() {
		x = -x;
		y = -y;
		z = -z;

		return this;
	}

	public Vec3i add(Vec3i vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;

		return this;
	}

	public Vec3i sub(Vec3i vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;

		return this;
	}

	public Vec3i mul(Vec3i vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;

		return this;
	}

	public Vec3i div(Vec3i vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;

		return this;
	}
	
	public Vec3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Vec3i sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Vec3i mul(int x, int y, int z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;

		return this;
	}

	public Vec3i div(int x, int y, int z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}
	
	public Vec3i add(int v) {
		x += v;
		y += v;
		z += v;

		return this;
	}

	public Vec3i sub(int v) {
		x -= v;
		y -= v;
		z -= v;

		return this;
	}

	public Vec3i mul(int v) {
		x *= v;
		y *= v;
		z *= v;

		return this;
	}

	public Vec3i div(int v) {
		x /= v;
		y /= v;
		z /= v;

		return this;
	}

	public Vec3i copy() {
		return new Vec3i(x, y, z);
	}

	public String toString() {
		return x + " " + y + " " + z;
	}
	
	public boolean equals(Vec3i v) {
		return x == v.x && y == v.y && z == v.z;
	}

	public void println() {
		System.out.println(this);
	}
	
	public void print() {
		System.out.print(this);
	}
}
