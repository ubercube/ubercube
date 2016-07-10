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

package fr.veridiangames.core.utils;

import java.io.*;

/**
 * Created by Marc on 09/07/2016.
 */
public class DataStream
{
    public static void write(OutputStream out, byte[] bytes) throws IOException
    {
        DataOutputStream dout = new DataOutputStream(out);
        dout.writeInt(bytes.length);
        dout.write(bytes);
    }

    public static byte[] read(InputStream in) throws IOException
    {
        DataInputStream din = new DataInputStream(in);
        int length = din.readInt();
        byte[] bytes = new byte[length];
        din.readFully(bytes, 0, bytes.length);
        return bytes;
    }
}
