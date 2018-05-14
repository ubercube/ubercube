package fr.veridiangames.client.main.screens.gamemode;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.game.gamemodes.TDMGameMode;
import fr.veridiangames.core.utils.Color4f;

public class QGHudScreen extends GuiCanvas
{

	private GuiLabel redLabel;
	private GuiLabel blueLabel;
	private GuiCanvas parent;

	public QGHudScreen(GuiCanvas parent)
	{
		super(parent);
		this.parent = parent;

		Display display = Ubercube.getInstance().getDisplay();

		redLabel = new GuiLabel("Red : 0", display.getWidth() / 2-160, 10, 20f);
		redLabel.setOrigin(GuiComponent.GuiOrigin.TC);
		redLabel.setScreenParent(GuiComponent.GuiCorner.TC);
		redLabel.setColor(Color4f.RED);
		redLabel.setDropShadow(1);
		redLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
		super.add(redLabel);

		blueLabel = new GuiLabel("Blue : 0", display.getWidth() / 2 +160, 10, 20f);
		blueLabel.setOrigin(GuiComponent.GuiOrigin.TC);
		blueLabel.setScreenParent(GuiComponent.GuiCorner.TC);
		blueLabel.setColor(Color4f.BLUE);
		blueLabel.setDropShadow(1);
		blueLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
		super.add(blueLabel);
	}

	public void update()
	{
		if (!parent.isRendered())
			return;

		super.update();
		QGGameMode mode = (QGGameMode) Ubercube.getInstance().getGameCore().getGame().getGameMode();
		redLabel.setText(mode.getRedTeam().getName() + " : " + mode.getRedScore());
		blueLabel.setText(mode.getBlueTeam().getName() + " : " + mode.getBlueScore());
	}
}

