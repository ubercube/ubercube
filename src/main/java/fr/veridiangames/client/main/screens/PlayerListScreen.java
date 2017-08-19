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

package fr.veridiangames.client.main.screens;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.utils.Color4f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Marc on 13/07/2016.
 */
public class PlayerListScreen extends GuiCanvas
{
    private static final TrueTypeFont FONT = new TrueTypeFont(StaticFont.Kroftsmann(0, 20), true);
    private GameCore core;
    private Map<Integer, PLine> players;

    private GuiPanel bg;
    private boolean rendered;
    private GuiCanvas parent;

    public PlayerListScreen(GuiCanvas parent, Display display, GameCore core, int x, int y)
    {
        super(parent);
        this.core = core;
        this.players = new ConcurrentHashMap<>();
        this.parent = parent;

        this.bg = new GuiPanel(x, y, 200, 300);
        this.bg.setOrigin(GuiComponent.GuiOrigin.CENTER);
        this.bg.setScreenParent(GuiComponent.GuiCorner.TC);
        this.bg.setColor(new Color4f(0, 0, 0, 0.35f));
        super.add(bg);
    }

    int time = 0;
    public void update()
    {
        if (!parent.isRendered())
            return;

        super.update();

        rendered = false;
        if (Display.getInstance().getInput().getKey(Input.KEY_TAB))
            rendered = true;

        bg.setUseable(rendered);

        if (!rendered)
        {
            time = 0;
            return;
        }

        if (time % 60 == 0)
        {
            EntityManager entityManager = core.getGame().getEntityManager();
            for (int i = 0; i < entityManager.getPlayerEntites().size(); i++)
            {
                Player player = (Player) entityManager.get(entityManager.getPlayerEntites().get(i));
                String name = player.getName();
                int ping = player.getPing();
                if (!players.containsKey(player.getID()))
                {
                    players.put(player.getID(), new PLine(player, bg, i));
                }
                else
                {
                    players.get(player.getID()).update(bg.getX(), bg.getY() + i * 25, ping, bg);
                }
            }
            for (Map.Entry<Integer, PLine> entry : new HashMap<>(players).entrySet())
            {
                int id = entry.getKey();
                if (!entityManager.getEntities().containsKey(id))
                    players.remove(id);
            }
            time = 0;
        }

        time++;
    }

    public void render(GuiShader shader)
    {
        if (!rendered)
            return;

        for (Map.Entry<Integer, PLine> entry : players.entrySet())
        {
            entry.getValue().render(shader);
        }
    }

    class PLine
    {
        FontRenderer nameLabel;
        FontRenderer pingLabel;
        String name;
        int ping;
        int x, y;
        Color4f pingColor;
        Color4f nameColor;

        public PLine(Player player, GuiComponent parent, int i)
        {
            this.name = player.getName();
            this.ping = 0;
            this.x = parent.getX();
            this.y = parent.getY() + i * 25;
            this.nameLabel = new FontRenderer(FONT, name, x + 5, y + 2);
            this.pingLabel = new FontRenderer(FONT, ping + " ms", x + parent.getW() - 5, y + 2);
            this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);
            this.pingColor = new Color4f(0f, 1f, 0f, 1f);
            this.nameColor = player.getTeam().getColor();
        }

        void update(int x, int y, int ping, GuiComponent parent)
        {
            this.x = x;
            this.y = y;
            this.ping = ping;
            this.nameLabel.setPosition(x + 5, y + 2);

            this.pingLabel.setText(this.ping + " ms");
            this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);

            float r = (float)(this.ping - 50) / 50.0f;
            float g = 1.0f - r;
            if (r < 0) r = 0;
            if (r > 1) r = 1;
            if (g < 0) g = 0;
            if (g > 1) g = 1;

            this.pingColor.setRed(r);
            this.pingColor.setGreen(g);
        }

        void render(GuiShader shader)
        {
            this.nameLabel.render(shader, nameColor, 1);
            this.pingLabel.render(shader, pingColor, 1);
        }
    }
}
