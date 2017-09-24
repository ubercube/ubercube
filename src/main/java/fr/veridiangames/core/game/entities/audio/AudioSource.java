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

package fr.veridiangames.core.game.entities.audio;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marc on 18/07/2016.
 */
public class AudioSource extends Entity
{
    private int sound;
    private Vec3 position;
    private Vec3 velocity;
    private boolean loop;
    private boolean surround;
    private float gain;

    public AudioSource(int source)
    {
        this(source, null, null, false);
        this.surround = false;
    }

    public AudioSource(int source, float gain)
    {
        this(source, null, null, false);
        this.surround = false;
        this.gain = gain;
    }

    public AudioSource(int source, boolean loop)
    {
        this(source, null, null, loop);
        this.surround = false;
    }

    public AudioSource(int source, Vec3 position)
    {
        this(source, position, null, false);
    }

	public AudioSource(int source, Vec3 position, float gain)
	{
		this(source, position, null, false);
		this.gain = gain;
	}

    public AudioSource(int source, Vec3 position, boolean loop)
    {
        this(source, position, null, loop);
    }

    public AudioSource(int sound, Vec3 position, Vec3 velocity, boolean loop)
    {
        super(Indexer.getUniqueID() * (sound + 1));
        this.sound = sound;
        this.position = position;
        this.velocity = velocity;
        this.loop = loop;
        this.surround = true;
        this.gain = 1.0f;
    }

    public int getSound()
    {
        return sound;
    }

    public Vec3 getPosition()
    {
        return position;
    }

    public Vec3 getVelocity()
    {
        return velocity;
    }

    public boolean isLoop()
    {
        return loop;
    }

    public boolean isSurround()
    {
        return surround;
    }

    public float getGain()
    {
        return gain;
    }

    public AudioSource setGain(float gain)
    {
        this.gain = gain;
        return this;
    }
}
