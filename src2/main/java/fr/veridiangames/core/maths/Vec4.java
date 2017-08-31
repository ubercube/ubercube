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

public class Vec4 {

	public float x, y, z, w;

	public Vec4() {
		this(0, 0, 0, 0);
	}

	public Vec4(float v) {
		this(v, v, v, v);
	}

	public Vec4(Vec4 v) {
		this(v.x, v.y, v.z, v.w);
	}

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void set(Vec4 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = vec.w;
	}

	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float sqrt() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}

	public float magnitude() {
		return (float) Math.sqrt(this.sqrt());
	}

	public Vec4 normalize() {
		float mag = this.magnitude();
		return new Vec4(this.x / mag, this.y / mag, this.z / mag, this.w / mag);
	}

	public float max() {
		return Math.max(Math.max(this.x, this.y), Math.max(this.z, this.w));
	}

	public float min() {
		return Math.min(Math.min(this.x, this.y), Math.min(this.z, this.w));
	}

	public float squaredDistance(Vec4 v) {
		return (this.x-v.x)*(this.x-v.x) + (this.y-v.y)*(this.y-v.y) + (this.z-v.z)*(this.z-v.z) + (this.w-v.w)*(this.w-v.w);
	}

	public float distance(Vec4 v) {
		return (float) Math.sqrt(this.squaredDistance(v));
	}

	public static Vec4 lerp(Vec4 a, Vec4 b, float t) {
		float x = Mathf.lerp(a.x, b.x, t);
		float y = Mathf.lerp(a.y, b.y, t);
		float z = Mathf.lerp(a.z, b.z, t);
		float w = Mathf.lerp(a.w, b.w, t);

		return new Vec4(x, y, z, w);
	}

	public static Vec4 cLerp(Vec4 a, Vec4 b, float t) {
		float x = Mathf.cLerp(a.x, b.x, t);
		float y = Mathf.cLerp(a.y, b.y, t);
		float z = Mathf.cLerp(a.z, b.z, t);
		float w = Mathf.cLerp(a.w, b.w, t);

		return new Vec4(x, y, z, w);
	}

	public static Vec4 sLerp(Vec4 a, Vec4 b, float t) {
		float x = Mathf.sLerp(a.x, b.x, t);
		float y = Mathf.sLerp(a.y, b.y, t);
		float z = Mathf.sLerp(a.z, b.z, t);
		float w = Mathf.sLerp(a.w, b.w, t);

		return new Vec4(x, y, z, w);
	}

	public Vec4 negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		this.w = -this.w;

		return this;
	}

	public Vec4 add(Vec4 vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		this.w += vec.w;

		return this;
	}

	public Vec4 sub(Vec4 vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		this.w -= vec.w;

		return this;
	}

	public Vec4 mul(Vec4 vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		this.w *= vec.w;

		return this;
	}

	public Vec4 div(Vec4 vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
		this.w /= vec.w;

		return this;
	}

	public Vec4 add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;

		return this;
	}

	public Vec4 sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;

		return this;
	}

	public Vec4 mul(float x, float y, float z, float w) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;

		return this;
	}

	public Vec4 div(float x, float y, float z, float w) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		this.w /= w;

		return this;
	}

	public Vec4 add(float v) {
		this.x += v;
		this.y += v;
		this.z += v;
		this.w += v;

		return this;
	}

	public Vec4 sub(float v) {
		this.x -= v;
		this.y -= v;
		this.z -= v;
		this.w -= v;

		return this;
	}

	public Vec4 mul(float v) {
		this.x *= v;
		this.y *= v;
		this.z *= v;
		this.w *= v;

		return this;
	}

	public Vec4 div(float v) {
		this.x /= v;
		this.y /= v;
		this.z /= v;
		this.w /= v;

		return this;
	}

	public Vec4 copy() {
		return new Vec4(this.x, this.y, this.z, this.w);
	}

	@Override
	public String toString() {
		return this.x + " " + this.y + " " + this.z + " " + this.w;
	}

	public boolean equals(Vec4 v) {
		return this.x == v.x && this.y == v.y && this.z == v.z && this.w == v.w;
	}

	public void println() {
		System.out.println(this);
	}

	public void print() {
		System.out.print(this);
	}
}
