package fr.veridiangames.client.main.screens.gamemenu;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiSlider;
import fr.veridiangames.core.utils.Color4f;

public class OptionsMenuScreen extends GuiCanvas
{
    private Display display;

    private GuiSlider mouseSpeed;
    private GuiSlider audioGain;

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

        GuiLabel mouseSpeedText = new GuiLabel("Sensitivity", display.getWidth() / 2, 160, 24);
        mouseSpeedText.setDropShadow(2);
        mouseSpeedText.setColor(Color4f.WHITE);
        mouseSpeedText.setOrigin(GuiComponent.GuiOrigin.BC);
        mouseSpeedText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(mouseSpeedText);

        float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getSpeed();
        mouseSpeed = new GuiSlider((int)(currentMouseSpeed * 100) + "", display.getWidth() / 2, 165, 480);
        mouseSpeed.setOrigin(GuiComponent.GuiOrigin.TC);
        mouseSpeed.setScreenParent(GuiComponent.GuiCorner.TC);
        mouseSpeed.getLabel().setOrigin(GuiComponent.GuiOrigin.CENTER);
        mouseSpeed.setValue(currentMouseSpeed);
        super.add(mouseSpeed);

        GuiLabel audioGainText = new GuiLabel("Audio", display.getWidth() / 2, 230, 24);
        audioGainText.setDropShadow(2);
        audioGainText.setColor(Color4f.WHITE);
        audioGainText.setOrigin(GuiComponent.GuiOrigin.BC);
        audioGainText.setScreenParent(GuiComponent.GuiCorner.TC);
        super.add(audioGainText);

        audioGain = new GuiSlider((int)(AudioSystem.getMainVolume() * 100) + "", display.getWidth() / 2, 235, 480);
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
        float currentMouseSpeed = Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().getSpeed();
        mouseSpeed.getLabel().setText((int)(currentMouseSpeed * 100) + "");
        Ubercube.getInstance().getGameCore().getGame().getPlayer().getMouseComponent().setSpeed(mouseSpeed.getValue());
        audioGain.getLabel().setText((int)(AudioSystem.getMainVolume() * 100) + "");
        AudioSystem.setMainVolume(audioGain.getValue());
    }
}
