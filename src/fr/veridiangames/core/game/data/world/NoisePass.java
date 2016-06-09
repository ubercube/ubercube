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

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class NoisePass
{
	private Noise noise;
	private long  seed;
	private int octave;
	private float amplitude;
	private float height;
	private float inverse;
	
	public NoisePass(long seed, int octave, float amplitude, float height, float inverse)
	{
		this.seed = seed;
		this.octave = octave;
		this.amplitude = amplitude;
		this.height = height;
		this.inverse = inverse;
		
		generateNoisePass();
	}
	
	private void generateNoisePass()
	{
		this.noise = new Noise(seed, octave, amplitude);
	}
	
	public float getNoisePass(float x, float y)
	{
		float n = this.noise.getNoise(x, y, height, inverse);
		return n;
	}
}
