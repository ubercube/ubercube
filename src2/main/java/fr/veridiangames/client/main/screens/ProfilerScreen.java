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

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

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

/**
 * Created by Aiko on 18/07/2016.
 *
 * TODO: Resize the curve depending on display size
 */

public class ProfilerScreen extends GuiCanvas {
    private Display display;
    private CurvePanel curvePanel;

    private GuiLabel labelRes;

    public ProfilerScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);

        this.display = display;

        this.curvePanel = new CurvePanel(this, 10, 10, display.getWidth()-20, display.getHeight() / 4 + 40);
        this.curvePanel.setOrigin(GuiComponent.GuiOrigin.A);

        this.labelRes = new GuiLabel("", 10, display.getHeight() / 2 - 40, 20f);
        this.labelRes.setOrigin(GuiComponent.GuiOrigin.A);
        this.labelRes.setScreenParent(GuiComponent.GuiCorner.LC);
        this.labelRes.setColor(Color4f.WHITE);
        this.labelRes.setDropShadow(2);
        this.labelRes.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));

        this.add(this.curvePanel);
        this.add(this.labelRes);

        this.setRendered(false);
    }

    @Override
	public void update()
    {
        if (this.display.getInput().getKeyDown(Input.KEY_F3))
        {
            this.setRendered(!this.isRendered());
            this.getParent().setRendered(!this.isRendered());
        }

        if (!this.isRendered())
            return;

        super.update();

        if (this.display.getInput().getKey(Input.KEY_F5))
            Profiler.setResolution(Profiler.getResolution()-1 < 1 ? 1 : Profiler.getResolution()-1);
        else if (this.display.getInput().getKey(Input.KEY_F6))
            Profiler.setResolution(Profiler.getResolution()+1);

        this.labelRes.setText("Profiler resolution: " + Profiler.getResolution() + " ticks ");
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

            this.processCornerParent();

            this.setColor(new Color4f(0, 0, 0, 0.15f));
        }

        @Override
		public void update()
        {
            super.update();

            if (Profiler.getProfilers().size() != this.curves.size())
            {
                this.curves.clear();

                for (Profiler profiler : Profiler.getProfilers())
                {
                    Curve curve = new Curve(this, profiler);
                    this.canvas.add(curve.getLabel());
                    this.curves.add(curve);
                }
            }

            if (Profiler.wasUpdated())
                this.curves.forEach(Curve::update);
        }

        @Override
		public void render(GuiShader shader)
        {
            super.render(shader);

            shader.setColor(1, 1, 1, 0.2f);
            for (LinePrimitive line : this.lines)
                line.render(shader);
            shader.setColor(1, 1, 1, 0.8f);
            for (LinePrimitive line : this.borderLines)
                line.render(shader);

            GL11.glLineWidth(1f);
            for (Curve curve : this.curves)
                curve.render(shader);
        }

        @Override
		protected void processCornerParent()
        {
            this.x = 10;
            this.y = 10;
            this.w = ProfilerScreen.this.display.getWidth()-20;
            this.h = ProfilerScreen.this.display.getHeight()/3+40;

            this.lines.forEach(LinePrimitive::delete);

            this.lines.clear();

            for (int i = 0; i < this.xScale; i+=20)
                this.lines.add(new LinePrimitive(new Vec3(this.getW() / this.xScale * i + this.getX(),  this.getH() + this.getY(), 0),
                        new Vec3(this.getW() / this.xScale * i + this.getX(),  this.getY(),               0)));
            for (int i = 0; i < 100; i+=5)
                this.lines.add(new LinePrimitive(new Vec3(this.getX(),             this.getH() / 100.0f * i + this.getY(), 0),
                        new Vec3(this.getW()+this.getX(), this.getH() / 100.0f * i + this.getY(), 0)));

            this.borderLines.forEach(LinePrimitive::delete);

            this.borderLines.clear();

            this.borderLines.add(new LinePrimitive(new Vec3(this.x, this.y, 0), new Vec3(this.x+this.w, this.y, 0)));
            this.borderLines.add(new LinePrimitive(new Vec3(this.x, this.y+this.h, 0), new Vec3(this.x+this.w, this.y+this.h, 0)));
            this.borderLines.add(new LinePrimitive(new Vec3(this.x, 10, 0), new Vec3(this.x, this.y+this.h, 0)));
            this.borderLines.add(new LinePrimitive(new Vec3(this.x+this.w, 10, 0), new Vec3(this.x+this.w, this.y+this.h, 0)));
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
            this.label = new GuiLabel("", 10, ProfilerScreen.this.display.getHeight() / 2 + Profiler.getProfilers().indexOf(profiler) * 20, 20f);
            this.label.setOrigin(GuiComponent.GuiOrigin.LC);
            this.label.setScreenParent(GuiComponent.GuiCorner.LC);
            this.label.setColor(profiler.getColor());
            this.label.setDropShadow(2);
            this.label.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        }

        public void update() {
            this.label.setText(this.profiler.getName() + ": " + String.format("%.2f", this.profiler.getPercentage()) + "% (" + String.format("%.2f", this.profiler.getElapsed()) + "ms)");

            if (this.points.size() >= this.curvePanel.xScale)
                this.points.remove(0);

            for (int i = 0; i < this.curvePanel.xScale-1; i++)
            {
                float fx = this.curvePanel.getW() * 2.0f / this.curvePanel.xScale * i + this.curvePanel.getX();
                float fy;

                if (i == this.points.size()-1 || this.points.size() == 0)
                {
                    fy = this.curvePanel.getH() - (this.curvePanel.getH() / 100.0f * this.profiler.getPercentage()) + this.curvePanel.getY();
                    this.points.add(new Vec3(fx, fy, 0));
                    break;
                }
                else if (this.points.size() == this.curvePanel.xScale - 1)
                {
                    this.points.get(i).x = fx;
                    this.points.get(i).y = this.points.get(i+1).y;
                }
            }

            if (this.curve != null)
				this.curve.delete();

            this.curve = new CurvePrimitive(this.points);
        }


        public void render(GuiShader shader)
        {
            shader.setColor(this.profiler.getColor().sub(new Color4f(0, 0, 0, 0.2f)));

            if (this.curve != null)
                this.curve.render();
        }

        public GuiLabel getLabel() {
            return this.label;
        }
    }
}
