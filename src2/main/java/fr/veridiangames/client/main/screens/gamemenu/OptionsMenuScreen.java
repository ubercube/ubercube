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
    private GuiSlider mouseSpeed;
    private GuiSlider audioGain;
    private GuiCheckBox renderSnow;

    public OptionsMenuScreen(GuiCanvas parent, Display display)
    {
        super(parent);
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

        GuiLabel mouseSpeedText = new GuiLabel("Sensitivity", display.getWidth() / 2, 160, 24);
        mouseSpeedText.setDropShadow(2);
        mouseSpeedText.setColor(Color4f.WHITE);
        mouseSpeedText.setOrigin(GuiComponent.GuiOrigin.BC);
        mouseSpeedText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(mouseSpeedText);

        float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getSpeed();
        this.mouseSpeed = new GuiSlider((int)(currentMouseSpeed * 100) + "", display.getWidth() / 2, 165, 480);
        this.mouseSpeed.setOrigin(GuiComponent.GuiOrigin.TC);
        this.mouseSpeed.setScreenParent(GuiComponent.GuiCorner.TC);
        this.mouseSpeed.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
        this.mouseSpeed.setValue(currentMouseSpeed);
        super.add(this.mouseSpeed);

        GuiLabel audioGainText = new GuiLabel("Audio", display.getWidth() / 2, 230, 24);
        audioGainText.setDropShadow(2);
        audioGainText.setColor(Color4f.WHITE);
        audioGainText.setOrigin(GuiComponent.GuiOrigin.BC);
        audioGainText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(audioGainText);

        this.audioGain = new GuiSlider((int)(AudioSystem.getMainVolume() * 100) + "", display.getWidth() / 2, 235, 480);
        this.audioGain.setOrigin(GuiComponent.GuiOrigin.TC);
        this.audioGain.setScreenParent(GuiComponent.GuiCorner.TC);
        this.audioGain.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
        this.audioGain.setValue(AudioSystem.getMainVolume());
        super.add(this.audioGain);

        this.renderSnow = new GuiCheckBox("Render snow", display.getWidth() / 2, 290, true);
        this.renderSnow.setOrigin(GuiComponent.GuiOrigin.TC);
        this.renderSnow.setScreenParent(GuiComponent.GuiCorner.TC);
        this.renderSnow.getLabel().setColor(Color4f.WHITE);
        this.renderSnow.getLabel().setDropShadow(2);
        this.renderSnow.triggered = Ubercube.getInstance().getGameCore().getGame().getPlayer().isRenderSnow();
        super.add(this.renderSnow);

        this.setRendered(false);
    }

    @Override
	public void update()
    {
        if (!this.isRendered())
            return;

        super.update();
        float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getSpeed();
        this.mouseSpeed.getLabel().setText((int)(currentMouseSpeed * 100) + "");
        Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().setSpeed(this.mouseSpeed.getValue());
        this.audioGain.getLabel().setText((int)(AudioSystem.getMainVolume() * 100) + "");
        AudioSystem.setMainVolume(this.audioGain.getValue());
        Ubercube.getInstance().getGameCore().getGame().getPlayer().setRenderSnow(this.renderSnow.triggered);
    }
}
