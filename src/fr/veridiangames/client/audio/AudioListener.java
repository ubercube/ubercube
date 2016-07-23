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

import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Marc on 24/06/2016.
 */
public class AudioListener
{
    private static FloatBuffer orientationBuffer;
    private static Transform transform;

    public static void init()
    {
        transform = new Transform();
        orientationBuffer = BufferUtils.createFloatBuffer(6);
        orientationBuffer.put(new float[]{0, 0, 0, 0, 0, 0});
        orientationBuffer.flip();
    }

    public static void setTransform(Transform transform)
    {
        AudioListener.transform = new Transform(transform);

        alListener3f(AL_POSITION, transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
        alListener3f(AL_VELOCITY, 1, 1, 1);

        orientationBuffer.put(0, -transform.getForward().x);
        orientationBuffer.put(1, -transform.getForward().y);
        orientationBuffer.put(2, -transform.getForward().z);
        orientationBuffer.put(3, transform.getUp().x);
        orientationBuffer.put(4, transform.getUp().y);
        orientationBuffer.put(5, transform.getUp().z);

        alListenerfv(AL_ORIENTATION, orientationBuffer);
    }

    public static Transform getTransform()
    {
        return transform;
    }
}
