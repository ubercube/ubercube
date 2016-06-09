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

package fr.veridiangames.core.game.data.world;

import java.util.Random;

import fr.veridiangames.core.maths.Vec2;

public class Noise
{
	private long seed;
	private Random rand;
	private int octave;
	private float amplitude;
	
	public Noise(long seed, int octave, float amplitude) {
		this.seed = seed;
		this.octave = octave;
		this.amplitude = amplitude;
		
		rand = new Random();
	}
	
	public float getNoise(float x, float z, float height, float inverse) {
		int xmin = (int) (double) x / octave;
		int xmax = (int) xmin + 1;
		int zmin = (int) (double) z / octave;
		int zmax = (int) zmin + 1;
		
		Vec2 a = new Vec2(xmin, zmin);
		Vec2 b = new Vec2(xmax, zmin);
		Vec2 c = new Vec2(xmax, zmax);
		Vec2 d = new Vec2(xmin, zmax);
		
		float ra = (float) noise(a);
		float rb = (float) noise(b);
		float rc = (float) noise(c);
		float rd = (float) noise(d);
		
		float t1 = (x - xmin * octave) / octave;
		float t2 = (z - zmin * octave) / octave;
		
		float i1 = interpolate(ra, rb, t1);
		float i2 = interpolate(rd, rc, t1);
		float h  = interpolate(i1, i2, t2);
		
		return ((h * 2 - 1) * inverse + height) * amplitude; 
	}
	
	private float interpolate(float a, float b, float t) {
		float ft = (float) (t * Math.PI);
		float f = (float) ((1f - Math.cos(ft)) * 0.5f);
		
		return a * (1f - f) + b * f;
	}
	
	private double noise(Vec2 coord) {
		rand.setSeed((long) (coord.x * 43594546 + coord.y * 29438876 + seed));
		return rand.nextDouble();
	}

	public long getSeed() {
		return seed;
	}

	public int getOctave() {
		return octave;
	}

	public float getAmplitude() {
		return amplitude;
	}
}
