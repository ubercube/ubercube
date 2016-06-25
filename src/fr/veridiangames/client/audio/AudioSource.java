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

package fr.veridiangames.client.audio;

/**
 * Created by Marc on 24/06/2016.
 */

import fr.veridiangames.core.maths.Vec3;

import static org.lwjgl.openal.AL10.*;

public class AudioSource
{
    private int source;

    public AudioSource()
    {
        source = alGenSources();
    }

    public void play(int buffer)
    {
        alSourceStop(source);
        alSourcei(source, AL_BUFFER, buffer);
        alSourcePlay(source);
    }

    public void delete()
    {
        stop();
        alDeleteSources(source);
    }

    public void pause()
    {
        alSourcePause(source);
    }

    public void stop()
    {
        alSourceStop(source);
    }

    public void setPosition(Vec3 position)
    {
        alSource3f(source, AL_POSITION, position.x, position.y, position.z);
    }

    public void setDirection(Vec3 direction)
    {
        alSource3f(source, AL_VELOCITY, direction.x, direction.y, direction.z);
    }

    public void setLooping(boolean loop)
    {
        alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public boolean isPlaying()
    {
        return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void setVolume(float volume)
    {
        alSourcef(source, AL_GAIN, volume);
    }

    public void setPitch(float pitch)
    {
        alSourcef(source, AL_PITCH, pitch);
    }
}
