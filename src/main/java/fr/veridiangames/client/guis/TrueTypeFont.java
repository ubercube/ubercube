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

import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;

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

		createSet(null);
	}
	
	private BufferedImage getFontImage(char ch) {
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		if (antiAlias == true) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(font);
		fontMetrics = g.getFontMetrics();
		int charwidth = fontMetrics.charWidth(ch);

		if (charwidth <= 0) {
			charwidth = 1;
		}
		int charheight = fontMetrics.getHeight();
		if (charheight <= 0) {
			charheight = fontSize;
		}

		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if (antiAlias == true) {
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		gt.setFont(font);

		gt.setColor(Color.WHITE);
		int charx = 0;
		int chary = 0;
		gt.drawString(String.valueOf(ch), (charx), (chary)
				+ fontMetrics.getAscent());

		return fontImage;

	}

	private void createSet( char[] customCharsArray ) {	
		if	(customCharsArray != null && customCharsArray.length > 0) {
			textureWidth *= 2;
		}

		BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) imgTemp.getGraphics();

		g.setColor(new Color(255,255,255,1));
		g.fillRect(0,0,textureWidth,textureHeight);
		
		int rowHeight = 0;
		int positionX = 0;
		int positionY = 0;
		
		int customCharsLength = ( customCharsArray != null ) ? customCharsArray.length : 0; 

		for (int i = 0; i < 256 + customCharsLength; i++) {
			char ch = ( i < 256 ) ? (char) i : customCharsArray[i-256];
			
			BufferedImage fontImage = getFontImage(ch);

			IntObject newIntObject = new IntObject();

			newIntObject.width = fontImage.getWidth();
			newIntObject.height = fontImage.getHeight();

			if (positionX + newIntObject.width >= textureWidth) {
				positionX = 0;
				positionY += rowHeight;
				rowHeight = 0;
			}

			newIntObject.storedX = positionX;
			newIntObject.storedY = positionY;

			if (newIntObject.height > fontHeight) {
				fontHeight = newIntObject.height;
			}

			if (newIntObject.height > rowHeight) {
				rowHeight = newIntObject.height;
			}

			g.drawImage(fontImage, positionX, positionY, null);

			positionX += newIntObject.width;

			if( i < 256 ) {
				charArray[i] = newIntObject;
			} else {
				customChars.put( new Character( ch ), newIntObject );
			}

			fontImage = null;
		}

		fontTexture = TextureLoader.getTexture(font.toString(), imgTemp);
	}

	public int getWidth(String whatchars) {
		int totalwidth = 0;
		IntObject intObject = null;
		int currentChar = 0;
		for (int i = 0; i < whatchars.length(); i++) {
			currentChar = whatchars.charAt(i);
			if (currentChar < 256) {
				intObject = charArray[currentChar];
			} else {
				intObject = (IntObject)customChars.get( new Character( (char) currentChar ) );
			}
			
			if( intObject != null )
				totalwidth += intObject.width;
		}
		return totalwidth;
	}

	public int getHeight() {
		return fontHeight;
	}
}