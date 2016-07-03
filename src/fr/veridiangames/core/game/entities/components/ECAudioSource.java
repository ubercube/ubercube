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

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 14 f�vr. 2016.
 */
public class ECAudioSource extends EComponent
{
	private Vec3 position;
	private Vec3 velocity;

	private float volume;
	private float pitch;
	private boolean loop;

	private int sound;

	private boolean playing;
	private boolean paused;

	public ECAudioSource()
	{
		super(AUDIO_SOURCE);
	}

	public void play()
	{
		this.playing = true;
	}

	public void stop()
	{
		this.playing = false;
	}

	public void paused()
	{
		this.paused = true;
	}

	public float getVolume()
	{
		return volume;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public float getPitch()
	{
		return pitch;
	}

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

	public boolean isLoop()
	{
		return loop;
	}

	public void setLoop(boolean loop)
	{
		this.loop = loop;
	}

	public int getSound()
	{
		return sound;
	}

	public void setSound(int sound)
	{
		this.sound = sound;
	}


	public boolean isPlaying()
	{
		return playing;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public Vec3 getPosition()
	{
		return position;
	}

	public void setPosition(Vec3 position)
	{
		this.position = position;
	}

	public Vec3 getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vec3 velocity)
	{
		this.velocity = velocity;
	}
}