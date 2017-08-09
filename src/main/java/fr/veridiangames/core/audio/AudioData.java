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

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

/**
 * Created by Marc on 24/06/2016.
 */

public class AudioData
{
    private int format;
    private int samplerate;
    private int bytes;
    private int bytesPerFrame;
    private ByteBuffer dataBuffer;

    private final AudioInputStream ais;
    private final byte[] data;

    private AudioData(AudioInputStream stream)
    {
        this.ais = stream;

        AudioFormat audioFormat = stream.getFormat();
        this.format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());

        this.samplerate = (int) audioFormat.getSampleRate();
        this.bytesPerFrame = audioFormat.getFrameSize();
        this.bytes = (int) (stream.getFrameLength() * bytesPerFrame);
        this.dataBuffer = BufferUtils.createByteBuffer(bytes);
        this.data = new byte[bytes];

        this.loadData();
    }

    public void dispose()
    {
        try
        {
            ais.close();
            dataBuffer.clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ByteBuffer loadData()
    {
        try
        {
            int readData = ais.read(data, 0, bytes);
            dataBuffer.clear();
            dataBuffer.put(data, 0, readData);
            dataBuffer.flip();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return dataBuffer;
    }


    public static AudioData create(String file)
    {
        InputStream stream = null;
        try
        {
            stream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        if (stream == null)
        {
            throw new RuntimeException("Sound file not found: " + file);
        }
        InputStream bufferedInput = new BufferedInputStream(stream);
        AudioInputStream audioStream = null;
        try
        {
            audioStream = getAudioInputStream(bufferedInput);
        } catch (UnsupportedAudioFileException e)
        {
            System.err.println("UnsupportedAudioFileException");
            e.printStackTrace();
        } catch (IOException e)
        {
            System.err.println("IOException");
            e.printStackTrace();
        }
        AudioData wavStream = new AudioData(audioStream);
        return wavStream;
    }


    private static int getOpenAlFormat(int channels, int bitsPerSample)
    {
        if (channels == 1)
        {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
        }
        else
        {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
        }
    }

    public int getFormat()
    {
        return format;
    }

    public int getSamplerate()
    {
        return samplerate;
    }

    public int getBytes()
    {
        return bytes;
    }

    public int getBytesPerFrame()
    {
        return bytesPerFrame;
    }

    public ByteBuffer getDataBuffer()
    {
        return dataBuffer;
    }

    public AudioInputStream getAis()
    {
        return ais;
    }

    public byte[] getData()
    {
        return data;
    }
}