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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.utils.Color4f;

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
        super.add(this.bg);
    }

    int time = 0;
    @Override
	public void update()
    {
        if (!this.parent.isRendered())
            return;

        super.update();

        this.rendered = false;
        if (Display.getInstance().getInput().getKey(Input.KEY_TAB))
            this.rendered = true;

        this.bg.setUseable(this.rendered);

        if (!this.rendered)
        {
            this.time = 0;
            return;
        }

        if (this.time % 60 == 0)
        {
            EntityManager entityManager = this.core.getGame().getEntityManager();
            for (int i = 0; i < entityManager.getPlayerEntites().size(); i++)
            {
                Player player = (Player) entityManager.get(entityManager.getPlayerEntites().get(i));
                player.getName();
                int ping = player.getPing();
                if (!this.players.containsKey(player.getID()))
					this.players.put(player.getID(), new PLine(player, this.bg, i));
				else
					this.players.get(player.getID()).update(this.bg.getX(), this.bg.getY() + i * 25, ping, this.bg);
            }
            for (Map.Entry<Integer, PLine> entry : new HashMap<>(this.players).entrySet())
            {
                int id = entry.getKey();
                if (!entityManager.getEntities().containsKey(id))
                    this.players.remove(id);
            }
            this.time = 0;
        }

        this.time++;
    }

    @Override
	public void render(GuiShader shader)
    {
        if (!this.rendered)
            return;

        for (Map.Entry<Integer, PLine> entry : this.players.entrySet())
			entry.getValue().render(shader);
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
            this.nameLabel = new FontRenderer(FONT, this.name, this.x + 5, this.y + 2);
            this.pingLabel = new FontRenderer(FONT, this.ping + " ms", this.x + parent.getW() - 5, this.y + 2);
            this.pingLabel.setPosition(this.x + parent.getW() - 5 - this.pingLabel.getWidth(), this.y + 2);
            this.pingColor = new Color4f(0f, 1f, 0f, 1f);
            this.nameColor = Color4f.WHITE;
            if (player.getTeam() != null)
                this.nameColor = player.getTeam().getColor();
        }

        void update(int x, int y, int ping, GuiComponent parent)
        {
            this.x = x;
            this.y = y;
            this.ping = ping;
            this.nameLabel.setPosition(x + 5, y + 2);

            this.pingLabel.setText(this.ping + " ms");
            this.pingLabel.setPosition(x + parent.getW() - 5 - this.pingLabel.getWidth(), y + 2);

            float r = (this.ping - 50) / 50.0f;
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
            this.nameLabel.render(shader, this.nameColor, 1);
            this.pingLabel.render(shader, this.pingColor, 1);
        }
    }
}
