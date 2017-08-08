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

import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.primitives.CurvePrimitive;
import fr.veridiangames.client.rendering.guis.primitives.LinePrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.profiler.Profiler;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.opengl.GL11;

import javax.sound.sampled.Line;
import java.util.ArrayList;

/**
 * Created by Aiko on 18/07/2016.
 *
 * TODO: Resize the curve depending on display size
 */
public class ProfilerScreen extends GuiCanvas {
    private GameCore core;
    private Display display;
    private CurvePanel curvePanel;

    private GuiLabel labelRes;

    public ProfilerScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);

        this.core = core;
        this.display = display;

        this.curvePanel = new CurvePanel(this, 10, 10, display.getWidth()-20, display.getHeight() / 4 + 40);
        this.curvePanel.setOrigin(GuiComponent.GuiOrigin.A);

        this.labelRes = new GuiLabel("", 10, display.getHeight() / 2 - 40, 20f);
        this.labelRes.setOrigin(GuiComponent.GuiOrigin.A);
        this.labelRes.setScreenParent(GuiComponent.GuiCorner.LC);
        this.labelRes.setColor(Color4f.WHITE);
        this.labelRes.setDropShadow(2);
        this.labelRes.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));

        this.add(curvePanel);
        this.add(labelRes);

        this.setRendered(false);
    }

    public void update()
    {
        if (display.getInput().getKeyDown(Input.KEY_F3))
        {
            setRendered(!isRendered());
            getParent().setRendered(!isRendered());
        }

        if (!isRendered())
            return;

        super.update();

        if (display.getInput().getKey(Input.KEY_F5))
            Profiler.setResolution(Profiler.getResolution()-1 < 1 ? 1 : Profiler.getResolution()-1);
        else if (display.getInput().getKey(Input.KEY_F6))
            Profiler.setResolution(Profiler.getResolution()+1);

        labelRes.setText("Profiler resolution: " + Profiler.getResolution() + " ticks ");
    }

    private class CurvePanel extends GuiPanel
    {
        private GuiCanvas canvas;

        private ArrayList<LinePrimitive> lines;
        private ArrayList<LinePrimitive> borderLines;
        private ArrayList<Curve> curves = new ArrayList<>();

        protected float xScale = 1000;

        public CurvePanel(GuiCanvas canvas, int x, int y, int w, int h) {
            super(x, y, w, h);

            this.canvas = canvas;
            this.lines = new ArrayList<>();
            this.borderLines = new ArrayList<>();

            processCornerParent();

            this.setColor(new Color4f(0, 0, 0, 0.15f));
        }

        public void update()
        {
            super.update();

            if (Profiler.getProfilers().size() != curves.size())
            {
                curves.clear();

                for (Profiler profiler : Profiler.getProfilers())
                {
                    Curve curve = new Curve(this, profiler);
                    canvas.add(curve.getLabel());
                    curves.add(curve);
                }
            }

            if (Profiler.wasUpdated())
                curves.forEach(Curve::update);
        }

        public void render(GuiShader shader)
        {
            super.render(shader);

            shader.setColor(1, 1, 1, 0.2f);
            for (LinePrimitive line : lines)
                line.render(shader);
            shader.setColor(1, 1, 1, 0.8f);
            for (LinePrimitive line : borderLines)
                line.render(shader);

            GL11.glLineWidth(1f);
            for (Curve curve : curves)
                curve.render(shader);
        }

        protected void processCornerParent()
        {
            x = 10;
            y = 10;
            w = display.getWidth()-20;
            h = display.getHeight()/3+40;

            lines.forEach(LinePrimitive::delete);

            lines.clear();

            for (int i = 0; i < xScale; i+=20)
                lines.add(new LinePrimitive(new Vec3(this.getW() / xScale * i + this.getX(),  this.getH() + this.getY(), 0),
                        new Vec3(this.getW() / xScale * i + this.getX(),  this.getY(),               0)));
            for (int i = 0; i < 100; i+=5)
                lines.add(new LinePrimitive(new Vec3(this.getX(),             this.getH() / 100.0f * i + this.getY(), 0),
                        new Vec3(this.getW()+this.getX(), this.getH() / 100.0f * i + this.getY(), 0)));

            borderLines.forEach(LinePrimitive::delete);

            borderLines.clear();

            borderLines.add(new LinePrimitive(new Vec3(x, y, 0), new Vec3(x+w, y, 0)));
            borderLines.add(new LinePrimitive(new Vec3(x, y+h, 0), new Vec3(x+w, y+h, 0)));
            borderLines.add(new LinePrimitive(new Vec3(x, 10, 0), new Vec3(x, y+h, 0)));
            borderLines.add(new LinePrimitive(new Vec3(x+w, 10, 0), new Vec3(x+w, y+h, 0)));
        }
    }

    private class Curve
    {
        private CurvePanel curvePanel;
        private Profiler profiler;

        private GuiLabel label;

        private CurvePrimitive curve;
        private ArrayList<Vec3> points;

        public Curve(CurvePanel curvePanel, Profiler profiler)
        {
            this.curvePanel = curvePanel;
            this.profiler = profiler;
            this.points = new ArrayList<>();
            this.label = new GuiLabel("", 10, display.getHeight() / 2 + Profiler.getProfilers().indexOf(profiler) * 20, 20f);
            this.label.setOrigin(GuiComponent.GuiOrigin.LC);
            this.label.setScreenParent(GuiComponent.GuiCorner.LC);
            this.label.setColor(profiler.getColor());
            this.label.setDropShadow(2);
            this.label.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        }

        public void update() {
            label.setText(profiler.getName() + ": " + profiler.getPercentage() + "% (" + profiler.getElapsed() + "ms)");

            if (points.size() >= curvePanel.xScale)
                points.remove(0);

            for (int i = 0; i < curvePanel.xScale-1; i++)
            {
                float fx = curvePanel.getW() * 2.0f / curvePanel.xScale * i + curvePanel.getX();
                float fy;

                if (i == points.size()-1 || points.size() == 0)
                {
                    fy = curvePanel.getH() - (curvePanel.getH() / 100.0f * profiler.getPercentage()) + curvePanel.getY();
                    points.add(new Vec3(fx, fy, 0));
                    break;
                }
                else if (points.size() == curvePanel.xScale - 1)
                {
                    points.get(i).x = fx;
                    points.get(i).y = points.get(i+1).y;
                }
            }

            if (curve != null)
                curve.delete();

            curve = new CurvePrimitive(points);
        }


        public void render(GuiShader shader)
        {
            shader.setColor(profiler.getColor().sub(new Color4f(0, 0, 0, 0.2f)));

            if (curve != null)
                curve.render();
        }

        public void dispose() {
            curve.delete();
        }

        public GuiLabel getLabel() {
            return label;
        }
    }
}
