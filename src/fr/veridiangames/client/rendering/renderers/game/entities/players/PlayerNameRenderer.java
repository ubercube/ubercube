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

package fr.veridiangames.client.rendering.renderers.game.entities.players;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.renderers.guis.Font3DRenderer;
import fr.veridiangames.client.rendering.shaders.Gui3DShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.modes.Team;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import java.awt.*;
import java.util.*;
import java.util.List;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Marc on 13/06/2016.
 */
public class PlayerNameRenderer
{
    private TrueTypeFont font;
    private List<Font3DRenderer> playerFontRenderers;
    private List<Color4f> colors = new ArrayList<>();

    public PlayerNameRenderer()
    {
        font = new TrueTypeFont(StaticFont.Kroftsmann(0, 30), true);
        playerFontRenderers = new ArrayList<>();
    }

    public void update(Map<Integer, Entity> entities, java.util.List<Integer> indices)
    {
        playerFontRenderers.clear();
        colors.clear();
        for (int i = 0; i < indices.size(); i++)
        {
            int key = indices.get(i);
            Entity e = entities.get(key);
            if (!(e instanceof NetworkedPlayer))
                continue;
            String name = ((Player) e).getName();
            Vec3 position = ((Player) e).getPosition();
            Font3DRenderer renderer = new Font3DRenderer(font, name, position.copy().add(0, 2.3f, 0));
            playerFontRenderers.add(renderer);
            Team t = Ubercube.getInstance().getGameCore().getGame().getGameMode().getPlayerTeam((Player) e);
            if(t != null) {
                colors.add(t.getColor());
            }else{
                colors.add(Color4f.WHITE);
            }
        }
    }

    public void render(Gui3DShader shader, Camera camera)
    {
        for (int i = 0; i < playerFontRenderers.size(); i++)
        {
            playerFontRenderers.get(i).render(shader, camera, colors.get(i), 4);
        }
    }
}
