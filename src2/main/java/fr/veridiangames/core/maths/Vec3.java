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

public class Vec3 {

	public static final Vec3 UP = new Vec3(0, 1, 0);
	public static final Vec3 FRONT = new Vec3(0, 0, 1);
	public static final Vec3 RIGHT = new Vec3(1, 0, 0);

	public Vec2 xy() { return new Vec2(this.x, this.y); }
	public Vec2 xz() { return new Vec2(this.x, this.z); }
	public Vec2 yz() { return new Vec2(this.y, this.z); }

	public Vec3i getInts() { return new Vec3i((int) this.x, (int) this.y, (int) this.z); }

	public float x, y, z;

	public Vec3() {
		this(0, 0, 0);
	}

	public Vec3(float v) {
		this(v, v, v);
	}

	public Vec3(Vec3 v) {
		this(v.x, v.y, v.z);
	}

	public Vec3(Vec3i v) {
		this(v.x, v.y, v.z);
	}

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vec3 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}


	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float sqrt() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public float magnitude() {
		return (float) Math.sqrt(this.sqrt());
	}

	public Vec3 normalize() {
		float mag = this.magnitude();

		this.x /= mag;
		this.y /= mag;
		this.z /= mag;

		return this;
	}

	public Vec3 gtNorm(Vec3 ref)
	{
		Vec3 result = new Vec3();
		Vec3 d = new Vec3(this.x - ref.x, this.y - ref.y, this.z - ref.z);
		float abx = Math.abs(d.x);
		float aby = Math.abs(d.y);
		float abz = Math.abs(d.z);
		float good = Mathf.max(Mathf.max(abx, aby), abz);
		if (abx == good)
		{
			result.add(1.0f, 0.0f, 0.0f);
			if (d.x < 0)
				result.x *= -1;
		}
		if (aby == good)
		{
			result.add(0.0f, 1.0f, 0.0f);
			if (d.y < 0)
				result.y *= -1;
		}
		if (abz == good)
		{
			result.add(0.0f, 0.0f, 1.0f);
			if (d.z < 0)
				result.z *= -1;
		}

		return result;
	}

	public Vec3 checkNormals() {
		float max = Math.max(Math.max(this.x, this.y), this.z);
		float min = Math.min(Math.min(this.x, this.y), this.z);

		float absMax = Math.abs(max - 1);
		float absMin = Math.abs(min);

		float v = 0;
		if (absMax > absMin) v = min;
		else v = max;
		int rv = 1;

		if (v < 0.5f) rv = -1;

		return new Vec3(v == this.x ? rv : 0, v == this.y ? rv : 0, v == this.z ? rv : 0);
	}

	public Vec3 cross(Vec3 r) {
		float nx = this.y * r.z - this.z * r.y;
		float ny = this.z * r.x - this.x * r.z;
		float nz = this.x * r.y - this.y * r.x;

		return new Vec3(nx, ny, nz);
	}

	public float dot(Vec3 r) {
		return this.x * r.x + this.y * r.y + this.z * r.z;
	}

	public float max() {
		return Mathf.max(this.x, Mathf.max(this.y, this.z));
	}

	public float min() {
		return Mathf.min(this.x, Mathf.min(this.y, this.z));
	}

//	public float squaredDistance(Vec3 v) {
//		return (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z);
//	}
//
//	public float distance(Vec3 v) {
//		return (float) Math.sqrt(squaredDistance(v));
//	}

	public Vec3 reflect(Vec3 normal) {
		return this.sub(normal.copy().mul(this.dot(normal) * 2.0f));
	}

	public Vec3 refract(Vec3 normal, float eta) {
		float dot = normal.dot(this);
		float k = 1.f - eta * eta * (1.f - dot * dot);
		Vec3 result = normal.mul(this.mul(eta).sub((float) (eta * dot + Math.sqrt(k))));

		if (k < 0.f)
			return new Vec3();
		else
			return result;
	}

	public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.lerp(a.x, b.x, t);
		float y = Mathf.lerp(a.y, b.y, t);
		float z = Mathf.lerp(a.z, b.z, t);

		return new Vec3(x, y, z);
	}

	public static Vec3 cLerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.cLerp(a.x, b.x, t);
		float y = Mathf.cLerp(a.y, b.y, t);
		float z = Mathf.cLerp(a.z, b.z, t);

		return new Vec3(x, y, z);
	}

	public static Vec3 cLerp(Vec3 a, Vec3 b, float t1, float t2, float t3) {
		float ft1 = (float) (t1 * Math.PI);
		float f1 = (float) ((1f - Math.cos(ft1)) * 0.5f);
		float ft2 = (float) (t2 * Math.PI);
		float f2 = (float) ((1f - Math.cos(ft2)) * 0.5f);
		float ft3 = (float) (t3 * Math.PI);
		float f3 = (float) ((1f - Math.cos(ft3)) * 0.5f);

		float x = a.x * (1f - f1) + b.x * f1;
		float y = a.y * (1f - f2) + b.y * f2;
		float z = a.z * (1f - f3) + b.z * f3;

		return new Vec3(x, y, z);
	}

	public static Vec3 sLerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.sLerp(a.x, b.x, t);
		float y = Mathf.sLerp(a.y, b.y, t);
		float z = Mathf.sLerp(a.z, b.z, t);

		return new Vec3(x, y, z);
	}

	public Vec3 negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;

		return this;
	}

	public Vec3 add(Vec3 vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;

		return this;
	}

	public Vec3 sub(Vec3 vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;

		return this;
	}

	public Vec3 mul(Vec3 vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;

		return this;
	}

	public Vec3 div(Vec3 vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;

		return this;
	}

	public Vec3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Vec3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Vec3 mul(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;

		return this;
	}

	public Vec3 div(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}

	public Vec3 add(float v) {
		this.x += v;
		this.y += v;
		this.z += v;

		return this;
	}

	public Vec3 sub(float v) {
		this.x -= v;
		this.y -= v;
		this.z -= v;

		return this;
	}

	public Vec3 mul(float v) {
		this.x *= v;
		this.y *= v;
		this.z *= v;

		return this;
	}

	public Vec3 div(float v) {
		this.x /= v;
		this.y /= v;
		this.z /= v;

		return this;
	}

	public Vec3 reo()
	{
		Vec3 result = new Vec3();

		result.x = 1 - this.x;
		result.y = 1 - this.y;
		result.z = 1 - this.z;

		System.out.println(this + " || " + result);


		return result;
	}

	public Vec3 abs()
	{
		if (this.x < 0)
			this.x = -this.x;
		if (this.y < 0)
			this.y = -this.y;
		if (this.z < 0)
			this.z = -this.z;

		return this;
	}

	public Vec3 rotate(Quat rot) {
		Quat w = rot.mul(this).mul(rot.conjugate());
		return new Vec3(w.x, w.y, w.z);
	}

	public static Vec3 random()
	{
		return new Vec3(Mathf.random() * 2.0f - 1.0f, Mathf.random() * 2.0f - 1.0f, Mathf.random() * 2.0f - 1.0f);
	}

	public Vec3 copy() {
		return new Vec3(this.x, this.y, this.z);
	}

	@Override
	public String toString() {
		return this.x + " " + this.y + " " + this.z;
	}

	public boolean equals(Vec3 v) {
		return this.x == v.x && this.y == v.y && this.z == v.z;
	}

	public boolean equals(float x, float y, float z)  {
		return this.x == x && this.y == y && this.z == z;
	}

	public void println() {
		System.out.println(this);
	}

	public void print() {
		System.out.print(this);
	}
}
