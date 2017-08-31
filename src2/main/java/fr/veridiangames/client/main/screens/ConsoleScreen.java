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

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiTextBox;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.TchatMsgPacket;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marc on 13/07/2016.
 */
public class ConsoleScreen extends GuiCanvas
{
    private static final TrueTypeFont FONT = new TrueTypeFont(StaticFont.Kroftsmann(0, 20), true);
    private GameCore core;

    private boolean console;
    private GuiPanel bg;
    private GuiTextBox write;
    private List<CLine> messageList;
    private int x;
    private float yScroll;

    public ConsoleScreen(GuiCanvas parent, Display display, GameCore core, int x, int y, int w, int h)
    {
        super(parent);
        this.core = core;
        this.messageList = new ArrayList<>();
        this.x = x;
        this.bg = new GuiPanel(x, y - 36, w, h);
        this.bg.setColor(new Color4f(0, 0, 0, 0.35f));
        this.bg.setScreenParent(GuiComponent.GuiCorner.BL);
        this.bg.setOrigin(GuiComponent.GuiOrigin.D);
        super.add(this.bg);

        this.write = new GuiTextBox(x, y + 1, w, 64 - 15);
        this.write.setOrigin(GuiComponent.GuiOrigin.D);
        this.write.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(this.write);
    }

    @Override
	public void update()
    {
        super.update();

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_T) ||
                Display.getInstance().getInput().getKeyDown(Input.KEY_ENTER))
        {
            this.console = true;
            Display.getInstance().getInput().getMouse().setGrabbed(false);
            this.write.setFocused(true);
            this.yScroll = 0;
        }
        if (Display.getInstance().getInput().getKeyDown(Input.KEY_ESCAPE))
        {
            this.console = false;
            Display.getInstance().getInput().getMouse().setGrabbed(true);
            this.write.setFocused(false);
        }
        Ubercube.getInstance().setInConsole(this.console);
        this.bg.setUseable(this.console);
        this.write.setUseable(this.console);

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_ENTER) && this.console && this.write.getText().length() > 0)
        {
            Ubercube.getInstance().getNet().send(new TchatMsgPacket(this.core.getGame().getPlayer().getName() + ": " + this.write.getText()), Protocol.TCP);
            this.console = false;
            Display.getInstance().getInput().getMouse().setGrabbed(true);
            this.write.clear();
        }

        if (this.console)
        {
            this.yScroll += Display.getInstance().getInput().getMouse().getDWheel() * 30f;
            if (this.yScroll < 0) this.yScroll = 0;
        }
        else
            this.yScroll = 0;

        for (int i = 0; i < this.messageList.size(); i++)
        {
            CLine msg = this.messageList.get(i);
            msg.update(this.x + 5, this.bg.getY() + this.bg.getH() - 16 - (this.messageList.size() - 1) * 20 + i * 20 + (int) this.yScroll - 5, this.console);
        }
    }

    @Override
	public void render(GuiShader shader)
    {
        if (this.console)
        {
            glEnable(GL_SCISSOR_TEST);
            glScissor(this.bg.getX(), Display.getInstance().getHeight() - this.bg.getY() - this.bg.getH(), this.bg.getW(), this.bg.getH());
        }
        for (int i = 0; i < this.messageList.size(); i++)
        {
            CLine msg = this.messageList.get(i);
            msg.render(shader);
        }
        if (this.console)
			glDisable(GL_SCISSOR_TEST);
    }

    public void log(String msg)
    {
        this.messageList.add(new CLine(msg, Color4f.WHITE));
    }

    class CLine
    {
        String msg;
        Color4f color;
        int time;
        FontRenderer renderer;
        int x, y;
        boolean init = false;

        public CLine(String msg, Color4f color)
        {
            this.msg = msg;
            this.color = new Color4f(color);
            this.time = 0;
        }

        void init()
        {
            this.renderer = new FontRenderer(FONT, this.msg, 0, 0);
        }

        void update(int x, int y, boolean console)
        {
            if (!this.init)
            {
                this.init();
                this.init = true;
            }
            this.x = x;
            this.y = y;
            this.time++;
            if (!console)
            {
                float alpha = 1.0f - (this.time - 1100.0f) / 100.0f;
                if (alpha > 1) alpha = 1;
                if (alpha < 0) alpha = 0;
                this.color.setAlpha(alpha);
            } else
				this.color.setAlpha(1.0f);

        }

        void render(GuiShader shader)
        {
            if (this.renderer == null)
                return;
            this.renderer.setPosition(this.x, this.y);
            this.renderer.render(shader, this.color, 2);
        }

        boolean isRemoved()
        {
            return this.time > 1200;
        }
    }
}
