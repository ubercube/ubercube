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

public class Mat4 {
	public float[][] matrix = new float[4][4];

	//TODO: Zbutteffen

	public static Mat4 identity() {
		Mat4 result = new Mat4();
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				result.matrix[x][y] = 0f;

		result.matrix[0][0] = 1;
		result.matrix[1][1] = 1;
		result.matrix[2][2] = 1;
		result.matrix[3][3] = 1;

		return result;
	}

	public Mat4 mul(Mat4 m) {
		Mat4 result = identity();

		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				result.matrix[x][y] = this.matrix[x][0] * m.matrix[0][y] +
									  this.matrix[x][1] * m.matrix[1][y] +
									  this.matrix[x][2] * m.matrix[2][y] +
									  this.matrix[x][3] * m.matrix[3][y];

		return result;
	}

	public static Mat4 translate(float x, float y, float z) {
		Mat4 result = identity();

		result.matrix[0][3] = x;
		result.matrix[1][3] = y;
		result.matrix[2][3] = z;

		return result;
	}

	public static Mat4 translate(Vec3 pos) {
		Mat4 result = identity();

		result.matrix[0][3] = pos.x;
		result.matrix[1][3] = pos.y;
		result.matrix[2][3] = pos.z;

		return result;
	}

	public static Mat4 rotate(float x, float y, float z) {
		Mat4 rx = identity();
		Mat4 ry = identity();
		Mat4 rz = identity();

		x = Mathf.toRadians(x);
		y = Mathf.toRadians(y);
		z = Mathf.toRadians(z);

		float xcos = Mathf.cos(x);
		float xsin = Mathf.sin(x);

		float ycos = Mathf.cos(y);
		float ysin = Mathf.sin(y);

		float zcos = Mathf.cos(z);
		float zsin = Mathf.sin(z);

		rx.matrix[1][1] = xcos;
		rx.matrix[1][2] = -xsin;
		rx.matrix[2][1] = xsin;
		rx.matrix[2][2] = xcos;

		ry.matrix[0][0] = ycos;
		ry.matrix[0][2] = -ysin;
		ry.matrix[2][0] = ysin;
		ry.matrix[2][2] = ycos;

		rz.matrix[0][0] = zcos;
		rz.matrix[0][1] = -zsin;
		rz.matrix[1][0] = zsin;
		rz.matrix[1][1] = zcos;

		return rz.mul(ry.mul(rx));
	}

	public static Mat4 rotate(Vec3 forward, Vec3 up, Vec3 right) {
		Mat4 result = identity();

		Vec3 f = new Vec3(forward).normalize();
		Vec3 r = new Vec3(right).normalize();
		Vec3 u = new Vec3(up).normalize();

		result.matrix[0][0] = r.x;
		result.matrix[0][1] = r.y;
		result.matrix[0][2] = r.z;

		result.matrix[1][0] = u.x;
		result.matrix[1][1] = u.y;
		result.matrix[1][2] = u.z;

		result.matrix[2][0] = f.x;
		result.matrix[2][1] = f.y;
		result.matrix[2][2] = f.z;

		return result;
	}

	public static Mat4 rotate(Vec3 forward, Vec3 up) {
		Mat4 m = identity();

		Vec3 f = new Vec3(forward).normalize();
		Vec3 r = new Vec3(up).normalize();
		r = r.cross(f);
		Vec3 u = f.cross(r);

		m.matrix[0][0] = r.x;
		m.matrix[0][1] = r.y;
		m.matrix[0][2] = r.z;

		m.matrix[1][0] = u.x;
		m.matrix[1][1] = u.y;
		m.matrix[1][2] = u.z;

		m.matrix[2][0] = f.x;
		m.matrix[2][1] = f.y;
		m.matrix[2][2] = f.z;

		return m;
	}

	public static Mat4 scale(float x, float y, float z) {
		Mat4 result = identity();

		result.matrix[0][0] = x;
		result.matrix[1][1] = y;
		result.matrix[2][2] = z;

		return result;
	}

	public static Mat4 scale(Vec3 vec) {
		return scale(vec.x, vec.y, vec.z);
	}

	public static Mat4 perspective(float fov, float aspect, float zNear, float zFar) {
		Mat4 result = identity();

		float FOV = (float) Math.tan(Math.toRadians(fov / 2));
		float dist = zNear - zFar;

		result.matrix[0][0] = 1.0f / (FOV * aspect);
		result.matrix[1][1] = 1.0f / FOV;

		result.matrix[2][2] = (-zNear - zFar) / dist;
		result.matrix[2][3] = 2 * zFar * zNear / dist;

		result.matrix[3][2] = 1;
		result.matrix[3][3] = 0;

		return result;
	}

	public static Mat4 orthographic(float right, float left, float top, float bottom, float zNear, float zFar) {
		Mat4 m = identity();

		m.matrix[0][0] = 2/(right - left);
		m.matrix[0][3] = -(right + left) / (right - left);

		m.matrix[1][1] = 2/(top - bottom);
		m.matrix[1][3] = -(top + bottom) / (top - bottom);

		m.matrix[2][2] = -2/(zFar - zNear);
		m.matrix[2][3] = -(zFar + zNear) / (zFar - zNear);

		return m;
	}

	public static Vec3 transform(Mat4 m, Vec3 v){
		return new Vec3(
			m.matrix[0][0] * v.x + m.matrix[0][1] * v.y + m.matrix[0][2] * v.z + m.matrix[0][3],
			m.matrix[1][0] * v.x + m.matrix[1][1] * v.y + m.matrix[1][2] * v.z + m.matrix[1][3],
			m.matrix[2][0] * v.x + m.matrix[2][1] * v.y + m.matrix[2][2] * v.z + m.matrix[2][3]
		);
	}

	public float[] getComponents()
	{
		float[] result = new float[4 * 4];
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				result[x + y * 4] = this.matrix[x][y];
		return result;
	}

}