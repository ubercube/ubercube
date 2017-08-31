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

package fr.veridiangames.client.inputs;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Marccspro on 9 f�vr. 2016.
 */
public class Keyboard extends GLFWKeyCallback {

	public static boolean AZERTY = true;

	public static final int NUM_KEYCODES = 1000;

	public static final int RELEASE = 0x0;
	public static final int PRESS = 0x1;
	public static final int REPEAT = 0x2;

	public boolean[] keys = new boolean[65536];

	public ArrayList<Integer> currentKeys = new ArrayList<>();
	private ArrayList<Integer> downKeys = new ArrayList<>();
	private ArrayList<Integer> upKeys = new ArrayList<>();

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//System.out.println("KEY(" + String.valueOf(Character.valueOf((char) key)) + "): 0x" + Integer.toHexString(key));
		if (key >= 0) {
			if (AZERTY) {
				if (key == Input.KEY_Q) {this.keys[Input.KEY_A] = action != RELEASE; return;}
				if (key == Input.KEY_A) {this.keys[Input.KEY_Q] = action != RELEASE; return;}

				if (key == Input.KEY_Z) {this.keys[Input.KEY_W] = action != RELEASE; return;}
				if (key == Input.KEY_W) {this.keys[Input.KEY_Z] = action != RELEASE; return;}

				if (key == Input.KEY_Z) {this.keys[Input.KEY_W] = action != RELEASE; return;}
				if (key == Input.KEY_W) {this.keys[Input.KEY_Z] = action != RELEASE; return;}

				if (key == Input.KEY_SEMICOLON) {this.keys[Input.KEY_M] = action != RELEASE; return;}
				if (key == Input.KEY_M) {this.keys[Input.KEY_COMMA] = action != RELEASE; return;}

				if (key == Input.KEY_COMMA) {this.keys[Input.KEY_PERIOD] = action != RELEASE; return;}
				if (key == Input.KEY_PERIOD) {this.keys[Input.KEY_KP_DIVIDE] = action != RELEASE; return;}
				//if (key == KEY_M) {keys[KEY_COMMA] = action != RELEASE; return;}
			}
			this.keys[key] = action != RELEASE;
		}
	}

	public void update() {
		this.upKeys.clear();

		for (int i = 0; i < NUM_KEYCODES; i++)
			if (!this.getKey(i) && this.currentKeys.contains(i))
				this.upKeys.add(i);

		this.downKeys.clear();

		for (int i = 0; i < NUM_KEYCODES; i++)
			if (this.getKey(i) && !this.currentKeys.contains(i))
				this.downKeys.add(i);

		this.currentKeys.clear();

		for (int i = 0; i < NUM_KEYCODES; i++)
			if (this.getKey(i))
				this.currentKeys.add(i);
	}

	public boolean getKey(int key) {
		return this.keys[key];
	}

	public boolean getKeyDown(int key) {
		return this.downKeys.contains(key);
	}

	public boolean getKeyUp(int key) {
		return this.upKeys.contains(key);
	}
}