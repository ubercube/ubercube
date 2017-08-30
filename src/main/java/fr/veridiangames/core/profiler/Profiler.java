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

package fr.veridiangames.core.profiler;

import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;

/**
 * Created by Aiko on 16/07/2016.
 */
public class Profiler {
    private static ArrayList<Profiler> profilers = new ArrayList<>();

    private static int count;
    private static int resolution;
    private static boolean updated;

    static
    {
        count = 0;
        resolution = 1;
        updated = false;
    }

    private String name;
    private long start;
    private float elapsing;
    private float elapsed;
    private float percentage;
    private Color4f color;

    public Profiler(String name, boolean use)
    {
        if (!use)
            return;
        this.name = name;
        profilers.add(this);
        this.color = Color4f.randomColor();
    }

    public Profiler(String name, Color4f color)
    {
        this.name = name;
        profilers.add(this);
        this.color = color;
    }

    public static void updateAll()
    {
        count++;
        updated = false;

        if (count >= resolution)
        {
            count = 0;

            getProfilers().forEach(Profiler::update);

            updated = true;
        }
    }

    public void update()
    {
        if (elapsing == 0)
        {
            percentage = 0;
            elapsed = elapsing;
            return;
        }

        percentage = elapsing * 100.0f / (resolution * (1000.0f / 60.0f));
        elapsed = elapsing;
        elapsing = 0;
    }

    public void start()
    {
        start = System.currentTimeMillis();
    }

    public void end()
    {
        elapsing += System.currentTimeMillis() - start;
    }

    public String getName()
    {
        return name;
    }

    public float getElapsed()
    {
        return elapsed;
    }

    public float getPercentage()
    {
        return percentage;
    }

    public Color4f getColor()
    {
        return color;
    }

    public static boolean wasUpdated()
    {
        return updated;
    }

    public static ArrayList<Profiler> getProfilers()
    {
        return profilers;
    }

    public static void setResolution(int resolution)
    {
        Profiler.resolution = resolution;
    }

    public static int getResolution()
    {
        return resolution;
    }
}
