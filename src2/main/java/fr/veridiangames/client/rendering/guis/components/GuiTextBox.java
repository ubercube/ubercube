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


public class GuiTextBox extends GuiComponent
{
	private String text = "";
	GuiLabel label;
	private float textWidth;
	private int maxChars;
	private boolean focused = false;

	public GuiTextBox(int x, int y, int w, int maxChars) {
		super(x, y, w, 30, new Color4f(0, 0, 0, 0.35f));
		this.maxChars = maxChars;

		this.label = new GuiLabel("", x, y, StaticFont.Kroftsmann(0, 20));
		this.label.setColor(Color4f.WHITE);
		this.label.setDropShadow(2);
	}

	int time = 0;
	@Override
	public void update() {
		super.update();

		this.time++;
		this.manageKeys();

		this.label.update();
		this.label.setPosition(this.x + 6, this.y + 6);

		if (this.mouseButtonUp)
			this.focused = true;
		if (Display.getInstance().getInput().getMouse().getButtonUp(0) && !this.mouseIn)
			this.focused = false;
	}

	public void addChar(char c) {
		if (!this.focused) return;
		if (this.text != null) if (this.text.length() >= this.maxChars) return;
		if (this.text == null) this.text = "" + c;
		else this.text += c;

		this.textWidth += this.label.getCharData(c).width;
		this.label.setText(this.text);
	}

	public void removeChar() {
		if (this.text == null) return;
		if (this.text.length() <= 0) return;

		int lastCharIndex = this.text.length() - 1;

		this.textWidth -= this.label.getCharData(this.text.charAt(lastCharIndex)).width;
		this.text = this.text.substring(0, lastCharIndex);
		this.label.setText(this.text);
	}

	@Override
	public void render(GuiShader shader) {
//		shader.setColor(bgColor);
//		StaticPrimitive.quadPrimitive().render(shader, x +w/2, y +h/2, 0, w/2, h/2, 0);
		shader.setColor(this.color);
		StaticPrimitive.quadPrimitive().render(shader, this.x +this.w/2, this.y +this.h/2, 0, this.w/2, this.h/2, 0);
		if (!this.focused)
			this.time = 0;

		if (this.time % 60 > 30 && this.focused) {
			shader.setColor(Color4f.WHITE);
			StaticPrimitive.quadPrimitive().render(shader, this.x + this.textWidth + 8, this.y + this.h / 2, 0, 1.5f, 11, 0);
		}

		this.label.render(shader);
	}

