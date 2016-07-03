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
import fr.veridiangames.client.inputs.Mouse;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;

import java.awt.Font;


public class GuiTextBox extends GuiComponent
{
	private Color4f bgColor;
	private String text = "";
	GuiLabel label;
	private float textWidth;
	private int maxChars;
	private boolean focused = false;
	
	public GuiTextBox(int x, int y, int w, int maxChars) {
		super(x, y, w, 35, Color4f.WHITE);
		this.maxChars = maxChars;
		bgColor = new Color4f(0, 0, 0, 0.5f);
		
		label = new GuiLabel("", x, y, new Font("Arial", 1, 20));
		label.setColor(Color4f.BLACK);
	}
	
	int time = 0;
	public void update() {
		super.update();
		
		time++;
		manageKeys();
		
		label.update();
		label.setPosition(x + 6, y + 6);
		
		if (this.mouseIn || this.focused) {
			bgColor = new Color4f(1, 1, 1, 0.5f);
		}else {
			bgColor = new Color4f(0, 0, 0, 0.5f);			
		}
		
		if (mouseButtonUp) {
			focused = true;
		}
		if (Display.getInstance().getInput().getMouse().getButtonUp(0) && !mouseIn) {
			focused = false;
		}
	}
	
	public void addChar(char c) {
		if (text != null) if (text.length() >= maxChars) return;
		if (text == null) text = "" + c;
		else text += c;
		
		textWidth += label.getCharData(c).width;
		label.setText(text);
	}
	
	public void removeChar() {
		if (text == null) return;
		if (text.length() <= 0) return; 

		int lastCharIndex = text.length() - 1;
		
		textWidth -= label.getCharData(text.charAt(lastCharIndex)).width;
		text = text.substring(0, lastCharIndex);
		label.setText(text);
	}
	
	public void render(GuiShader shader) {
		shader.setColor(bgColor);
		StaticPrimitive.quadPrimitive().render(shader, x +w/2, y +h/2, 0, w/2, h/2, 0);
		shader.setColor(color);
		StaticPrimitive.quadPrimitive().render(shader, x +w/2, y +h/2, 0, w/2 - 4, h/2 - 4, 0);

		if (time % 60 > 30 && focused) {
			shader.setColor(Color4f.BLACK);
			StaticPrimitive.quadPrimitive().render(shader, x + textWidth + 8, y + h / 2, 0, 1.5f, 11, 0);
		}
		
		label.render(shader);
	}
	
