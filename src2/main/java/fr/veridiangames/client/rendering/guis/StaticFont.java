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

package fr.veridiangames.client.rendering.guis;

import static fr.veridiangames.client.FileManager.getResource;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

import fr.veridiangames.core.utils.Log;

public class StaticFont {
	public static Font square_bold(int style, float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(getResource("fonts/RifficFree-Bold.ttf")));
		} catch (FontFormatException e) {
			Log.exception(e);
		} catch (IOException e) {
			Log.exception(e);
		}

		return font.deriveFont(style, size);
	}

	public static Font Kroftsmann(int style, float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(getResource("fonts/RifficFree-Bold.ttf")));
		} catch (FontFormatException e) {
			Log.exception(e);
		} catch (IOException e) {
			Log.exception(e);
		}

		return font.deriveFont(style, size);
	}

	public static Font HPSimplified_Rg(int style, float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(getResource("fonts/RifficFree-Bold.ttf")));
		} catch (FontFormatException e) {
			Log.exception(e);
		} catch (IOException e) {
			Log.exception(e);
		}

		return font.deriveFont(style, size);
	}
}
