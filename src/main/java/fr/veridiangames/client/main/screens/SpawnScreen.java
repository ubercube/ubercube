package fr.veridiangames.client.main.screens;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.kits.AssaultKit;
import fr.veridiangames.core.game.entities.weapons.kits.BuilderKit;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.RespawnPacket;
import fr.veridiangames.core.network.packets.WeaponChangePacket;
import fr.veridiangames.core.utils.Color4f;

public class SpawnScreen extends GuiCanvas
{
    private GameCore core;
    private Display display;
    private boolean     centered;

    public SpawnScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);
        this.core = core;
        this.display = display;
        this.centered = false;

        GuiPanel bgPanel = new GuiPanel(0, 0, display.getWidth(), display.getHeight());
        bgPanel.setColor(Color4f.DARK_CYAN);
        bgPanel.setOrigin(GuiComponent.GuiOrigin.A);
        bgPanel.setScreenParent(GuiComponent.GuiCorner.SCALED);
        bgPanel.getColor().setAlpha(0.5f);
        super.add(bgPanel);

        GuiLabel title = new GuiLabel("Select your class !", display.getWidth() / 2, display.getHeight() / 3, 30);
        title.setDropShadow(2);
        title.setColor(Color4f.DARK_CYAN);
        title.setOrigin(GuiComponent.GuiOrigin.BC);
        title.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(title);

        /*GuiButton respawnButton = new GuiButton("Respawn", display.getWidth() / 2 , display.getHeight() / 2 - 15, 150, new GuiActionListener() {
            @Override
            public void onAction()
            {
                ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
                p.getNet().send(new RespawnPacket(p), Protocol.TCP);
                display.getInput().getMouse().setGrabbed(true);
            }
        });
        respawnButton.centerText();
        respawnButton.setClickable(true);
        respawnButton.setOrigin(GuiComponent.GuiOrigin.BC);
        respawnButton.setScreenParent(GuiComponent.GuiCorner.TC);*/


		GuiButton assaultButton = new GuiButton("Assault", display.getWidth() / 2 - 100, display.getHeight() / 2 - 15, 150, new GuiActionListener() {
			@Override
			public void onAction()
			{
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(new AssaultKit(p.getWeaponComponent().getWeaponManager()));
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
			}
		});
		assaultButton.centerText();
		assaultButton.setClickable(true);
		assaultButton.setOrigin(GuiComponent.GuiOrigin.BC);
		assaultButton.setScreenParent(GuiComponent.GuiCorner.TC);


		GuiButton builderButton = new GuiButton("Builder", display.getWidth() / 2 + 100, display.getHeight() / 2 - 15, 150, new GuiActionListener() {
			@Override
			public void onAction()
			{
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getWeaponComponent().setKit(new BuilderKit(p.getWeaponComponent().getWeaponManager()));
				display.getInput().getMouse().setGrabbed(true);
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
			}
		});
		builderButton.centerText();
		builderButton.setClickable(true);
		builderButton.setOrigin(GuiComponent.GuiOrigin.BC);
		builderButton.setScreenParent(GuiComponent.GuiCorner.TC);

        super.add(assaultButton);
		//super.add(respawnButton);
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
