package fr.veridiangames.client.main.screens.gamemenu;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiCheckBox;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiSlider;
import fr.veridiangames.core.utils.Color4f;

public class GraphicsMenuScreen extends GuiCanvas
{
    private Display display;

    private GuiCheckBox renderSnow;
    private GuiCheckBox enableVsync;
	private GuiCheckBox enableSSAA;
	private GuiCheckBox enableShadows;

    public GraphicsMenuScreen(GuiCanvas parent, Display display)
    {
        super(parent);
        this.display = display;

        GuiPanel bg = new GuiPanel(display.getWidth() / 2, 120, 500, 500);
        bg.setColor(Color4f.BLACK);
        bg.getColor().setAlpha(0.2f);
        bg.setOrigin(GuiComponent.GuiOrigin.TC);
        bg.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(bg);

        GuiLabel title = new GuiLabel("Graphics", display.getWidth() / 2, 120, 30);
        title.setDropShadow(2);
        title.setColor(Color4f.WHITE);
        title.setOrigin(GuiComponent.GuiOrigin.BC);
        title.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(title);

        renderSnow = new GuiCheckBox("Render snow", display.getWidth() / 2, 160, true);
        renderSnow.setOrigin(GuiComponent.GuiOrigin.A);
        renderSnow.setScreenParent(GuiComponent.GuiCorner.TC);
        renderSnow.getLabel().setColor(Color4f.WHITE);
        renderSnow.getLabel().setDropShadow(2);
        renderSnow.triggered = Ubercube.getInstance().getGameCore().getGame().getPlayer().isRenderSnow();
        super.add(renderSnow);

		enableVsync = new GuiCheckBox("Enable vSync", display.getWidth() / 2 - 220, 160, true);
		enableVsync.setOrigin(GuiComponent.GuiOrigin.A);
		enableVsync.setScreenParent(GuiComponent.GuiCorner.TC);
		enableVsync.getLabel().setColor(Color4f.WHITE);
		enableVsync.getLabel().setDropShadow(2);
		enableVsync.triggered = Display.getInstance().isVsync();
		super.add(enableVsync);

		enableSSAA = new GuiCheckBox("Enable SSAA", display.getWidth() / 2 - 220, 220, true);
		enableSSAA.setOrigin(GuiComponent.GuiOrigin.A);
		enableSSAA.setScreenParent(GuiComponent.GuiCorner.TC);
		enableSSAA.getLabel().setColor(Color4f.WHITE);
		enableSSAA.getLabel().setDropShadow(2);
		enableSSAA.triggered = true;
		super.add(enableSSAA);

		enableShadows = new GuiCheckBox("Enable Shadows", display.getWidth() / 2, 220, true);
		enableShadows.setOrigin(GuiComponent.GuiOrigin.A);
		enableShadows.setScreenParent(GuiComponent.GuiCorner.TC);
		enableShadows.getLabel().setColor(Color4f.WHITE);
		enableShadows.getLabel().setDropShadow(2);
		enableShadows.triggered = true;
		super.add(enableShadows);

        setRendered(false);
    }

    public void update()
    {
        if (!isRendered())
            return;
		super.update();

        Ubercube.getInstance().getGameCore().getGame().getPlayer().setRenderSnow(renderSnow.triggered);
        Display.getInstance().setVsync(enableVsync.triggered);
		Ubercube.getInstance().getMainRenderer().getGameBufferRenderer().setSamples(enableSSAA.triggered ? 2 : 1);
		Ubercube.getInstance().getMainRenderer().getGameBufferRenderer().setRenderShadows(enableShadows.triggered);
    }
}
