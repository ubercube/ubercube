package fr.veridiangames.client.main.screens;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiMinimap;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.kits.AssaultKit;
import fr.veridiangames.core.game.entities.weapons.kits.BuilderKit;
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
		bgPanel.setScreenParent(GuiComponent.GuiCorner.SCALED);
		super.add(bgPanel);

        /* Map */
		GuiMinimap mapPanel = new GuiMinimap(display.getWidth() / 2, display.getHeight() / 2, 1920, 1080);
		mapPanel.setColor(Color4f.RED);
		mapPanel.setOrigin(GuiComponent.GuiOrigin.CENTER);
		mapPanel.setScreenParent(GuiComponent.GuiCorner.CENTER);
		mapPanel.setScale(4);
		mapPanel.setDrawClient(false);
		super.add(mapPanel);

        /* End Map */
		GuiButton sniperButton = new GuiButton("Sniper", display.getWidth() / 2 , display.getHeight() - 50, 150, new GuiActionListener() {
            @Override
            public void onAction()
            {
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(new SniperKit(p.getWeaponComponent().getWeaponManager()));
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new WeaponChangePacket(p), Protocol.UDP);
				p.getNet().send(new WeaponPositionPacket(p.getID(), p.getWeaponComponent().getWeaponIndex()), Protocol.UDP);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
            }
        });
		sniperButton.centerText();
		sniperButton.setClickable(true);
		sniperButton.setOrigin(GuiComponent.GuiOrigin.CENTER);
		sniperButton.setScreenParent(GuiComponent.GuiCorner.BC);

		GuiButton assaultButton = new GuiButton("Assault", display.getWidth() / 2 - 300, display.getHeight() - 50, 150, new GuiActionListener() {
			@Override
			public void onAction()
			{
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(new AssaultKit(p.getWeaponComponent().getWeaponManager()));
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new WeaponChangePacket(p), Protocol.UDP);
				p.getNet().send(new WeaponPositionPacket(p.getID(), p.getWeaponComponent().getWeaponIndex()), Protocol.UDP);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
			}
		});
		assaultButton.centerText();
		assaultButton.setClickable(true);
		assaultButton.setOrigin(GuiComponent.GuiOrigin.CENTER);
		assaultButton.setScreenParent(GuiComponent.GuiCorner.BC);

		GuiButton builderButton = new GuiButton("Builder", display.getWidth() / 2 + 300, display.getHeight() - 50, 150, new GuiActionListener() {
			@Override
			public void onAction()
			{
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(new BuilderKit(p.getWeaponComponent().getWeaponManager()));
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new WeaponChangePacket(p), Protocol.UDP);
				p.getNet().send(new WeaponPositionPacket(p.getID(), p.getWeaponComponent().getWeaponIndex()), Protocol.UDP);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
			}
		});
		builderButton.centerText();
		builderButton.setClickable(true);
		builderButton.setOrigin(GuiComponent.GuiOrigin.CENTER);
		builderButton.setScreenParent(GuiComponent.GuiCorner.BC);

        super.add(assaultButton);
		super.add(sniperButton);
		super.add(builderButton);

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
}
