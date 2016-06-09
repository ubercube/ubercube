/*
 *   Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.rendering.renderers.game.entities;

import java.util.List;
import java.util.Map;

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

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marccspro on 1 mai 2016.
 */
public class EntityWeaponRenderer
{
	private static final ModelVoxRenderer AK47_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox("res/weapons/AK47.vox"), new Vec3(0.5f, 1f, 1f));
	private static final ModelVoxRenderer AWP_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox("res/weapons/AWP.vox"));
	private static final ModelVoxRenderer SHOVEL_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox("res/weapons/SHOVEL.vox"));

	public void renderEntityWeapons(WeaponShader shader, int cubemap, Map<Integer, Entity> entities, List<Integer> indices)
	{
		Renderer.bindTextureCube(cubemap);
		glDisable(GL11.GL_CULL_FACE);
		for (int i = 0; i < indices.size(); i++)
		{
			if (i == 0)
				continue;
			
			int entityID = indices.get(i);
			renderEntityWeapon(shader, cubemap, entities, entityID);
		}
		glEnable(GL11.GL_CULL_FACE);
		Renderer.bindTextureCube(0);
	}
	
	public void renderPlayerWeapon(WeaponShader shader, int cubemap, Map<Integer, Entity> entities, List<Integer> indices)
	{
		Renderer.bindTextureCube(cubemap);
		glDisable(GL11.GL_CULL_FACE);

		renderEntityWeapon(shader, cubemap, entities, indices.get(0));
		
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
		
		int weapon = weaponComp.getWeaponID();
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
		}
	}
}
