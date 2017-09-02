package fr.veridiangames.client.inputs;

import org.lwjgl.glfw.GLFWCharCallbackI;

import java.util.ArrayList;
import java.util.List;

public class TextInput implements GLFWCharCallbackI
{
	private List<Integer> keyCodes = new ArrayList<>();

	private int keyCode = -1;

	public void invoke(long window, int codepoint)
	{
		keyCode = codepoint;
	}

	public int getKeyCode()
	{
		int key = new Integer(keyCode);
		keyCode = 0;
		return key;
	}
}
