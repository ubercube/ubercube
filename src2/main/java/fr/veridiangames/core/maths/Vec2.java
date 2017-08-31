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

public class Vec2 {

	public static final Vec2 UP = new Vec2(0, 1);
	public static final Vec2 RIGHT = new Vec2(1, 0);

	public float x, y;

	public Vec2() {
		this(0, 0);
	}

	public Vec2(float v) {
		this(v, v);
	}

	public Vec2(Vec2 v) {
		this(v.x, v.y);
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;

		return this;
	}

	public float sqrt() {
		return this.x * this.x + this.y * this.y;
	}

	public float magnitude() {
		return (float) Math.sqrt(this.sqrt());
	}

	public Vec2 normalize() {
		return new Vec2(this.x / this.magnitude(), this.y / this.magnitude());
	}

	public Vec2 cross(Vec2 r) {
		return new Vec2(r.x, -r.y);
	}

	public float dot(Vec2 r) {
		return this.x * r.x + this.y * r.y;
	}

	public float max() {
		return Math.max(this.x, this.y);
	}

	public float min() {
		return Math.min(this.x, this.y);
	}

	public Vec2 reflect(Vec2 normal) {
		return this.sub(normal.mul(this.dot(normal) * 2.0f));
	}

	public Vec2 refract(Vec2 normal, float eta) {
		float dot = normal.dot(this);
		float k = 1.f - eta * eta * (1.f - dot * dot);
		Vec2 result = normal.mul(this.mul(eta).sub((float) (eta * dot + Math.sqrt(k))));

		if (k < 0.f)
			return new Vec2();
		else
			return result;
	}

	public static Vec2 lerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.lerp(a.x, b.x, t);
		float y = Mathf.lerp(a.y, b.y, t);

		return new Vec2(x, y);
	}

	public static Vec2 cLerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.cLerp(a.x, b.x, t);
		float y = Mathf.cLerp(a.y, b.y, t);

		return new Vec2(x, y);
	}

	public static Vec2 sLerp(Vec3 a, Vec3 b, float t) {
		float x = Mathf.sLerp(a.x, b.x, t);
		float y = Mathf.sLerp(a.y, b.y, t);

		return new Vec2(x, y);
	}

	public Vec2 negate() {
		this.x = -this.x;
		this.y = -this.y;

		return this;
	}

	public Vec2 add(Vec2 vec) {
		this.x += vec.x;
		this.y += vec.y;

		return this;
	}

	public Vec2 sub(Vec2 vec) {
		this.x -= vec.x;
		this.y -= vec.y;

		return this;
	}

	public Vec2 mul(Vec2 vec) {
		this.x *= vec.x;
		this.y *= vec.y;

		return this;
	}

	public Vec2 div(Vec2 vec) {
		this.x /= vec.x;
		this.y /= vec.y;

		return this;
	}

	public Vec2 add(float x, float y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public Vec2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;

		return this;
	}

	public Vec2 mul(float x, float y) {
		this.x *= x;
		this.y *= y;

		return this;
	}

	public Vec2 div(float x, float y) {
		this.x /= x;
		this.y /= y;

		return this;
	}

	public Vec2 add(float v) {
		this.x += v;
		this.y += v;

		return this;
	}

	public Vec2 sub(float v) {
		this.x -= v;
		this.y -= v;

		return this;
	}

	public Vec2 mul(float v) {
		this.x *= v;
		this.y *= v;

		return this;
	}

	public Vec2 div(float v) {
		this.x /= v;
		this.y /= v;

		return this;
	}

	public Vec2 copy() {
		return new Vec2(this.x, this.y);
	}

	@Override
	public String toString() {
		return this.x + " " + this.y;
	}

	public boolean equals(Vec2 v) {
		return this.x == v.x && this.y == v.y;
	}

	public void print() {
		System.out.print(this);
	}

	public void println() {
		System.out.println(this);
	}
}