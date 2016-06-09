/*
 *   Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
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
		return x * x + y * y;
	}
	
	public float magnitude() {
		return (float) Math.sqrt(sqrt());
	}
	
	public Vec2 normalize() {
		return new Vec2(x / magnitude(), y / magnitude());
	}
	
	public Vec2 cross(Vec2 r) {
		return new Vec2(r.x, -r.y);
	}
	
	public float dot(Vec2 r) {
		return x * r.x + y * r.y;
	}
	
	public float max() {
		return Math.max(x, y);
	}
	
	public float min() {
		return Math.min(x, y);
	}
	
	public Vec2 reflect(Vec2 normal) {
		return sub(normal.mul(dot(normal) * 2.0f));
	}

	public Vec2 refract(Vec2 normal, float eta) {
		float dot = normal.dot(this);
		float k = 1.f - eta * eta * (1.f - dot * dot);
		Vec2 result = normal.mul(mul(eta).sub((float) (eta * dot + Math.sqrt(k))));
		
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
		x = -x;
		y = -y;

		return this;
	}

	public Vec2 add(Vec2 vec) {
		x += vec.x;
		y += vec.y;

		return this;
	}

	public Vec2 sub(Vec2 vec) {
		x -= vec.x;
		y -= vec.y;

		return this;
	}

	public Vec2 mul(Vec2 vec) {
		x *= vec.x;
		y *= vec.y;

		return this;
	}

	public Vec2 div(Vec2 vec) {
		x /= vec.x;
		y /= vec.y;
		
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
		x += v;
		y += v;

		return this;
	}

	public Vec2 sub(float v) {
		x -= v;
		y -= v;

		return this;
	}

	public Vec2 mul(float v) {
		x *= v;
		y *= v;

		return this;
	}

	public Vec2 div(float v) {
		x /= v;
		y /= v;

		return this;
	}

	public Vec2 copy() {
		return new Vec2(x, y);
	}

	public String toString() {
		return x + " " + y;
	}
	
	public boolean equals(Vec2 v) {
		return x == v.x && y == v.y;
	}
	
	public void print() {
		System.out.print(this);
	}
	
	public void println() {
		System.out.println(this);
	}
}