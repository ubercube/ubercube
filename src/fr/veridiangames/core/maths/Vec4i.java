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

package fr.veridiangames.core.maths;

public class Vec4i {

	public int x, y, z, w;

	public Vec4i() {
		this(0, 0, 0, 0);
	}
	
	public Vec4i(int v) {
		this(v, v, v, v);
	}
	
	public Vec4i(Vec4i v) {
		this(v.x, v.y, v.z, v.w);
	}

	public Vec4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Vec4i vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = vec.w;
	}
	
	public void set(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public int sqrt() {
		return x * x + y * y + z * z + w * w;
	}
	
	public int magnitude() {
		return (int) Math.sqrt(sqrt());
	}
	
	public Vec4i normalize() {
		int mag = magnitude();
		return new Vec4i(x / mag, y / mag, z / mag, w / mag);
	}
	
	public int max() {
		return Math.max(Math.max(x, y), Math.max(z, w));
	}
	
	public int min() {
		return Math.min(Math.min(x, y), Math.min(z, w));
	}
	
	public int squaredDistance(Vec4i v) {
		return (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z) + (w-v.w)*(w-v.w);
	}
	
	public float distance(Vec4i v) {
		return (float) Math.sqrt((float)squaredDistance(v));
	}
	
	public Vec4i negate() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;

		return this;
	}

	public Vec4i add(Vec4i vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		w += vec.w;
		
		return this;
	}

	public Vec4i sub(Vec4i vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		w -= vec.w;
		
		return this;
	}

	public Vec4i mul(Vec4i vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		w *= vec.w;

		return this;
	}

	public Vec4i div(Vec4i vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		w /= vec.w;

		return this;
	}
	
	public Vec4i add(int x, int y, int z, int w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;

		return this;
	}

	public Vec4i sub(int x, int y, int z, int w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;

		return this;
	}

	public Vec4i mul(int x, int y, int z, int w) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;

		return this;
	}

	public Vec4i div(int x, int y, int z, int w) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		this.w /= w;

		return this;
	}
	
	public Vec4i add(int v) {
		x += v;
		y += v;
		z += v;
		w += v;
		
		return this;
	}

	public Vec4i sub(int v) {
		x -= v;
		y -= v;
		z -= v;
		w -= v;

		return this;
	}

	public Vec4i mul(int v) {
		x *= v;
		y *= v;
		z *= v;
		w *= v;
		
		return this;
	}

	public Vec4i div(int v) {
		x /= v;
		y /= v;
		z /= v;
		w /= v;

		return this;
	}

	public Vec4i copy() {
		return new Vec4i(x, y, z, w);
	}

	public String toString() {
		return x + " " + y + " " + z + " " + w;
	}
	
	public boolean equals(Vec4i v) {
		return x == v.x && y == v.y && z == v.z && w == v.w;
	}

	public void println() {
		System.out.println(this);
	}
	
	public void print() {
		System.out.print(this);
	}
}
