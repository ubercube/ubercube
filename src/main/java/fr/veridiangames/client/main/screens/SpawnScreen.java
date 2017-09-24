package fr.veridiangames.client.main.screens;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiMinimap;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiRotatingWeapon;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.renderers.models.OBJModel;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.kits.AssaultKit;
import fr.veridiangames.core.game.entities.weapons.kits.Kit;
import fr.veridiangames.core.game.entities.weapons.kits.MedicKit;
import fr.veridiangames.core.game.entities.weapons.kits.SniperKit;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.RespawnPacket;
import fr.veridiangames.core.network.packets.WeaponChangePacket;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.client.Resource.getResource;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class SpawnScreen extends GuiCanvas
{
    private GameCore core;
    private Display display;
    private boolean centered;

    public SpawnScreen(GuiCanvas parent, Display display, GameCore core)
    {
		super(parent);
		this.core = core;
		this.display = display;
		this.centered = false;

		GuiPanel bgPanel = new GuiPanel(0, 0, display.getWidth(), display.getHeight());
		bgPanel.setTexture(TextureLoader.loadTexture(getResource("textures/LoadingBG.png"), GL_LINEAR, false));
		bgPanel.setOrigin(GuiComponent.GuiOrigin.A);
		bgPanel.setScaleParent(GuiComponent.GuiScale.SCALED);
		super.add(bgPanel);

        /* Map */
		GuiMinimap mapPanel = new GuiMinimap(display.getWidth() / 2, display.getHeight() / 2, 1920, 1080);
		mapPanel.setColor(Color4f.RED);
		mapPanel.setOrigin(GuiComponent.GuiOrigin.CENTER);
		mapPanel.setScreenParent(GuiComponent.GuiCorner.CENTER);
		mapPanel.setScale(4);
		mapPanel.setDrawClient(false);
		super.add(mapPanel);

		/* Kits */
		ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
		displayKit(new AssaultKit(p.getWeaponComponent().getWeaponManager()), display.getWidth() / 2 - 400, display.getHeight() - 50);
		displayKit(new SniperKit(p.getWeaponComponent().getWeaponManager()), display.getWidth() / 2, display.getHeight() - 50);
		displayKit(new MedicKit(p.getWeaponComponent().getWeaponManager()), display.getWidth() / 2 + 400, display.getHeight() - 50);

        setRendered(false);
    }

    public void update()
    {
        if(GameCore.getInstance().getGame().getPlayer().isDead())
        {
            setRendered(true);
            display.getInput().getMouse().setGrabbed(false);
            super.update();
        }
        else
        {
            setRendered(false);
        }
    }

    private Weapon getWeapon(int id)
	{
		return GameCore.getInstance().getGame().getPlayer().getWeaponComponent().getWeaponManager().get(id);
	}

	private void displayKit(Kit kit, int x, int y)
	{
		GuiRotatingWeapon rotating = new GuiRotatingWeapon(x, y - 115, 400, 300, new Color4f(0, 0, 0, 0), kit.getWeapons().get(0));
		rotating.setOrigin(GuiComponent.GuiOrigin.CENTER);
		rotating.setScreenParent(GuiComponent.GuiCorner.BC);

		GuiButton button = new GuiButton(kit.getName(), x, y, 200, new GuiActionListener() {
			@Override
			public void onAction()
			{
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(kit);
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
			}
		});
		button.centerText();
		button.setClickable(true);
		button.setOrigin(GuiComponent.GuiOrigin.CENTER);
		button.setScreenParent(GuiComponent.GuiCorner.BC);

		super.add(rotating);
		super.add(button);
	}
}
