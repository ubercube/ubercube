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

package fr.veridiangames.client.rendering.renderers.game.entities;

import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import org.lwjgl.opengl.GL11;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECWeapon;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.loaders.ModelLoader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.client.rendering.renderers.models.ModelVoxRenderer;
import fr.veridiangames.client.rendering.shaders.WeaponShader;

import static fr.veridiangames.client.rendering.renderers.models.ModelVoxRenderer.*;
import static java.awt.Color.red;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marccspro on 1 mai 2016.
 */
public class EntityWeaponRenderer
{
	public void renderEntityWeapons(WeaponShader shader, int cubemap, Map<Integer, Entity> entities, List<Integer> indices)
	{
		renderPlayerWeapon(shader, cubemap, entities);

		Renderer.bindTextureCube(cubemap);
		glDisable(GL11.GL_CULL_FACE);
		for (int i = 0; i < indices.size(); i++)
		{
			int playerID = GameCore.getInstance().getGame().getPlayer().getID();
			int entityID = indices.get(i); // TODO: Null

			if (entityID == playerID)
				continue;

			renderEntityWeapon(shader, cubemap, entities, entityID);
		}
		glEnable(GL11.GL_CULL_FACE);
		Renderer.bindTextureCube(0);
	}
	
	public void renderPlayerWeapon(WeaponShader shader, int cubemap, Map<Integer, Entity> entities)
	{
		Renderer.bindTextureCube(cubemap);
		glDisable(GL11.GL_CULL_FACE);

		renderEntityWeapon(shader, cubemap, entities, GameCore.getInstance().getGame().getPlayer().getID());
		
		glEnable(GL11.GL_CULL_FACE);
		Renderer.bindTextureCube(0);
	}
	
	private void renderEntityWeapon(WeaponShader shader, int cubemap, Map<Integer, Entity> entities, int entity)
	{
		Entity e = entities.get(entity);
		if (e == null)
			return;
		if (!e.contains(EComponent.RENDER) || !e.contains(EComponent.WEAPON))
			return;
		ECRender render = (ECRender) e.get(EComponent.RENDER);
		ECWeapon weaponComp = (ECWeapon) e.get(EComponent.WEAPON);
		if (render == null || weaponComp == null)
			return;
		
		int weapon = weaponComp.getWeapon().getModel();
		shader.setModelViewMatrix(weaponComp.getWeapon().getTransform().toMatrix().mul(Mat4.scale(1f/16f, 1f/16f, 1f/16f)));
		
		renderWeapon(weapon);
	}
	
	private void renderWeapon(int weapon) 
	{
		switch (weapon)
		{
			case Weapon.AK47:
				AK47_RENDERER.render();
				break;
			case Weapon.AWP:
				AWP_RENDERER.render();
				break;
			case Weapon.SHOVEL:
				SHOVEL_RENDERER.render();
				break;
			case Weapon.GRENADE:
				GRENADE_RENDERER.render();
				break;
		}
	}
}
