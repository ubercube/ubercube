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

import static org.lwjgl.opengl.GL11.*;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.EntityWeaponRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.ModeledEntityRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.particles.ParticleRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerNameRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerRenderer;
import fr.veridiangames.client.rendering.renderers.game.entities.players.PlayerSelectionRenderer;
import fr.veridiangames.client.rendering.renderers.game.physics.ColliderRenderer;
import fr.veridiangames.client.rendering.renderers.game.world.WorldRenderer;
import fr.veridiangames.client.rendering.shaders.EntityShader;
import fr.veridiangames.client.rendering.shaders.EnvSphereShader;
import fr.veridiangames.client.rendering.shaders.Gui3DShader;
import fr.veridiangames.client.rendering.shaders.ModelShader;
import fr.veridiangames.client.rendering.shaders.PlayerShader;
import fr.veridiangames.client.rendering.shaders.WeaponFboShader;
import fr.veridiangames.client.rendering.shaders.WorldShader;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class GameRenderer
{
	private GameCore				core;

	private PlayerShader			playerShader;
	private EntityShader			entityShader;
	private WorldShader				worldShader;
	private ModelShader 			modelShader;
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

	public GameRenderer(Ubercube main, GameCore core)
	{
		this.core = core;

		this.playerShader = new PlayerShader();
		this.entityShader = new EntityShader();
		this.worldShader = new WorldShader();
		this.modelShader = new ModelShader();
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

		this.playerViewport = new PlayerViewport(main.getDisplay(), main);

		this.envCubemap = new EnvCubemap(512);
		this.envCamera = new Camera(90.0f, 512, 512, 0.5f, 100.0f);

		this.drawColliders = false;
	}

	public void update()
	{
		if (Display.getInstance().getInput().getKeyUp(Input.KEY_F2))
			this.drawColliders = !this.drawColliders;
		this.playerViewport.update();
		this.playerRenderer.updateInstances(
			this.core.getGame().getEntityManager().getEntities(),
			this.core.getGame().getEntityManager().getPlayerEntites()
		);
		this.entityRenderer.updateInstances(
			this.core.getGame().getEntityManager().getEntities(),
			this.core.getGame().getEntityManager().getRenderableEntites()
		);
		this.particleRenderer.updateInstances(
			this.playerViewport.getCamera(),
			this.core.getGame().getEntityManager().getEntities(),
			this.core.getGame().getEntityManager().getParticleEntities()
		);
		this.worldRenderer.update(this.playerViewport.getCamera());
		this.playerNameRenderer.update(
			this.core.getGame().getEntityManager().getEntities(),
			this.core.getGame().getEntityManager().getPlayerEntites()
		);
		if (this.drawColliders)
			this.colliderRenderer.updateInstances(
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getRenderableEntites()
			);
	}

	public void render()
	{
		this.renderWorld(this.playerViewport.getCamera());

		if (this.weaponFbo == null || Display.getInstance().wasResized())
		{
			if (this.weaponFbo != null)
				this.weaponFbo.destroy();
			this.weaponFbo = new FrameBuffer(Display.getInstance().getWidth(), Display.getInstance().getHeight());
		}

		this.weaponFbo.bind();
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.renderPlayer(this.playerViewport.getCamera());
		this.weaponFbo.unbind();

		this.weaponFboShader.bind();
		this.weaponFboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));

		glBindTexture(GL_TEXTURE_2D, this.weaponFbo.getColorTextureID());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(this.weaponFboShader,
				Display.getInstance().getWidth() / 2,
				Display.getInstance().getHeight() / 2,0 ,
				Display.getInstance().getWidth() / 2,
				-Display.getInstance().getHeight() / 2, 0);
		glEnable(GL_CULL_FACE);
	}

	public void bindEnvMap()
	{
		this.envCubemap.bind();
		this.envCamera.getTransform().setLocalPosition(this.playerViewport.getPlayerHandler().getEnvSpherePos());

		this.envCubemap.bindSide(0);
		this.envCamera.getTransform().setLocalRotation(new Quat(0, 1, 0, 1));
		this.renderWorld(this.envCamera);

		this.envCubemap.bindSide(1);
		this.envCamera.getTransform().setLocalRotation(new Quat(0, -1, 0, 1));
		this.renderWorld(this.envCamera);

		this.envCubemap.bindSide(2);
		this.envCamera.getTransform().setLocalRotation(new Quat(1, 0, 0, 1));
		this.renderWorld(this.envCamera);

		this.envCubemap.bindSide(3);
		this.envCamera.getTransform().setLocalRotation(new Quat(-1, 0, 0, 1));
		this.renderWorld(this.envCamera);

		this.envCubemap.bindSide(4);
		this.envCamera.getTransform().setLocalRotation(new Quat(0, 0, 0, 1));
		this.renderWorld(this.envCamera);

		this.envCubemap.bindSide(5);
		this.envCamera.getTransform().setLocalRotation(new Quat(0, 1, 0, 0));
		this.renderWorld(this.envCamera);

		this.envCubemap.unbind();
	}

	public void renderPlayer(Camera camera)
	{
		this.modelShader.bind();
		this.modelShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				this.core.getGame().getData().getViewDistance()
		);
		this.modelShader.setModelViewMatrix(Mat4.identity());
		this.entityWeaponRenderer.renderPlayerWeapon(
				this.modelShader,
				this.envCubemap.getCubemap(),
				this.core.getGame().getEntityManager().getEntities()
		);
	}

	public void renderWorld(Camera camera)
	{
		/* ***** RENDERING PLAYER ENTITIES ***** */
		this.playerShader.bind();
		this.playerShader.setShaderBase(
			camera.getProjection(),
			camera.getTransform().getPosition(),
			this.core.getGame().getData().getViewDistance()
		);
		this.playerShader.setModelViewMatrix(Mat4.identity());
		this.playerRenderer.render();

		/* ***** RENDERING OTHER ENTITIES ***** */
		this.entityShader.bind();
		this.entityShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				this.core.getGame().getData().getViewDistance()
		);
		this.entityShader.setModelViewMatrix(Mat4.identity());
		this.entityRenderer.render(
				this.entityShader,
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getRenderableEntites()
		);
		if (this.drawColliders)
			this.colliderRenderer.render(
				this.entityShader,
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getRenderableEntites()
			);

		/* ***** RENDERING PARTICLES ***** */
		this.entityShader.bind();
		this.entityShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				this.core.getGame().getData().getViewDistance()
		);
		this.entityShader.setModelViewMatrix(Mat4.identity());
		this.particleRenderer.render(
				this.entityShader,
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getParticleEntities()
		);

		/* ***** RENDERING WEAPONS AND MODELS ***** */
		this.modelShader.bind();
		this.modelShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				this.core.getGame().getData().getViewDistance()
		);
		this.entityWeaponRenderer.renderEntityWeapons(
				this.modelShader,
				this.envCubemap.getCubemap(),
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getRenderableEntites()
		);
		this.modeledEntityRenderer.render(
				this.modelShader,
				this.envCubemap.getCubemap(),
				this.core.getGame().getEntityManager().getEntities(),
				this.core.getGame().getEntityManager().getRenderableEntites()
		);

		/* ***** RENDERING THE WORLD ***** */
		this.worldShader.bind();
		this.worldShader.setShaderBase(
				camera.getProjection(),
				camera.getTransform().getPosition(),
				this.core.getGame().getData().getViewDistance()
		);
		this.worldShader.setModelViewMatrix(Mat4.identity());
		this.worldRenderer.render();
		this.playerSelectionRenderer.render(this.worldShader);

		/* ***** RENDERING BILLBOARDED TEXT ***** */
		this.gui3DShader.bind();
		this.gui3DShader.setProjectionMatrix(camera.getProjection());
		this.gui3DShader.setModelViewMatrix(Mat4.identity());
		this.playerNameRenderer.render(
				this.gui3DShader,
				camera
		);
	}

	public GameCore getGameCore()
	{
		return this.core;
	}
}
