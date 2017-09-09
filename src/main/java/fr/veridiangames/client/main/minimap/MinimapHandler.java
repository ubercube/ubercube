package fr.veridiangames.client.main.minimap;

import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.gamemodes.Team;
import fr.veridiangames.core.maths.Vec2i;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;

import static fr.veridiangames.client.Resource.getResource;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class MinimapHandler
{
	public static final Texture SPAWN_ICON = TextureLoader.loadTexture(getResource("textures/house_icon.png"), GL_LINEAR, false);

	public static final Texture NORTH_ICON = TextureLoader.loadTexture(getResource("textures/North.png"), GL_LINEAR, false);
	public static final Texture SOUTH_ICON = TextureLoader.loadTexture(getResource("textures/South.png"), GL_LINEAR, false);
	public static final Texture EAST_ICON = TextureLoader.loadTexture(getResource("textures/East.png"), GL_LINEAR, false);
	public static final Texture WEST_ICON = TextureLoader.loadTexture(getResource("textures/West.png"), GL_LINEAR, false);

	private Vec2i pos;
	private Vec2i size;

	private List<MinimapObject> staticObjects;

	public MinimapHandler(GameCore core)
	{
		this.staticObjects = new ArrayList<>();
		this.pos = new Vec2i(35, 30);
		this.size = new Vec2i(300, 200);

		add(new MinimapObject(NORTH_ICON, new Vec3(0, 0, 60), Color4f.WHITE.copy(), MinimapObject.MinimapObjectType.RELATIVE));
		add(new MinimapObject(SOUTH_ICON, new Vec3(0, 0, -60), Color4f.WHITE.copy(), MinimapObject.MinimapObjectType.RELATIVE));
		add(new MinimapObject(EAST_ICON, new Vec3(60, 0, 0), Color4f.WHITE.copy(), MinimapObject.MinimapObjectType.RELATIVE));
		add(new MinimapObject(WEST_ICON, new Vec3(-60, 0, 0), Color4f.WHITE.copy(), MinimapObject.MinimapObjectType.RELATIVE));

		for (Team team : core.getGame().getGameMode().getTeams())
		{
			add(new MinimapObject(SPAWN_ICON, team.getSpawn(), team.getColor(), MinimapObject.MinimapObjectType.STATIC));
		}
	}

	public void add(MinimapObject staticObject)
	{
		this.staticObjects.add(staticObject);
	}

	public List<MinimapObject> getStaticObjects()
	{
		return staticObjects;
	}

	public Vec2i getPos() {
		return pos;
	}

	public void setPos(Vec2i pos) {
		this.pos = pos;
	}

	public Vec2i getSize() {
		return size;
	}

	public void setSize(Vec2i size) {
		this.size = size;
	}
}
