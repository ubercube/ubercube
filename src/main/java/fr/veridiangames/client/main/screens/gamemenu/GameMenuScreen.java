package fr.veridiangames.client.main.screens.gamemenu;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.utils.Color4f;

public class GameMenuScreen extends GuiCanvas
{
    private GameCore    core;
    private Display     display;
    private boolean     centered;

    public GameMenuScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);
        this.core = core;
        this.display = display;
        this.centered = false;

        GuiPanel bgPanel = new GuiPanel(0, 0, Display.getInstance().getWidth(), Display.getInstance().getHeight());
        bgPanel.setColor(Color4f.BLACK);
        bgPanel.setOrigin(GuiComponent.GuiOrigin.A);
        bgPanel.setScaleParent(GuiComponent.GuiScale.SCALED);
        bgPanel.getColor().setAlpha(0.5f);
        super.add(bgPanel);

        GuiButton returnButton = new GuiButton("return", 50, 50, 150, new GuiActionListener() {
            @Override
            public void onAction() {
                setRendered(false);
                display.getInput().getMouse().setGrabbed(true);
                centered = false;
                core.setIgnoreAction(true);
            }
        });
        returnButton.centerText();
        returnButton.setClickable(true);
        returnButton.setOrigin(GuiComponent.GuiOrigin.A);
        returnButton.setScreenParent(GuiComponent.GuiCorner.TL);
        super.add(returnButton);

        OptionsMenuScreen optionsMenuScreen = new OptionsMenuScreen(this, display);
        super.addCanvas(optionsMenuScreen);
        GuiButton optionsButton = new GuiButton("options", 50, 90, 150, new GuiActionListener() {
            @Override
            public void onAction() {
                optionsMenuScreen.setRendered(true);
            }
        });
        optionsButton.centerText();
        optionsButton.setClickable(true);
        optionsButton.setOrigin(GuiComponent.GuiOrigin.A);
        optionsButton.setScreenParent(GuiComponent.GuiCorner.TL);
        super.add(optionsButton);

        GuiButton graphicsButton = new GuiButton("graphics", 50, 130, 150, new GuiActionListener() {
            @Override
            public void onAction() {
                optionsMenuScreen.setRendered(false);
            }
        });
        graphicsButton.centerText();
        graphicsButton.setClickable(true);
        graphicsButton.setOrigin(GuiComponent.GuiOrigin.A);
        graphicsButton.setScreenParent(GuiComponent.GuiCorner.TL);
        super.add(graphicsButton);

        GuiButton disconnectButton = new GuiButton("disconnect", 50, 170, 150, new GuiActionListener() {
            @Override
            public void onAction() {
                Ubercube.getInstance().disconnectAndExit();
            }
        });
        disconnectButton.centerText();
        disconnectButton.setClickable(true);
        disconnectButton.setOrigin(GuiComponent.GuiOrigin.A);
        disconnectButton.setScreenParent(GuiComponent.GuiCorner.TL);
        super.add(disconnectButton);

        setRendered(false);
    }

    public void update()
    {
        if (display.getInput().getKeyDown(Input.KEY_ESCAPE))
        {
            setRendered(true);
            display.getInput().getMouse().setGrabbed(false);
        }

        Ubercube.getInstance().setInMenu(false);
        if (!isRendered())
            return;

        Ubercube.getInstance().setInMenu(true);
        super.update();
    }
}
