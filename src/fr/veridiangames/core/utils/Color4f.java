/*
 *   Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.utils;

import java.util.Random;

import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;

public class Color4f {
	public static final Color4f WHITE = new Color4f(1f, 1f, 1f, 1f);
	public static final Color4f NULL = new Color4f(-1f, -1f, -1f, -1f);
	public static final Color4f BLACK = new Color4f(0f, 0f, 0f, 1f);

	public static final Color4f GRAY = new Color4f(.5f, .5f, .5f, 1f);
	public static final Color4f DARK_GRAY = new Color4f(.25f, .25f, .25f, 1f);
	public static final Color4f LIGHT_GRAY = new Color4f(.75f, .75f, .75f, 1f);

	public static final Color4f RED = new Color4f(1f, 0f, 0f, 1f);
	public static final Color4f LIGHT_RED = new Color4f(1f, .5f, .5f, 1f);
	public static final Color4f DARK_RED = new Color4f(.25f, 0f, 0f, 1f);

	public static final Color4f GREEN = new Color4f(0f, 1f, 0f, 1f);
	public static final Color4f LIGHT_GREEN = new Color4f(.5f, 1f, .5f, 1f);
	public static final Color4f DARK_GREEN = new Color4f(0f, 0.5f, 0f, 1f);

	public static final Color4f BLUE = new Color4f(0f, 0f, 1f, 1f);
	public static final Color4f LIGHT_BLUE = new Color4f(.5f, .5f, 1, 1f);
	public static final Color4f DARK_BLUE = new Color4f(0f, 0f, .5f, 1f);

	public static final Color4f CYAN = new Color4f(0f, 1f, 1f, 1f);
	public static final Color4f LIGHT_CYAN = new Color4f(.5f, 1f, 1f, 1f);
	public static final Color4f DARK_CYAN = new Color4f(0f, .5f, .5f, 1f);

	public static final Color4f PINK = new Color4f(1f, 0f, 1f, 1f);
	public static final Color4f LIGHT_PINK = new Color4f(1f, .5f, 1f, 1f);
	public static final Color4f DARK_PINK = new Color4f(.5f, 0f, .5f, 1f);

	public static final Color4f YELLOW = new Color4f(1f, 1f, 0f, 1f);
	public static final Color4f LIGHT_YELLOW = new Color4f(1f, 1f, .5f, 1f);
	public static final Color4f DARK_YELLOW = new Color4f(.5f, .5f, 0f, 1f);

	public static final Color4f ORANGE = new Color4f(1f, .5f, 0f, 1f);
	public static final Color4f LIGHT_ORANGE = new Color4f(1f, .75f, .5f, 1f);
	public static final Color4f DARK_ORANGE = new Color4f(.5f, .25f, 0f, 1f);

	public static final Color4f PURPLE = new Color4f(.5f, 0f, 1f, 1f);
	public static final Color4f LIGHT_PURPLE = new Color4f(.75f, .5f, 1f, 1f);
	public static final Color4f DARK_PURPLE = new Color4f(.25f, 0f, .5f, 1f);

	public static Color4f randomColor() {
		Random rand = new Random();
		return new Color4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	}

	public float r, g, b, a;

	public Color4f(Color4f c) {
		this(c.r, c.g, c.b, c.a);
	}

	public Color4f(int rgb) {
		int R = (rgb & 0xff0000)>>16;
		int G = (rgb & 0xff00)>>8;
		int B = (rgb & 0xff);
		
		this.r = (float) R/255f;
		this.g = (float) G/255f;
		this.b = (float) B/255f;
		this.a = 1;
	}

	public Color4f(int r, int g, int b) {
		this((float) r / 255f, (float) g / 255f, (float) b / 255f, 1f);
	}

	public Color4f(int r, int g, int b, int a) {
		this((float) r / 255f, (float) g / 255f, (float) b / 255f, a / 255f);
	}

	public Color4f(float r, float g, float b) {
		this(r, g, b, 1f);
	}

	public Color4f(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color4f(Vec3 hsl)
	{
		float r, g, b;
		float h = hsl.x;
		float s = hsl.y;
		float l = hsl.z;
		float temp1, temp2;
		float tempr, tempg, tempb;
		
        if(l < 0.5) temp2 = l * (1 + s);
        else temp2 = (l + s) - (l * s);
        temp1 = 2 * l - temp2;
        tempr = h + 1.0f / 3.0f;
        if(tempr > 1) tempr--;
        tempg = h;
        tempb = h - 1.0f / 3.0f;
        if(tempb < 0) tempb++;

        //Red
        if(tempr < 1.0f / 6.0f) r = temp1 + (temp2 - temp1) * 6.0f * tempr;
        else if(tempr < 0.5f) r = temp2;
        else if(tempr < 2.0f / 3.0f) r = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempr) * 6.0f;
        else r = temp1;

        //Green
        if(tempg < 1.0f / 6.0f) g = temp1 + (temp2 - temp1) * 6.0f * tempg;
        else if(tempg < 0.5f) g = temp2;
        else if(tempg < 2.0f / 3.0f) g = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempg) * 6.0f;
        else g = temp1;

        //Blue
        if(tempb < 1.0f / 6.0f) b = temp1 + (temp2 - temp1) * 6.0f * tempb;
        else if(tempb < 0.5f) b = temp2;
        else if(tempb < 2.0f / 3.0f) b = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempb) * 6.0f;
        else b = temp1;
        
        this.r = r;
        this.g = g;
        this.b = b;
	}
	
	public Vec3 getHSL()
	{
		float h, s, l;
		
		float maxColor = Mathf.max(r, Mathf.max(g, b));
	    float minColor = Mathf.min(r, Mathf.min(g, b));
		if (minColor == maxColor)
		{
			h = 0.0f;
			s = 0.0f;
			l = r;
		}
	    else
	    {
	        l = (minColor + maxColor) / 2;

	        if(l < 0.5) s = (maxColor - minColor) / (maxColor + minColor);
	        else s = (maxColor - minColor) / (2.0f - maxColor - minColor);

	        if(r == maxColor) h = (g - b) / (maxColor - minColor);
	        else if(g == maxColor) h = 2.0f + (b - r) / (maxColor - minColor);
	        else h = 4.0f + (r - g) / (maxColor - minColor);

	        h /= 6; //to bring it to a number between 0 and 1
	        if(h < 0) h ++;
	    }
		
		return new Vec3(h, s, l);
	}
	
	public static int getColor(int r, int g, int b, int a)
	{
		return a << 24 | r << 16 | g << 8 | b;
	}

	public static int getColor(float r, float g, float b, float a)
	{
		int rr = (int) (r * 255);
		int gg = (int) (g * 255);
		int bb = (int) (b * 255);
		int aa = (int) (a * 255);
		
		return getColor(rr, gg, bb, aa);
	}
	
	public static Color4f lerp(Color4f ca, Color4f cb, float t) {
		float r = Mathf.lerp(ca.r, cb.r, t);
		float g = Mathf.lerp(ca.g, cb.g, t);
		float b = Mathf.lerp(ca.b, cb.b, t);
		float a = Mathf.lerp(ca.a, cb.a, t);
		
		return new Color4f(r, g, b, a);
	}
	
	public static Color4f cLerp(Color4f ca, Color4f cb, float t) {
		float r = Mathf.cLerp(ca.r, cb.r, t);
		float g = Mathf.cLerp(ca.g, cb.g, t);
		float b = Mathf.cLerp(ca.b, cb.b, t);
		float a = Mathf.cLerp(ca.a, cb.a, t);
		
		return new Color4f(r, g, b, a);
	}
	
	public static Color4f sLerp(Color4f ca, Color4f cb, float t) {
		float r = Mathf.sLerp(ca.r, cb.r, t);
		float g = Mathf.sLerp(ca.g, cb.g, t);
		float b = Mathf.sLerp(ca.b, cb.b, t);
		float a = Mathf.sLerp(ca.a, cb.a, t);
		
		return new Color4f(r, g, b, a);
	}
	
	public static Color4f mix(Color4f ca, Color4f cb, float t) {
		float r = Math.abs(ca.r + (cb.r - ca.r) * t); 
		float g = Math.abs(ca.g + (cb.g - ca.g) * t); 
		float b = Math.abs(ca.b + (cb.b - ca.b) * t); 
		float a = Math.abs(ca.a + (cb.a - ca.a) * t); 
		
		return new Color4f(r, g, b, a);
	}
	
	public float[] toArray() {
		return new float[] { r, g, b, a };
	}
	
	public Vec3 rgb() {
		return new Vec3(r, g, b);
	}
	
	public float getRed() {
		return r;
	}

	public void setRed(float red) {
		this.r = red;
	}

	public float getGreen() {
		return g;
	}

	public void setGreen(float green) {
		this.g = green;
	}

	public float getBlue() {
		return b;
	}

	public void setBlue(float blue) {
		this.b = blue;
	}

	public float getAlpha() {
		return a;
	}

	public void setAlpha(float alpha) {
		this.a = alpha;
	}

	public int getRGB() {
		int R = (int) (r*255f);
		int G = (int) (g*255f);
		int B = (int) (b*255f);
		int rgb = 0;
		
		rgb = R << 16 | G << 8 | B;
		return rgb;
	}
	
	public int getARGB() {
		int A = (int) (a*255f);
		int R = (int) (r*255f);
		int G = (int) (g*255f);
		int B = (int) (b*255f);
		int rgb = 0;
		
		rgb = A << 24 | R << 16 | G << 8 | B;
		return rgb;
	}
	
	public Color4f add(float v) {
		r += v;
		g += v;
		b += v;
		return this;
	}

	public Color4f add(Color4f v) {
		r += v.r;
		g += v.g;
		b += v.b;
		return this;
	}
	
	public Color4f sub(float v) {
		r -= v;
		g -= v;
		b -= v;
		return this;
	}

	public Color4f sub(Color4f v) {
		r -= v.r;
		g -= v.g;
		b -= v.b;
		return this;
	}
	
	public Color4f mul(float v) {
		r *= v;
		g *= v;
		b *= v;
		return this;
	}

	public Color4f mul(Color4f v) {
		r *= v.r;
		g *= v.g;
		b *= v.b;
		return this;
	}
	
	public String toString()
	{
		return r + " " + g + " " + b + " " + a;
	}
	
	public Color4f copy() {
		return new Color4f(r, g, b, a);
	}
	
	public boolean equals(Color4f c) {
		return r == c.r && g == c.g && b == c.b && a == c.a;
	}
}
