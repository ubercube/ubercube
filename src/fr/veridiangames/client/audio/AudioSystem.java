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

import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL.createCapabilities;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

/**
 * Created by Marc on 24/06/2016.
 */
public class AudioSystem
{
    private static List<Integer> buffers;

    private static long device;
    private static long context;

    public static void init()
    {
        buffers = new ArrayList<>();

        device = alcOpenDevice((ByteBuffer)null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer)null);
        alcMakeContextCurrent(context);
        createCapabilities(deviceCaps);
    }



    /**
     *      A utiliser sinon OpenAL gueule à la fin
     */
    public static void destroy()
    {
        for (int i = 0; i < buffers.size(); i++)
        {
            alDeleteBuffers(buffers.get(i));
        }
        alcCloseDevice(device);
        alcDestroyContext(context);
    }
}
