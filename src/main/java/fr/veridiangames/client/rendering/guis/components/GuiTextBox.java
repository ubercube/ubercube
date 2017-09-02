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

package fr.veridiangames.client.rendering.guis.components;

import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Log;

public class GuiTextBox extends GuiComponent
{
	private boolean firstLoop = true;
	private Color4f selectionColor;
	private String text = "";
	GuiLabel label;
	private float textWidth;
	private int maxChars;
	private boolean focused = false;
	private int caretPosition;
	private int caretDistance;
	private boolean showCaret;

	private int selectionStart;
	private int selectionEnd;

	private int selectionStartDistance;
	private int selectionEndDistance;
	private int selectionWidth;

	public GuiTextBox(int x, int y, int w, int maxChars) {
		super(x, y, w, 30, new Color4f(0, 0, 0, 0.35f));
		this.maxChars = maxChars;
		selectionColor = new Color4f(0, 0.5f, 1, 0.5f);
		
		label = new GuiLabel("", x, y, StaticFont.Kroftsmann(0, 20));
		label.setColor(Color4f.WHITE);
		label.setDropShadow(2);
		caretPosition = 0;
		caretDistance = 0;
		showCaret = false;
		selectionStart = 2;
		selectionEnd = 4;
	}
	
	int time = 0;
	public void update() {
		if (!super.getCanvas().isRendered())
		{
			firstLoop = true;
			return;
		}
		super.update();
		time++;
		showCaret = false;
		manageKeys();
		
		label.update();
		label.setPosition(x + 6, y + 3);

		if (mouseButtonUp) {
			focused = true;
		}
		if (Display.getInstance().getInput().getMouse().getButtonUp(0) && !mouseIn) {
			focused = false;
		}
		caretDistance = 0;
		for (int i = 0; i < caretPosition; i++) {
			char c = text.charAt(i);
			caretDistance += label.getCharData(c).width;
		}
		label.setText(text);
	}
	
	public void addChar(char c) {
		if (!focused) return;
		if (text != null) if (text.length() >= maxChars) return;
		if (text == null) text = "" + c;
		else
		{
			if (caretDistance == textWidth)
				text += c;
			else
				addCharAt(c, caretPosition);
		}
		textWidth += label.getCharData(c).width;
		caretPosition++;
		showCaret = true;
	}

	public void addCharAt(Character c, int position)
	{
		String a = text.substring(0, position);
		String b = text.substring(position);
		text = a + c.toString() + b;
	}

	public void removeCharAt(int position)
	{
		if (position < 0)
			return;
		String a = text.substring(0, position);
		String b = text.substring(position + 1);
		text = a + b;
	}

	public void removeChar(int offset) {
		if (text == null) return;
		if (text.length() <= 0) return;
		int index = caretPosition - 1 + offset;
		if (index < 0 || index >= text.length())
			return;
		textWidth -= label.getCharData(text.charAt(index)).width;
		removeCharAt(index);
		if (offset == 0)
			caretPosition--;
		showCaret = true;
	}

	public void render(GuiShader shader)
	{
		shader.setColor(color);
		StaticPrimitive.quadPrimitive().render(shader, x +w/2, y +h/2, 0, w/2, h/2, 0);
		if (!focused)
			time = 0;
		label.render(shader);
		if ((time % 60 > 30 && focused) || showCaret) {
			shader.setColor(Color4f.WHITE);
			StaticPrimitive.quadPrimitive().render(shader, x + caretDistance + 8, y + h / 2, 0, 1.5f, 11, 0);
		}
	}

	private void moveCaret(int dir)
	{
		if (dir < 0 && caretPosition == 0)
			return;
		if (dir > 0 && caretPosition == text.length())
			return;
		caretPosition += dir;
		showCaret = true;
	}

	private void manageKeys() {
		Input input = Display.getInstance().getInput();
		int keycode = input.getKeyCode();
		boolean activation = input.getKeyboardCallback().currentKeys.size() == 0 &&
			input.getKeyboardCallback().upKeys.size() == 0 &&
			input.getKeyboardCallback().downKeys.size() == 0 && keycode == 0;

		Log.println("Can use console: " + activation + " is first loop ? " + firstLoop);
		if (firstLoop)
		{
			if (activation)
				firstLoop = false;
			return;
		}
		if (keycode != 0)
			addChar((char) keycode);
		if (getKey(input.KEY_BACKSPACE)) removeChar(0);
		if (getKey(input.KEY_DELETE)) removeChar(1);
		if (getKey(input.KEY_LEFT)) moveCaret(-1);
		if (getKey(input.KEY_RIGHT)) moveCaret(1);
	}

	int keyCode = -1;
	boolean keyDown = false;
	int keyTime = 0;
	boolean keyBurse = false;
	public boolean getKey(int key) {
		Input input = Display.getInstance().getInput();
		if (input.getKeyboardCallback().currentKeys.size() > 2) return false;
		if (input.getKey(key)) {
			if (keyDown) {
				if (keyCode == key) {
					keyTime++;
					if (keyTime > 30) {
						keyBurse = true;
					}
					else return false;
				}
			}
			keyCode = key;
			keyDown = input.getKey(key);
			return keyDown;
		}
		if (keyCode == key) {
			keyDown = false;
			keyCode = -1;
			keyBurse = false;
			keyTime = 0;
		}
		
		return false;
	}

	public void dispose() {
		
	}

	public String getText() {
		return text;
	}

	public void clear() {
		text = "";
		textWidth = 0;
		label.setText("");
		caretPosition = 0;
		caretDistance = 0;
	}

	public boolean isFocused()
	{
		return focused;
	}

	public void setFocused(boolean focused)
	{
		this.focused = focused;
	}
}