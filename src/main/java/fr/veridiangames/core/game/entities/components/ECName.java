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

package fr.veridiangames.core.game.entities.components;

/**
 * Created by Marccspro on 14 f�vr. 2016.
 */
public class ECName extends EComponent
{
	private String name;
	
	public ECName(String name)
	{
		super(NAME);
		this.name = getValidString(name);

	}

	public char getValidCharacter(char c)
	{
		if (Character.isDefined(c))
			return (c);
		return '?';
	}

	private String getValidString(String name)
	{
		String result = "";
		for (int i = 0; i < name.length(); i++)
		{
			char c = getValidCharacter(name.charAt(i));
			result += c;
		}
		return result;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}