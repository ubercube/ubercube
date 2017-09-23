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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.main.commands.CommandExecutor;
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

import java.util.ArrayList;
import java.util.List;

import static fr.veridiangames.core.maths.Mathf.ceil;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marc on 13/07/2016.
 */
public class ConsoleScreen extends GuiCanvas
{
    private static final TrueTypeFont FONT = new TrueTypeFont(StaticFont.Kroftsmann(0, 16), true);
    private GameCore core;

    private boolean console;
    private GuiPanel bg;
    private GuiTextBox write;
    private String message;
    private List<CLine> messageList;
    private int x, y;
    private int w, h;
    private float yScroll;
    private CommandExecutor ce = new CommandExecutor();

    public ConsoleScreen(GuiCanvas parent, Display display, GameCore core, int x, int y, int w, int h)
    {
        super(parent);
        this.core = core;
        this.messageList = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        bg = new GuiPanel(x, y - 36, w, h);
        bg.setColor(new Color4f(0, 0, 0, 0.35f));
        bg.setScreenParent(GuiComponent.GuiCorner.BL);
        bg.setOrigin(GuiComponent.GuiOrigin.D);
        super.add(bg);

        write = new GuiTextBox(x, y + 1, w, 256);
        write.setOrigin(GuiComponent.GuiOrigin.D);
        write.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(write);
    }

    public void update()
    {
        super.update();

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_T) || Display.getInstance().getInput().getKeyDown(Input.KEY_ENTER))
        {
            console = true;
            Display.getInstance().getInput().getMouse().setGrabbed(false);
            write.setFocused(true);
            yScroll = 0;
        }
        if (Display.getInstance().getInput().getKeyDown(Input.KEY_ESCAPE))
        {
            console = false;
            Display.getInstance().getInput().getMouse().setGrabbed(true);
            write.setFocused(false);
        }
        Ubercube.getInstance().setInConsole(console);
        bg.setUseable(console);
        write.setRendered(console);

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_ENTER) && console && write.getText().length() > 0)
        {
			if(ce.isCommand(write.getText()))
			{
				ce.exec(write.getText());
			}
			else
			{
				Ubercube.getInstance().getNet().send(new TchatMsgPacket(core.getGame().getPlayer().getName() + ": " + write.getText()), Protocol.TCP);
			}

            console = false;
            Display.getInstance().getInput().getMouse().setGrabbed(true);
            write.clear();
        }

        if (console)
        {
            yScroll += Display.getInstance().getInput().getMouse().getDWheel() * 30f;
            if (yScroll < 0) yScroll = 0;
        }
        else
            yScroll = 0;

        int offset = 0;
        for (int i = messageList.size() - 1; i >= 0; i--)
        {
            CLine msg = messageList.get(i);
            offset += msg.height;
            msg.update(x + 5, bg.getY() + bg.getH() - offset * 16 + (int) yScroll - 5, console);
        }
    }

    public void render(GuiShader shader)
    {
        if (console)
        {
            glEnable(GL_SCISSOR_TEST);
            glScissor(bg.getX() + 4, Display.getInstance().getHeight() - bg.getY() - bg.getH() + 4, bg.getW() - 8, bg.getH() - 8);
        }
        for (int i = 0; i < messageList.size(); i++)
        {
            CLine msg = messageList.get(i);
            msg.render(shader);
        }
        if (console)
        {
            glDisable(GL_SCISSOR_TEST);
        }
    }

    public void log(String msg)
    {
        messageList.add(new CLine(msg, Color4f.WHITE));
    }

    class CLine
    {
        String msg;
        Color4f color;
        int time;
        List<FontRenderer> renderers;
        int x, y;
        boolean init = false;
        int height;
		final int maxWidth = w;

        public CLine(String msg, Color4f color)
        {
            this.msg = msg;
            this.height = 1;
            this.color = new Color4f(color);
            this.time = 0;
            this.x = x;
            this.y = y;
        }

        void init()
        {
        	this.renderers = new ArrayList<>();
        	FontRenderer renderer = new FontRenderer(FONT, msg, 0, 0);
			this.height = (int) ceil((float)renderer.getWidth() / (float)(maxWidth - 40));
        	int lastPos = 0;
        	int width = 0;
        	System.out.println("HEIGHT: " + height + "    " + renderer.getWidth() + "     " + maxWidth);
        	if (height == 1)
			{
				renderers.add(renderer);
				return;
			}
        	for (int i = 0; i < msg.length(); i++)
			{
				width += renderer.getCharData(msg.charAt(i)).width;
				if (width >= maxWidth - 40 || i == msg.length() - 1)
				{
					String a = this.msg.substring(lastPos, i + 1);
					if (a.isEmpty())
						continue;
					renderers.add(new FontRenderer(FONT, a, 0, 0));
					width = 0;
					lastPos = i + 1;
				}
			}
        }

        void update(int x, int y, boolean console)
        {
            if (!init)
            {
                init();
                init = true;
            }
            this.x = x;
            this.y = y;
            time++;
            if (!console)
            {
                float alpha = 1.0f - (time - 1100.0f) / 100.0f;
                if (alpha > 1) alpha = 1;
                if (alpha < 0) alpha = 0;
                color.setAlpha(alpha);
            }
            else
            {
                color.setAlpha(1.0f);
            }
        }

        void render(GuiShader shader)
        {
            if (renderers == null)
                return;
            for (int i = 0; i < renderers.size(); i++)
			{
				FontRenderer renderer = renderers.get(i);
				renderer.setPosition(x, y + i * 16);
				renderer.render(shader, color, 2);
			}
        }

        boolean isRemoved()
        {
            return time > 1200;
        }
    }
}
