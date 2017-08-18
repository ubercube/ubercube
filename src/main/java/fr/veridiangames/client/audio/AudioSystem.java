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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE_CLAMPED;
import static org.lwjgl.openal.ALC10.*;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.AL.createCapabilities;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

/**
 * Created by Marc on 24/06/2016.
 */
public class AudioSystem
{
    private static float mainGain;
    private static long device;
    private static long context;
    private static List<AudioPlayer> audioPlayers;

    public static void init()
    {
        mainGain = 0.05f;

        device = alcOpenDevice((ByteBuffer)null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer)null);
        alcMakeContextCurrent(context);
        createCapabilities(deviceCaps);

        audioPlayers = new ArrayList<>();

        alDistanceModel(AL_LINEAR_DISTANCE_CLAMPED);
    }

    public static void play(int sound)
    {
        AudioPlayer player = new AudioPlayer(new AudioSource(sound));
        player.play(mainGain);
        audioPlayers.add(player);
    }

    public static void play(AudioSource audioSource)
    {
        AudioPlayer player = new AudioPlayer(audioSource);
        player.play(mainGain);
        audioPlayers.add(player);
    }

    public static void update(GameCore core)
    {
        Map<Integer, Entity> entities = core.getGame().getEntityManager().getEntities();
        List<Integer>  audioEntities = core.getGame().getEntityManager().getAudioEntities();
        for (int i = 0; i < audioEntities.size(); i++)
        {
            AudioSource audioSource = (AudioSource) entities.get(audioEntities.get(i));
            play(audioSource);
            core.getGame().getEntityManager().remove(audioEntities.get(i));
        }
        for (int i = 0; i < audioPlayers.size(); i++)
        {
            AudioPlayer player = audioPlayers.get(i);
            player.update();
            if (!player.isPlaying())
            {
                player.destroy();
                audioPlayers.remove(i);
            }
        }
    }

    public static void destroy()
    {
        for (int i = 0; i < audioPlayers.size(); i++)
        {
            audioPlayers.get(i).destroy();
        }
        audioPlayers.clear();

        alcCloseDevice(device);
        alcDestroyContext(context);
    }

    public static void setMainVolume(float v)
    {
        mainGain = v;
    }

    public static float getMainVolume()
    {
        return mainGain;
    }
}
