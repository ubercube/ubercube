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

import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.game.entities.ModeledEntityRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.particles.ParticleRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerNameRenderer;
import fr.veridiangames.client.rendering.renderers.game.minimap.MinimapFramebuffer;
import fr.veridiangames.client.rendering.renderers.game.physics.ColliderRenderer;
import fr.veridiangames.client.rendering.shaders.*;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityWeaponRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerSelectionRenderer;
import fr.veridiangames.client.rendering.renderers.game.world.WorldRenderer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class GameRenderer
{
	private GameCore				core;

	private PlayerShader			playerShader;
	private EntityShader			entityShader;
	private WorldShader				worldShader;
	private WeaponShader modelShader;
	private EnvSphereShader			envSphereShader;
	private WeaponFboShader			weaponFboShader;
	private FrameBuffer				weaponFbo;

	private Gui3DShader 			gui3DShader;

	private PlayerRenderer			playerRenderer;
	private EntityRenderer			entityRenderer;
	private ParticleRenderer 		particleRenderer;
	private WorldRenderer			worldRenderer;
	private PlayerSelectionRenderer playerSelectionRenderer;
	private EntityWeaponRenderer 	entityWeaponRenderer;
	private ModeledEntityRenderer 	modeledEntityRenderer;
	private ColliderRenderer		colliderRenderer;

	private PlayerNameRenderer 		playerNameRenderer;
	private PlayerViewport			playerViewport;

	private EnvCubemap				envCubemap;
	private Camera 					envCamera;

	private boolean					drawColliders;

	private MinimapFramebuffer		minimap;

	public GameRenderer(Ubercube main, GameCore core)
	{
		this.core = core;

		this.playerShader = new PlayerShader();
		this.entityShader = new EntityShader();
		this.worldShader = new WorldShader();
		this.modelShader = new WeaponShader();
		this.envSphereShader = new EnvSphereShader();
		this.weaponFboShader = new WeaponFboShader();

		this.playerRenderer = new PlayerRenderer();
		this.entityRenderer = new EntityRenderer();
		this.playerNameRenderer = new PlayerNameRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.entityWeaponRenderer = new EntityWeaponRenderer();
		this.modeledEntityRenderer = new ModeledEntityRenderer();
		this.playerSelectionRenderer = new PlayerSelectionRenderer(main.getPlayerHandler().getSelection());
		this.colliderRenderer = new ColliderRenderer();

		this.worldRenderer = new WorldRenderer(core);
		this.gui3DShader = new Gui3DShader();

		this.minimap = new MinimapFramebuffer();

		this.playerViewport = new PlayerViewport(main.getDisplay(), main);

		this.envCubemap = new EnvCubemap(512);
		this.envCamera = new Camera(90.0f, 512, 512, 0.5f, 100.0f);

		this.drawColliders = false;
	}

	public void update()
	{
		if (Display.getInstance().getInput().getKeyUp(Input.KEY_F2))
			drawColliders = !drawColliders;
		minimap.update();
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
			playerViewport.getCamera(),
			core.getGame().getEntityManager().getEntities(),
			core.getGame().getEntityManager().getParticleEntities()
		);
		worldRenderer.update(playerViewport.getCamera());
		playerNameRenderer.update(
			core.getGame().getEntityManager().getEntities(),
			core.getGame().getEntityManager().getPlayerEntites()
		);
		if (drawColliders)
		{
			colliderRenderer.updateInstances(
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
			);
		}
	}

	public void render()
	{
		renderWorld(playerViewport.getCamera());

		if (weaponFbo == null || Display.getInstance().wasResized())
		{
			if (weaponFbo != null)
			{
				this.weaponFbo.destroy();
				this.weaponFbo = null;
			}
			this.weaponFbo = new FrameBuffer(Display.getInstance().getWidth(), Display.getInstance().getHeight());
		}

		weaponFbo.bind();
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		renderPlayer(playerViewport.getCamera());
		weaponFbo.unbind();

		weaponFboShader.bind();
		weaponFboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));

		glBindTexture(GL_TEXTURE_2D, weaponFbo.getColorTextureID());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(weaponFboShader,
				Display.getInstance().getWidth() / 2,
				Display.getInstance().getHeight() / 2,0,
				Display.getInstance().getWidth() / 2,
				-Display.getInstance().getHeight() / 2, 1);
		glEnable(GL_CULL_FACE);

		minimap.render();
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
		modelShader.bind();
		camera.setNear(0.05f);
		modelShader.setShaderBase(
				camera.getProjectionMatrix(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		modelShader.setViewMatrix(camera.getViewMatrix());
		entityWeaponRenderer.renderPlayerWeapon(
				modelShader,
				envCubemap.getCubemap(),
				core.getGame().getEntityManager().getEntities()
		);
		camera.setNear(0.1f);
	}

	public void renderWorld(Camera camera)
	{
		/* ***** RENDERING PLAYER ENTITIES ***** */
		playerShader.bind();
		playerShader.setShaderBase(
			camera.getProjectionMatrix(),
			camera.getTransform().getPosition(),
			core.getGame().getData().getViewDistance()
		);
		playerShader.setModelViewMatrix(Mat4.identity());
		playerRenderer.render();

		/* ***** RENDERING OTHER ENTITIES ***** */
		entityShader.bind();
		entityShader.setShaderBase(
				camera.getProjectionMatrix(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityShader.setModelViewMatrix(Mat4.identity());
		entityRenderer.render(
				entityShader,
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
		);
		if (drawColliders)
		{
			colliderRenderer.render(
				entityShader,
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
			);
		}

		/* ***** RENDERING PARTICLES ***** */
		entityShader.bind();
		entityShader.setShaderBase(
				camera.getProjectionMatrix(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityShader.setModelViewMatrix(Mat4.identity());
		particleRenderer.render(
				entityShader,
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getParticleEntities()
		);

		/* ***** RENDERING WEAPONS AND MODELS ***** */
		modelShader.bind();
		modelShader.setShaderBase(
				camera.getProjectionMatrix(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		entityWeaponRenderer.renderEntityWeapons(
				modelShader,
				envCubemap.getCubemap(),
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
		);
		modeledEntityRenderer.render(
				modelShader,
				envCubemap.getCubemap(),
				core.getGame().getEntityManager().getEntities(),
				core.getGame().getEntityManager().getRenderableEntites()
		);

		/* ***** RENDERING THE WORLD ***** */
		worldShader.bind();
		worldShader.setShaderBase(
				camera.getProjectionMatrix(),
				camera.getTransform().getPosition(),
				core.getGame().getData().getViewDistance()
		);
		worldShader.setModelViewMatrix(Mat4.identity());
		worldRenderer.render();
		playerSelectionRenderer.render(worldShader);

		/* ***** RENDERING BILLBOARDED TEXT ***** */
		gui3DShader.bind();
		gui3DShader.setProjectionMatrix(camera.getProjectionMatrix());
		gui3DShader.setModelViewMatrix(Mat4.identity());
		playerNameRenderer.render(
				gui3DShader,
				camera
		);
	}
	
	public GameCore getGameCore()
	{
		return core;
	}
}
