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

package fr.veridiangames.client.guis;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.utils.Color4f;

public class TrueTypeFont {

	public IntObject[] charArray = new IntObject[256];
	public Map<Character, IntObject> customChars = new HashMap<Character, IntObject>();
	public boolean antiAlias;
	public int fontSize = 0;
	public int fontHeight = 0;
	public Texture fontTexture;
	public int textureWidth = 512;
	public int textureHeight = 512;
	public java.awt.Font font;
	public FontMetrics fontMetrics;
	public Color4f color = Color4f.WHITE;

	public class IntObject {
		public int width;
		public int height;
		public int storedX;
		public int storedY;
	}

	public TrueTypeFont(java.awt.Font font, boolean antiAlias) {
		this.font = font;
		this.fontSize = font.getSize();
		this.antiAlias = antiAlias;

		this.createSet(null);
	}

	private BufferedImage getFontImage(char ch) {
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		if (this.antiAlias == true)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(this.font);
		this.fontMetrics = g.getFontMetrics();
		int charwidth = this.fontMetrics.charWidth(ch);

		if (charwidth <= 0)
			charwidth = 1;
		int charheight = this.fontMetrics.getHeight();
		if (charheight <= 0)
			charheight = this.fontSize;

		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if (this.antiAlias == true)
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		gt.setFont(this.font);

		gt.setColor(Color.WHITE);
		int charx = 0;
		int chary = 0;
		gt.drawString(String.valueOf(ch), (charx), (chary)
				+ this.fontMetrics.getAscent());

		return fontImage;

	}

	private void createSet( char[] customCharsArray ) {
		if	(customCharsArray != null && customCharsArray.length > 0)
			this.textureWidth *= 2;

		BufferedImage imgTemp = new BufferedImage(this.textureWidth, this.textureHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) imgTemp.getGraphics();

		g.setColor(new Color(255,255,255,1));
		g.fillRect(0,0,this.textureWidth,this.textureHeight);

		int rowHeight = 0;
		int positionX = 0;
		int positionY = 0;

		int customCharsLength = ( customCharsArray != null ) ? customCharsArray.length : 0;

		for (int i = 0; i < 256 + customCharsLength; i++) {
			char ch = ( i < 256 ) ? (char) i : customCharsArray[i-256];

			BufferedImage fontImage = this.getFontImage(ch);

			IntObject newIntObject = new IntObject();

			newIntObject.width = fontImage.getWidth();
			newIntObject.height = fontImage.getHeight();

			if (positionX + newIntObject.width >= this.textureWidth) {
				positionX = 0;
				positionY += rowHeight;
				rowHeight = 0;
			}

			newIntObject.storedX = positionX;
			newIntObject.storedY = positionY;

			if (newIntObject.height > this.fontHeight)
				this.fontHeight = newIntObject.height;

			if (newIntObject.height > rowHeight)
				rowHeight = newIntObject.height;

			g.drawImage(fontImage, positionX, positionY, null);

			positionX += newIntObject.width;

			if( i < 256 )
				this.charArray[i] = newIntObject;
			else
				this.customChars.put( new Character( ch ), newIntObject );

			fontImage = null;
		}

		this.fontTexture = TextureLoader.getTexture(this.font.toString(), imgTemp);
	}

	public int getWidth(String whatchars) {
		int totalwidth = 0;
		IntObject intObject = null;
		int currentChar = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			currentChar = whatchars.charAt(i);
			if (currentChar < 256)
				intObject = this.charArray[currentChar];
			else
				intObject = this.customChars.get( new Character( (char) currentChar ) );

			if( intObject != null )
				totalwidth += intObject.width;
		}
		return totalwidth;
	}

	public int getHeight() {
		return this.fontHeight;
	}
}