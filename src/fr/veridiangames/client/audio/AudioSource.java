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
import static sun.misc.Version.println;

public class AudioSource
{
    private boolean destroyed;

    private int source;
    private int soundBuffer;

    private Vec3 position;
    private Vec3 lastPosition;
    private Vec3 velocity;
    private Vec3 lastVelocity;

    private float volume;
    private float lastVolume;
    private float pitch;
    private float lastPitch;
    private float globalVolume;
    private float lastGlobalVolume;
    private boolean loop;
    private boolean lastLoop;

    private boolean playing;
    private boolean paused;
    private boolean stop;

    public void init()
    {
        source = alGenSources();
        int error = alGetError();
        if (error != AL_NO_ERROR)
        {
            System.out.println(AudioManager.getALErrorString(error));
        }
        System.out.println("New source: " + source);
    }

    public void destroy()
    {
        stop();
        alDeleteSources(source);
        destroyed = true;
    }

    public void update()
    {
        if (position != lastPosition)
        {
            alSource3f(source, AL_POSITION, position.x, position.y, position.z);
            lastPosition = position;
        }
        if (velocity != lastVelocity)
        {
            alSource3f(source, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
            lastVelocity = velocity;
        }
        if (paused)
            pause();

        if (playing)
            play();

        if (stop)
            stop();

        if (volume != lastVolume)
        {
            alSourcef(source, AL_GAIN, volume);
            lastVolume = volume;
        }
        if (pitch != lastPitch)
        {
            alSourcef(source, AL_PITCH, pitch);
            lastPitch = pitch;
        }
        if (globalVolume != lastGlobalVolume)
        {
            alSourcef(source, AL_GAIN, volume * globalVolume);
            lastGlobalVolume = globalVolume;
        }
        if (loop != lastLoop)
        {
            alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
            lastLoop = loop;
        }
    }

    private void play()
    {
        alSourcei(source, AL_BUFFER, soundBuffer);
        alSourcePlay(source);
    }

    private void pause()
    {
        alSourcePause(source);
    }

    private void stop()
    {
        alSourceStop(source);
    }

    public void setPosition(Vec3 position)
    {
        this.position = position;
    }

    public void setVelocity(Vec3 velocity)
    {
        this.velocity = velocity;
    }

    public void setLooping(boolean loop)
    {
        this.loop = loop;
    }

    public boolean isPlaying()
    {
        return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public int getSourceID()
    {
        return source;
    }

    public float getVolume()
    {
        return volume;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getGlobalVolume()
    {
        return globalVolume;
    }

    public void setGlobalVolume(float globalVolume)
    {
        this.globalVolume = globalVolume;
    }

    public int getSound()
    {
        return soundBuffer;
    }

    public void setSound(int sound)
    {
        this.soundBuffer = sound;
    }

    public void setPlaying(boolean playing)
    {
        this.playing = playing;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }

    public boolean isStop()
    {
        return stop;
    }

    public void setStop(boolean stop)
    {
        this.stop = stop;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
    }
}
