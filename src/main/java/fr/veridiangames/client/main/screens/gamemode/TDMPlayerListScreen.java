package fr.veridiangames.client.main.screens.gamemode;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimi Vacarians on 01/09/2017.
 */
public class TDMPlayerListScreen extends GuiCanvas {

	private static final TrueTypeFont FONT = new TrueTypeFont(StaticFont.Kroftsmann(0, 20), true);

	private GuiCanvas parent;

	private GuiPanel redBG;
	private GuiPanel blueBG;
	private List<PLine> lines = new ArrayList<>();
	private boolean rendered = false;
	private TDMGameMode tdm = (TDMGameMode) GameCore.getInstance().getGame().getGameMode();

	public TDMPlayerListScreen(GuiCanvas parent) {
		super(parent);
		this.parent = parent;

		redBG = new GuiPanel(Display.getInstance().getWidth()/2-10, Display.getInstance().getHeight()/2, 250, 350);
		redBG.setOrigin(GuiComponent.GuiOrigin.RC);
		redBG.setScreenParent(GuiComponent.GuiCorner.CENTER);
		redBG.setColor(new Color4f(0, 0, 0, 0.35f));
		super.add(redBG);

		blueBG = new GuiPanel(Display.getInstance().getWidth()/2+10, Display.getInstance().getHeight()/2, 250, 350);
		blueBG.setOrigin(GuiComponent.GuiOrigin.LC);
		blueBG.setScreenParent(GuiComponent.GuiCorner.CENTER);
		blueBG.setColor(new Color4f(0, 0, 0, 0.35f));
		super.add(blueBG);


	}

	int time = 0;
	public void update(){

		if (!parent.isRendered())
			return;

		super.update();

		rendered = false;
		if (Display.getInstance().getInput().getKey(Input.KEY_TAB))
			rendered = true;


		redBG.setUseable(rendered);
		blueBG.setUseable(rendered);

		if (!rendered)
		{
			time = 0;
			return;
		}

		if (time % 60 == 0) {
			lines.clear();

			List<Integer> redTeam = new ArrayList<>();
			List<Integer> blueTeam = new ArrayList<>();

			for(int i : GameCore.getInstance().getGame().getEntityManager().getPlayerEntites()){
				Player p = (Player) GameCore.getInstance().getGame().getEntityManager().getEntities().get(i);
				if(tdm.getPlayerTeam(p.getID()) == tdm.getRedTeam()){
					redTeam.add(p.getID());
				}else{
					blueTeam.add(p.getID());
				}
			}

			int pn = 0;
			for (int i : redTeam) {
				Player p = (Player) GameCore.getInstance().getGame().getEntityManager().getEntities().get(i);
				PLine l = new PLine(p, redBG, pn);
				lines.add(l);
				pn++;
			}

			pn = 0;
			for (int i : blueTeam) {
				Player p = (Player) GameCore.getInstance().getGame().getEntityManager().getEntities().get(i);
				PLine l = new PLine(p, blueBG, pn);
				lines.add(l);
				pn++;
			}
			time = 0;
		}
		time++;
	}

	public void render(GuiShader gs){
		if (!rendered)
			return;

		for(PLine pl : lines){
			pl.render(gs);
		}
	}



	class PLine
	{
		FontRenderer nameLabel;
		FontRenderer pingLabel;
		String name;
		int ping;
		int x, y;
		Color4f pingColor;
		Color4f nameColor;

		public PLine(Player player, GuiComponent parent, int i)
		{
			this.name = player.getName();
			this.ping = 0;
			this.x = parent.getX();
			this.y = parent.getY() + i * 25;
			this.nameLabel = new FontRenderer(FONT, name, x + 5, y + 2);
			this.pingLabel = new FontRenderer(FONT, ping + " ms", x + parent.getW() - 5, y + 2);
			this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);
			this.pingColor = new Color4f(0f, 1f, 0f, 1f);
			this.nameColor = Color4f.WHITE;
			if (player.getTeam() != null)
				this.nameColor = player.getTeam().getColor();
		}

		void update(int x, int y, int ping, GuiComponent parent)
		{
			this.x = x;
			this.y = y;
			this.ping = ping;
			this.nameLabel.setPosition(x + 5, y + 2);

			this.pingLabel.setText(this.ping + " ms");
			this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);

			float r = (float)(this.ping - 50) / 50.0f;
			float g = 1.0f - r;
			if (r < 0) r = 0;
			if (r > 1) r = 1;
			if (g < 0) g = 0;
			if (g > 1) g = 1;

			this.pingColor.setRed(r);
			this.pingColor.setGreen(g);
		}

		void render(GuiShader shader)
		{
			this.nameLabel.render(shader, nameColor, 1);
			this.pingLabel.render(shader, pingColor, 1);
		}
	}
}
