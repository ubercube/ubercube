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

public class OptionsMenuScreen extends GuiCanvas
{
    private Display display;

    private GuiSlider mouseSpeed;
    private GuiSlider mouseZoomedSpeed;
    private GuiSlider audioGain;
    private GuiCheckBox renderSnow;
    private GuiCheckBox enableVsync;

    public OptionsMenuScreen(GuiCanvas parent, Display display)
    {
        super(parent);
        this.display = display;

        GuiPanel bg = new GuiPanel(display.getWidth() / 2, 120, 500, 500);
        bg.setColor(Color4f.BLACK);
        bg.getColor().setAlpha(0.2f);
        bg.setOrigin(GuiComponent.GuiOrigin.TC);
        bg.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(bg);

        GuiLabel title = new GuiLabel("Options", display.getWidth() / 2, 120, 30);
        title.setDropShadow(2);
        title.setColor(Color4f.WHITE);
        title.setOrigin(GuiComponent.GuiOrigin.BC);
        title.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(title);

        GuiLabel mouseSpeedText = new GuiLabel("Sensitivity (Normal and zoomed)", display.getWidth() / 2, 160 + 20, 24);
        mouseSpeedText.setDropShadow(2);
        mouseSpeedText.setColor(Color4f.WHITE);
        mouseSpeedText.setOrigin(GuiComponent.GuiOrigin.BC);
        mouseSpeedText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(mouseSpeedText);

        float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getIdleSpeed();
        mouseSpeed = new GuiSlider((int)Math.ceil(currentMouseSpeed * 100) + "", display.getWidth() / 2 - 5, 165 + 20, 235);
        mouseSpeed.setOrigin(GuiComponent.GuiOrigin.B);
        mouseSpeed.setScreenParent(GuiComponent.GuiCorner.TC);
        mouseSpeed.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
        mouseSpeed.setValue(currentMouseSpeed);
        super.add(mouseSpeed);

		float currentMouseZoomedSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getZoomedSpeed();
		mouseZoomedSpeed = new GuiSlider((int)Math.ceil(currentMouseZoomedSpeed * 100) + "", display.getWidth() / 2 + 5, 165 + 20, 235);
		mouseZoomedSpeed.setOrigin(GuiComponent.GuiOrigin.A);
		mouseZoomedSpeed.setScreenParent(GuiComponent.GuiCorner.TC);
		mouseZoomedSpeed.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
		mouseZoomedSpeed.setValue(currentMouseZoomedSpeed);
		super.add(mouseZoomedSpeed);

        GuiLabel audioGainText = new GuiLabel("Audio", display.getWidth() / 2, 230 + 20, 24);
        audioGainText.setDropShadow(2);
        audioGainText.setColor(Color4f.WHITE);
        audioGainText.setOrigin(GuiComponent.GuiOrigin.BC);
        audioGainText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(audioGainText);

        audioGain = new GuiSlider((int)(AudioSystem.getMainVolume() * 100) + "", display.getWidth() / 2, 235 + 20, 480);
        audioGain.setOrigin(GuiComponent.GuiOrigin.TC);
        audioGain.setScreenParent(GuiComponent.GuiCorner.TC);
        audioGain.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
        audioGain.setValue(AudioSystem.getMainVolume());
        super.add(audioGain);

        setRendered(false);
    }

    public void update()
    {
        if (!isRendered())
            return;

		super.update();

		float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getIdleSpeed();
		mouseSpeed.getLabel().setText((int)Math.ceil(currentMouseSpeed * 100) + "");
		float currentMouseZoomedSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getZoomedSpeed();
		mouseZoomedSpeed.getLabel().setText((int)Math.ceil(currentMouseZoomedSpeed * 100) + "");
        Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().setIdleSpeed(mouseSpeed.getValue());
        Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().setZoomedSpeed(mouseZoomedSpeed.getValue());
        AudioSystem.setMainVolume(audioGain.getValue());
        audioGain.getLabel().setText((int)(AudioSystem.getMainVolume() * 100) + "");
    }
}
