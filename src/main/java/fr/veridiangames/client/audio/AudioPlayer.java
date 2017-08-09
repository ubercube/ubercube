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

import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.maths.Vec3;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class AudioPlayer
{
    public static boolean muteAudio = false;

    private int source;
    private int sound;
    private Vec3 position;
    private Vec3 velocity;
    private boolean surround;
    private float gain;

    private boolean loop;

    public AudioPlayer(AudioSource audioSource)
    {
        this.source = alGenSources();
        this.sound = audioSource.getSound();
        this.position = audioSource.getPosition();
        this.velocity = audioSource.getVelocity();
        this.loop = audioSource.isLoop();
        this.surround = audioSource.isSurround();
        this.gain = audioSource.getGain();

        if (position == null)
            position = AudioListener.getTransform().getPosition().copy();

        if (velocity == null)
            velocity = new Vec3(1);
        int error = alGetError();
        if (error != AL_NO_ERROR)
        {
            System.out.println(getALErrorString(error));
            System.exit(1);
        }

        alSourcef(source, AL_ROLLOFF_FACTOR, 1);
        alSourcef(source, AL_REFERENCE_DISTANCE, 5);
        alSourcef(source, AL_MAX_DISTANCE, 25);
    }

    public void destroy()
    {
        stop();
        alDeleteSources(source);
    }

    public void update()
    {
        if (surround)
        {
            alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);
            alSource3f(source, AL_POSITION, position.x, position.y, position.z);
            alSource3f(source, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
            alSourcei(source, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
        }
        else
        {
            alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
            alSource3f(source, AL_POSITION, 0.0f, 0.0f, 0.0f);
            alSource3f(source, AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        }
    }

    public void play(float mainGain)
    {
        if (muteAudio)
            return;
        alSourcei(source, AL_BUFFER, sound);
        alSourcef(source, AL_GAIN, gain * mainGain);
        alSourcePlay(source);
    }

    public static String getALErrorString(int err)
    {
        switch (err)
        {
            case AL10.AL_NO_ERROR:
                return "AL_NO_ERROR";
            case AL10.AL_INVALID_NAME:
                return "AL_INVALID_NAME";
            case AL10.AL_INVALID_ENUM:
                return "AL_INVALID_ENUM";
            case AL10.AL_INVALID_VALUE:
                return "AL_INVALID_VALUE";
            case AL10.AL_INVALID_OPERATION:
                return "AL_INVALID_OPERATION";
            case AL10.AL_OUT_OF_MEMORY:
                return "AL_OUT_OF_MEMORY";
            default:
                return "No such error code";
        }
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
}