	private void manageKeys() {
		Input input = Display.getInstance().getInput();
		if (input.getKey(Input.KEY_LEFT_SHIFT) || input.getKey(Input.KEY_RIGHT_SHIFT)) {
			if (this.getKey(Input.KEY_A)) {this.addChar('A'); return;}
			if (this.getKey(Input.KEY_B)) {this.addChar('B'); return;}
			if (this.getKey(Input.KEY_C)) {this.addChar('C'); return;}
			if (this.getKey(Input.KEY_D)) {this.addChar('D'); return;}
			if (this.getKey(Input.KEY_E)) {this.addChar('E'); return;}
			if (this.getKey(Input.KEY_F)) {this.addChar('F'); return;}
			if (this.getKey(Input.KEY_G)) {this.addChar('G'); return;}
			if (this.getKey(Input.KEY_H)) {this.addChar('H'); return;}
			if (this.getKey(Input.KEY_I)) {this.addChar('I'); return;}
			if (this.getKey(Input.KEY_J)) {this.addChar('J'); return;}
			if (this.getKey(Input.KEY_K)) {this.addChar('K'); return;}
			if (this.getKey(Input.KEY_L)) {this.addChar('L'); return;}
			if (this.getKey(Input.KEY_M)) {this.addChar('M'); return;}
			if (this.getKey(Input.KEY_N)) {this.addChar('N'); return;}
			if (this.getKey(Input.KEY_O)) {this.addChar('O'); return;}
			if (this.getKey(Input.KEY_P)) {this.addChar('P'); return;}
			if (this.getKey(Input.KEY_Q)) {this.addChar('Q'); return;}
			if (this.getKey(Input.KEY_R)) {this.addChar('R'); return;}
			if (this.getKey(Input.KEY_S)) {this.addChar('S'); return;}
			if (this.getKey(Input.KEY_T)) {this.addChar('T'); return;}
			if (this.getKey(Input.KEY_U)) {this.addChar('U'); return;}
			if (this.getKey(Input.KEY_V)) {this.addChar('V'); return;}
			if (this.getKey(Input.KEY_W)) {this.addChar('W'); return;}
			if (this.getKey(Input.KEY_X)) {this.addChar('X'); return;}
			if (this.getKey(Input.KEY_Y)) {this.addChar('Y'); return;}
			if (this.getKey(Input.KEY_Z)) {this.addChar('Z'); return;}
		}else {
			if (this.getKey(Input.KEY_A)) {this.addChar('a'); return;}
			if (this.getKey(Input.KEY_B)) {this.addChar('b'); return;}
			if (this.getKey(Input.KEY_C)) {this.addChar('c'); return;}
			if (this.getKey(Input.KEY_D)) {this.addChar('d'); return;}
			if (this.getKey(Input.KEY_E)) {this.addChar('e'); return;}
			if (this.getKey(Input.KEY_F)) {this.addChar('f'); return;}
			if (this.getKey(Input.KEY_G)) {this.addChar('g'); return;}
			if (this.getKey(Input.KEY_H)) {this.addChar('h'); return;}
			if (this.getKey(Input.KEY_I)) {this.addChar('i'); return;}
			if (this.getKey(Input.KEY_J)) {this.addChar('j'); return;}
			if (this.getKey(Input.KEY_K)) {this.addChar('k'); return;}
			if (this.getKey(Input.KEY_L)) {this.addChar('l'); return;}
			if (this.getKey(Input.KEY_M)) {this.addChar('m'); return;}
			if (this.getKey(Input.KEY_N)) {this.addChar('n'); return;}
			if (this.getKey(Input.KEY_O)) {this.addChar('o'); return;}
			if (this.getKey(Input.KEY_P)) {this.addChar('p'); return;}
			if (this.getKey(Input.KEY_Q)) {this.addChar('q'); return;}
			if (this.getKey(Input.KEY_R)) {this.addChar('r'); return;}
			if (this.getKey(Input.KEY_S)) {this.addChar('s'); return;}
			if (this.getKey(Input.KEY_T)) {this.addChar('t'); return;}
			if (this.getKey(Input.KEY_U)) {this.addChar('u'); return;}
			if (this.getKey(Input.KEY_V)) {this.addChar('v'); return;}
			if (this.getKey(Input.KEY_W)) {this.addChar('w'); return;}
			if (this.getKey(Input.KEY_X)) {this.addChar('x'); return;}
			if (this.getKey(Input.KEY_Y)) {this.addChar('y'); return;}
			if (this.getKey(Input.KEY_Z)) {this.addChar('z'); return;}
		}

		if (this.getKey(Input.KEY_COMMA) && input.getKey(Input.KEY_RIGHT_SHIFT)) {this.addChar('?'); return;}
		else if (this.getKey(Input.KEY_COMMA)) {this.addChar(','); return;}

		if (this.getKey(Input.KEY_PERIOD)) {this.addChar('.'); return;}
		if (this.getKey(Input.KEY_KP_DIVIDE)) {this.addChar(':'); return;}
		if (this.getKey(Input.KEY_KP_DECIMAL)) {this.addChar('.'); return;}
		if (this.getKey(Input.KEY_SPACE)) {this.addChar(' '); return;}

		if (this.getKey(Input.KEY_0)) {this.addChar('0'); return;}
		if (this.getKey(Input.KEY_1)) {this.addChar('1'); return;}
		if (this.getKey(Input.KEY_2)) {this.addChar('2'); return;}
		if (this.getKey(Input.KEY_3)) {this.addChar('3'); return;}
		if (this.getKey(Input.KEY_4)) {this.addChar('4'); return;}
		if (this.getKey(Input.KEY_5)) {this.addChar('5'); return;}
		if (this.getKey(Input.KEY_6)) {this.addChar('6'); return;}
		if (this.getKey(Input.KEY_7)) {this.addChar('7'); return;}
		if (this.getKey(Input.KEY_8)) {this.addChar('8'); return;}
		if (this.getKey(Input.KEY_9)) {this.addChar('9'); return;}

		if (this.getKey(Input.KEY_KP_0)) {this.addChar('0'); return;}
		if (this.getKey(Input.KEY_KP_1)) {this.addChar('1'); return;}
		if (this.getKey(Input.KEY_KP_2)) {this.addChar('2'); return;}
		if (this.getKey(Input.KEY_KP_3)) {this.addChar('3'); return;}
		if (this.getKey(Input.KEY_KP_4)) {this.addChar('4'); return;}
		if (this.getKey(Input.KEY_KP_5)) {this.addChar('5'); return;}
		if (this.getKey(Input.KEY_KP_6)) {this.addChar('6'); return;}
		if (this.getKey(Input.KEY_KP_7)) {this.addChar('7'); return;}
		if (this.getKey(Input.KEY_KP_8)) {this.addChar('8'); return;}
		if (this.getKey(Input.KEY_KP_9)) {this.addChar('9'); return;}

		if (this.getKey(Input.KEY_BACKSPACE)) this.removeChar();
	}

	int keyCode = -1;
	boolean keyDown = false;
	int keyTime = 0;
	boolean keyBurse = false;
	public boolean getKey(int key) {
		Input input = Display.getInstance().getInput();
		if (input.getKeyboardCallback().currentKeys.size() > 2) return false;
		if (input.getKeyDown(key))
			return true;
//		if (input.getKey(key)) {
//			if (keyDown) {
//				if (keyCode == key) {
//					keyTime++;
//					if (keyTime > 30) {
//						keyBurse = true;
//					}
//					else return false;
//				}
//			}
//
//			keyCode = key;
//			keyDown = input.getKey(key);
//
//			return keyDown;
//		}
//		if (keyCode == key) {
//			keyDown = false;
//			keyCode = -1;
//			keyBurse = false;
//			keyTime = 0;
//		}

		return false;
	}

	@Override
	public void dispose() {

	}

	public String getText() {
		return this.text;
	}

	public void clear() {
		this.text = "";
		this.textWidth = 0;
		this.label.setText("");
	}

	public boolean isFocused()
	{
		return this.focused;
	}

	public void setFocused(boolean focused)
	{
		this.focused = focused;
	}


}