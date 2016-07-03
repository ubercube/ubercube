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

package fr.veridiangames.core.audio;

import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;

/**
 * Created by Marc on 24/06/2016.
 */
public class Audio
{
    public static final int AK47_BULLET_SHOT = loadWav("res/audio/GunShot3.wav");

    public static int loadWav(String file)
    {
        int buffer = alGenBuffers();

        AudioData audio = AudioData.create(file);
        alBufferData(buffer, audio.getFormat(), audio.getDataBuffer(), audio.getSamplerate());
        audio.dispose();

        return buffer;
    }
}
