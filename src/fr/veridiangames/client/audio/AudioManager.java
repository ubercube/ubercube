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
import fr.veridiangames.core.game.entities.components.ECAudioSource;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 25/06/2016.
 */
public class AudioManager
{
    private static HashMap<Integer, AudioSource> sources;
    private static List<Integer> garbage;

    public static void init()
    {
        AudioSystem.init();
        AudioListener.init();
        sources = new HashMap<>();
        garbage = new ArrayList<>();
    }

    public static boolean addSource(int id, AudioSource source)
    {
        if (sources.containsKey(id))
            return false;
        sources.put(id, source);
        return true;
    }

    public static void setGlobalVolume(float volume)
    {
        for (Map.Entry<Integer, AudioSource> entry : sources.entrySet())
        {
            AudioSource source = entry.getValue();
            source.setGlobalVolume(volume);
        }
    }

    public static void update(GameCore core)
    {
        Map<Integer, Entity> entities = core.getGame().getEntityManager().getEntities();
        List<Integer> keys = core.getGame().getEntityManager().getAudioSourcedEntites();
        for (int i = 0; i < keys.size(); i++)
        {
            Entity e = entities.get(keys.get(i));
            ECAudioSource sourceComp = (ECAudioSource) e.get(EComponent.AUDIO_SOURCE);
            if (!addSource(e.getID(), new AudioSource()))
            {
                AudioSource source = getSource(e.getID());
                ECRender render = (ECRender) e.get(EComponent.RENDER);
                source.setPosition(render.getTransform().getPosition());
                source.setVelocity(render.getTransform().getForward());
                source.setVolume(sourceComp.getVolume());
                source.setPitch(sourceComp.getPitch());
                source.setPlaying(sourceComp.isPlaying());
                source.setPaused(sourceComp.isPaused());
                source.setSound(sourceComp.getSound());
            }
        }
        for (Map.Entry<Integer, AudioSource> entry : sources.entrySet())
        {
            AudioSource source = entry.getValue();
            source.update();
            if (source.isDestroyed())
                garbage.add(entry.getKey());
        }
        for (int i = 0; i < garbage.size(); i++)
        {
            int key = garbage.get(i);
            sources.remove(key);
            garbage.remove(i);
        }
    }

    public static AudioSource getSource(int id)
    {
        return sources.get(id);
    }

    public static HashMap<Integer, AudioSource> getSources()
    {
        return sources;
    }

    public static void destroy()
    {
        for (Map.Entry<Integer, AudioSource> entry : sources.entrySet())
        {
            AudioSource source = entry.getValue();
            source.destroy();
        }
        AudioSystem.destroy();
    }
}
