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

import fr.veridiangames.server.ServerMain;
import fr.veridiangames.server.server.NetworkServer;

import static fr.veridiangames.client.Resource.getResource;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;

/**
 * Created by Marc on 24/06/2016.
 */
public class Sound
{
    public static final int AK47_SHOOT = loadWav(getResource("audio/AK47Shoot.wav"));
	public static final int AWP_SHOOT = loadWav(getResource("audio/AWPShoot.wav"));
    public static final int EXPLODE = loadWav(getResource("audio/waterexplode.wav"));
    public static final int IMPACT = loadWav(getResource("audio/impact.wav"));
    public static final int DIGGING = loadWav(getResource("audio/dig.wav"));
    public static final int PLACE = loadWav(getResource("audio/place.wav"));
    public static final int BEEP = loadWav(getResource("audio/beep.wav"));
    public static final int JUMP = loadWav(getResource("audio/jump.wav"));
    public static final int LAND = loadWav(getResource("audio/land.wav"));
    public static final int PLAYER_HIT = loadWav(getResource("audio/playerhit.wav"));
    public static final int[] FOOTSTEP = new int[]{loadWav(getResource("audio/footstep1.wav")),
                                                    loadWav(getResource("audio/footstep2.wav")),
                                                    loadWav(getResource("audio/footstep3.wav")),
                                                    loadWav(getResource("audio/footstep4.wav"))};

    public static int loadWav(String file)
    {
    	if(NetworkServer.getInstance()!= null)
    		return 0;

        int buffer = alGenBuffers();

        AudioData audio = AudioData.create(file);
        alBufferData(buffer, audio.getFormat(), audio.getDataBuffer(), audio.getSamplerate());
        audio.dispose();

        return buffer;
    }
}
