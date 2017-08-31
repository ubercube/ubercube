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

public class Vec2i {

	public static final Vec2i UP = new Vec2i(0, 1);
	public static final Vec2i RIGHT = new Vec2i(1, 0);

	public int x, y;

	public Vec2i() {
		this(0, 0);
	}

	public Vec2i(int v) {
		this(v, v);
	}

	public Vec2i(Vec2i v) {
		this(v.x, v.y);
	}

	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vec2i cross(Vec2i r) {
		return new Vec2i(r.x, -r.y);
	}

	public int max() {
		return Math.max(this.x, this.y);
	}

	public int min() {
		return Math.min(this.x, this.y);
	}

	public Vec2i negate() {
		this.x = -this.x;
		this.y = -this.y;

		return this;
	}

	public Vec2i add(Vec2i vec) {
		this.x += vec.x;
		this.y += vec.y;

		return this;
	}

	public Vec2i sub(Vec2i vec) {
		this.x -= vec.x;
		this.y -= vec.y;

		return this;
	}

	public Vec2i mul(Vec2i vec) {
		this.x *= vec.x;
		this.y *= vec.y;

		return this;
	}

	public Vec2i div(Vec2i vec) {
		this.x /= vec.x;
		this.y /= vec.y;

		return this;
	}

	public Vec2i add(int x, int y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public Vec2i sub(int x, int y) {
		this.x -= x;
		this.y -= y;

		return this;
	}

	public Vec2i mul(int x, int y) {
		this.x *= x;
		this.y *= y;

		return this;
	}

	public Vec2i div(int x, int y) {
		this.x /= x;
		this.y /= y;

		return this;
	}

	public Vec2i add(int v) {
		this.x += v;
		this.y += v;

		return this;
	}

	public Vec2i sub(int v) {
		this.x -= v;
		this.y -= v;

		return this;
	}

	public Vec2i mul(int v) {
		this.x *= v;
		this.y *= v;

		return this;
	}

	public Vec2i div(int v) {
		this.x /= v;
		this.y /= v;

		return this;
	}

	public Vec2i copy() {
		return new Vec2i(this.x, this.y);
	}

	@Override
	public String toString() {
		return this.x + " " + this.y;
	}

	public boolean equals(Vec2i v) {
		return this.x == v.x && this.y == v.y;
	}

	public void print() {
		System.out.print(this);
	}

	public void println() {
		System.out.println(this);
	}
}