	private void manageKeys() {
		Input input = Display.getInstance().getInput();
		if (input.getKey(input.KEY_LEFT_SHIFT) || input.getKey(input.KEY_RIGHT_SHIFT)) {
			if (getKey(input.KEY_A)) {addChar('A'); return;}
			if (getKey(input.KEY_B)) {addChar('B'); return;}
			if (getKey(input.KEY_C)) {addChar('C'); return;}
			if (getKey(input.KEY_D)) {addChar('D'); return;}
			if (getKey(input.KEY_E)) {addChar('E'); return;}
			if (getKey(input.KEY_F)) {addChar('F'); return;}
			if (getKey(input.KEY_G)) {addChar('G'); return;}
			if (getKey(input.KEY_H)) {addChar('H'); return;}
			if (getKey(input.KEY_I)) {addChar('I'); return;}
			if (getKey(input.KEY_J)) {addChar('J'); return;}
			if (getKey(input.KEY_K)) {addChar('K'); return;}
			if (getKey(input.KEY_L)) {addChar('L'); return;}
			if (getKey(input.KEY_M)) {addChar('M'); return;}
			if (getKey(input.KEY_N)) {addChar('N'); return;}
			if (getKey(input.KEY_O)) {addChar('O'); return;}
			if (getKey(input.KEY_P)) {addChar('P'); return;}
			if (getKey(input.KEY_Q)) {addChar('Q'); return;}
			if (getKey(input.KEY_R)) {addChar('R'); return;}
			if (getKey(input.KEY_S)) {addChar('S'); return;}
			if (getKey(input.KEY_T)) {addChar('T'); return;}
			if (getKey(input.KEY_U)) {addChar('U'); return;}
			if (getKey(input.KEY_V)) {addChar('V'); return;}
			if (getKey(input.KEY_W)) {addChar('W'); return;}
			if (getKey(input.KEY_X)) {addChar('X'); return;}
			if (getKey(input.KEY_Y)) {addChar('Y'); return;}
			if (getKey(input.KEY_Z)) {addChar('Z'); return;}
		}else {
			if (getKey(input.KEY_A)) {addChar('a'); return;}
			if (getKey(input.KEY_B)) {addChar('b'); return;}
			if (getKey(input.KEY_C)) {addChar('c'); return;}
			if (getKey(input.KEY_D)) {addChar('d'); return;}
			if (getKey(input.KEY_E)) {addChar('e'); return;}
			if (getKey(input.KEY_F)) {addChar('f'); return;}
			if (getKey(input.KEY_G)) {addChar('g'); return;}
			if (getKey(input.KEY_H)) {addChar('h'); return;}
			if (getKey(input.KEY_I)) {addChar('i'); return;}
			if (getKey(input.KEY_J)) {addChar('j'); return;}
			if (getKey(input.KEY_K)) {addChar('k'); return;}
			if (getKey(input.KEY_L)) {addChar('l'); return;}
			if (getKey(input.KEY_M)) {addChar('m'); return;}
			if (getKey(input.KEY_N)) {addChar('n'); return;}
			if (getKey(input.KEY_O)) {addChar('o'); return;}
			if (getKey(input.KEY_P)) {addChar('p'); return;}
			if (getKey(input.KEY_Q)) {addChar('q'); return;}
			if (getKey(input.KEY_R)) {addChar('r'); return;}
			if (getKey(input.KEY_S)) {addChar('s'); return;}
			if (getKey(input.KEY_T)) {addChar('t'); return;}
			if (getKey(input.KEY_U)) {addChar('u'); return;}
			if (getKey(input.KEY_V)) {addChar('v'); return;}
			if (getKey(input.KEY_W)) {addChar('w'); return;}
			if (getKey(input.KEY_X)) {addChar('x'); return;}
			if (getKey(input.KEY_Y)) {addChar('y'); return;}
			if (getKey(input.KEY_Z)) {addChar('z'); return;}
		}
		
		if (getKey(input.KEY_COMMA)) {addChar(','); return;}
		if (getKey(input.KEY_PERIOD)) {addChar('.'); return;}
		if (getKey(input.KEY_KP_DIVIDE)) {addChar(':'); return;}
		if (getKey(input.KEY_KP_DECIMAL)) {addChar('.'); return;}

		if (getKey(input.KEY_0)) {addChar('0'); return;}
		if (getKey(input.KEY_1)) {addChar('1'); return;}
		if (getKey(input.KEY_2)) {addChar('2'); return;}
		if (getKey(input.KEY_3)) {addChar('3'); return;}
		if (getKey(input.KEY_4)) {addChar('4'); return;}
		if (getKey(input.KEY_5)) {addChar('5'); return;}
		if (getKey(input.KEY_6)) {addChar('6'); return;}
		if (getKey(input.KEY_7)) {addChar('7'); return;}
		if (getKey(input.KEY_8)) {addChar('8'); return;}
		if (getKey(input.KEY_9)) {addChar('9'); return;}
		
		if (getKey(input.KEY_KP_0)) {addChar('0'); return;}
		if (getKey(input.KEY_KP_1)) {addChar('1'); return;}
		if (getKey(input.KEY_KP_2)) {addChar('2'); return;}
		if (getKey(input.KEY_KP_3)) {addChar('3'); return;}
		if (getKey(input.KEY_KP_4)) {addChar('4'); return;}
		if (getKey(input.KEY_KP_5)) {addChar('5'); return;}
		if (getKey(input.KEY_KP_6)) {addChar('6'); return;}
		if (getKey(input.KEY_KP_7)) {addChar('7'); return;}
		if (getKey(input.KEY_KP_8)) {addChar('8'); return;}
		if (getKey(input.KEY_KP_9)) {addChar('9'); return;}
		
		if (getKey(input.KEY_BACKSPACE)) removeChar();
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
	}
}