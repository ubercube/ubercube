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

package fr.veridiangames.client.rendering.renderers.game;

import fr.veridiangames.client.rendering.renderers.game.entities.particles.ParticleRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerNameRenderer;
import fr.veridiangames.client.rendering.shaders.*;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.client.main.Main;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.client.rendering.primitives.SpherePrimitive;
import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityWeaponRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerSelectionRenderer;
import fr.veridiangames.client.rendering.renderers.game.world.WorldRenderer;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class GameRenderer
{
	private GameCore				core;
	
	private PlayerShader			playerShader;
	private EntityShader			entityShader;
	private WorldShader				worldShader;
	private WeaponShader			weaponShader;
	private EnvSphereShader			envSphereShader;
	private Gui3DShader 			gui3DShader;

	private PlayerRenderer			playerRenderer;
	private EntityRenderer			entityRenderer;
	private ParticleRenderer 		particleRenderer;
	private WorldRenderer			worldRenderer;
	private PlayerSelectionRenderer playerSelectionRenderer;
	private EntityWeaponRenderer 	entityWeaponRenderer;
	private PlayerNameRenderer 		playerNameRenderer;

	private PlayerViewport			playerViewport;
	
	private SpherePrimitive			envSphere;
	private EnvCubemap				envCubemap;
	private Camera 					envCamera;
	
	public GameRenderer(Main main, GameCore core)
	{
		this.core = core;
		
		this.playerShader = new PlayerShader();
		this.entityShader = new EntityShader();
		this.worldShader = new WorldShader();
		this.weaponShader = new WeaponShader();
		this.envSphereShader = new EnvSphereShader();
		this.envSphereShader = new EnvSphereShader();

		this.playerRenderer = new PlayerRenderer();
		this.entityRenderer = new EntityRenderer();
		this.playerNameRenderer = new PlayerNameRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.entityWeaponRenderer = new EntityWeaponRenderer();
		this.playerSelectionRenderer = new PlayerSelectionRenderer(main.getPlayerHandler().getSelection());
		this.worldRenderer = new WorldRenderer(core);
		this.gui3DShader = new Gui3DShader();

		this.playerViewport = new PlayerViewport(main.getDisplay(), main);
		
		this.envSphere = new SpherePrimitive(3);
		this.envCubemap = new EnvCubemap(512);
		this.envCamera = new Camera(90.0f, 512, 512, 0.5f, 100.0f);
	}
	
	public void update()
	{
		playerViewport.update();
		playerRenderer.updateInstances(
			core.getGame().getEntityManager().getEntities(), 
			core.getGame().getEntityManager().getPlayerEntites()
		);
		entityRenderer.updateInstances(
			core.getGame().getEntityManager().getEntities(), 
			core.getGame().getEntityManager().getRenderableEntites()
		);
		particleRenderer.updateInstances(
			core.getGame().getEntityManager().getEntities(),
			core.getGame().getEntityManager().getParticleEntities()
		);
		worldRenderer.update(playerViewport.getCamera());
	}
	
	public void render()
	{
		renderWorld(playerViewport.getCamera());
	}

	public void bindEnvMap()
	{
		envCubemap.bind();
		envCamera.getTransform().setLocalPosition(playerViewport.getPlayerHandler().getEnvSpherePos());
		
		envCubemap.bindSide(0);
		envCamera.getTransform().setLocalRotation(new Quat(0, 1, 0, 1));
		renderWorld(envCamera);
		
		envCubemap.bindSide(1);
		envCamera.getTransform().setLocalRotation(new Quat(0, -1, 0, 1));
		renderWorld(envCamera);
		
		envCubemap.bindSide(2);
		envCamera.getTransform().setLocalRotation(new Quat(1, 0, 0, 1));
		renderWorld(envCamera);
		
		envCubemap.bindSide(3);
		envCamera.getTransform().setLocalRotation(new Quat(-1, 0, 0, 1));
		renderWorld(envCamera);
		
		envCubemap.bindSide(4);
		envCamera.getTransform().setLocalRotation(new Quat(0, 0, 0, 1));
		renderWorld(envCamera);
		
		envCubemap.bindSide(5);
		envCamera.getTransform().setLocalRotation(new Quat(0, 1, 0, 0));
		renderWorld(envCamera);

		envCubemap.unbind();
	}
	
	public void renderPlayer(Camera camera)
	{
		weaponShader.bind();
		weaponShader.setShaderBase(
				camera.getProjection(), 
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);

//		entityWeaponRenderer.renderPlayerWeapon(
//				weaponShader,
//				envCubemap.getCubemap(),
//				core.getGame().getEntityManager().getEntities(),
//				core.getGame().getEntityManager().getPlayerEntites()
//		);
	}
	
	public void renderWorld(Camera camera)
	{
		gui3DShader.bind();
		gui3DShader.setProjectionMatrix(camera.getProjection());
		gui3DShader.setModelViewMatrix(Mat4.identity());
		playerNameRenderer.render(
				gui3DShader,
				camera
		);

		playerShader.bind();
		playerShader.setShaderBase(
			camera.getProjection(),
			camera.getTransform().getPosition(),
			core.getGame().getData().getViewDistance()
		);
		playerShader.setModelViewMatrix(Mat4.identity());
		playerRenderer.render();
		
		entityShader.bind();
		entityShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityShader.setModelViewMatrix(Mat4.identity());
		entityRenderer.render(
				entityShader,
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
		);

		entityShader.bind();
		entityShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityShader.setModelViewMatrix(Mat4.identity());
		particleRenderer.render(
				entityShader,
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getParticleEntities()
		);

		weaponShader.bind();
		weaponShader.setShaderBase(
				camera.getProjection(), 
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityWeaponRenderer.renderEntityWeapons(
				weaponShader,
				envCubemap.getCubemap(),
				core.getGame().getEntityManager().getEntities(), 
				core.getGame().getEntityManager().getRenderableEntites()
		);
		
		worldShader.bind();
		worldShader.setShaderBase(
				camera.getProjection(), 
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		worldShader.setModelViewMatrix(Mat4.identity());
		worldRenderer.render();
		playerSelectionRenderer.render(worldShader);
		
		
		if (playerViewport.getPlayerHandler().isShowEnvSphere())
		{
			envSphereShader.bind();
			envSphereShader.setShaderBase(
					camera.getProjection(), 
					camera.getTransform().getPosition(),
					core.getGame().getData().getViewDistance()
			);
			envSphereShader.setModelViewMatrix(Mat4.translate(playerViewport.getPlayerHandler().getEnvSpherePos()));
			
			Renderer.bindTextureCube(envCubemap.getCubemap());
			envSphere.render();
			Renderer.bindTextureCube(0);
		}


	}
	
	public GameCore getGameCore()
	{
		return core;
	}
}
