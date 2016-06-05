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

public class Mathf
{
	public static final float PI = 3.14159265358979323846264338327950288419716939937510582f;

	public static float min(float a, float b)
	{
		if (a < b)
			return a;
		return b;
	}

	public static float max(float a, float b)
	{
		if (a > b)
			return a;
		return b;
	}

	public static int norm(float v)
	{
		if (v >= 0)
			return 1;
		return -1;
	}

	public static float clamp(float min, float max, float value)
	{
		if (value < min)
			value = min;
		if (value > max)
			value = max;

		return value;
	}

	public static float nearest(float a, float b, float v)
	{
		float absA = abs(a);
		float absB = abs(b);
		float absV = abs(v);

		float tempA = abs(absV - absA);
		float tempB = abs(absV - absB);

		if (tempA < tempB)
			return a;
		return b;
	}

	public static float cos(float angle)
	{
		return (float) Math.cos(angle);
	}

	public static float sin(float angle)
	{
		return (float) Math.sin(angle);
	}

	public static float tan(float angle)
	{
		return (float) Math.tan(angle);
	}

	public static float acos(float angle)
	{
		return (float) Math.acos(angle);
	}

	public static float asin(float angle)
	{
		return (float) Math.asin(angle);
	}

	public static float atan(float angle)
	{
		return (float) Math.atan(angle);
	}

	public static float atan2(float x, float y)
	{
		return (float) Math.atan2(x, y);
	}

	public static float toRadians(float angle)
	{
		return (float) Math.toRadians(angle);
	}

	public static float toDegrees(float angle)
	{
		return (float) Math.toDegrees(angle);
	}

	public static float sqrt(float a)
	{
		return (float) Math.sqrt(a);
	}

	public static float floor(float a)
	{
		return (float) Math.floor(a);
	}

	public static float ceil(float a)
	{
		return (float) Math.ceil(a);
	}

	public static float round(float a)
	{
		return (float) Math.round(a);
	}

	public static float random()
	{
		return (float) Math.random();
	}

	public static float random(float a, float b)
	{
		return (float) a + random() * (b - a);
	}

	public static float abs(float a)
	{
		return (float) Math.abs(a);
	}

	public static float pow(float f, float p)
	{
		return (float) Math.pow(f, p);
	}

	public static float lerp(float s, float e, float t)
	{
		return s + (e - s) * t;
	}

	public static float cLerp(float s, float e, float t)
	{
		float t2 = (1 - (float) Math.cos(t * Math.PI)) / 2;
		return (s * (1 - t2) + e * t2);
	}

	public static float sLerp(float s, float e, float t)
	{
		float t2 = (1 - (float) Math.sin(t * Math.PI)) / 2;
		return (s * (1 - t2) + e * t2);
	}

	public static float bLerp(float c00, float c10, float c01, float c11, float tx, float ty)
	{
		return cLerp(cLerp(c00, c10, tx), cLerp(c01, c11, tx), ty);
	}
}