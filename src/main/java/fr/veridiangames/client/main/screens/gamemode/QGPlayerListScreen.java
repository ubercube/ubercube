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
import fr.veridiangames.core.game.gamemodes.PlayerStats;
import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;

public class QGPlayerListScreen extends GuiCanvas
{

	private static final TrueTypeFont FONT = new TrueTypeFont(StaticFont.Kroftsmann(0, 20), true);

	private GuiCanvas parent;

	private GuiPanel redHeadBG;
	private GuiPanel redBG;

	private GuiPanel blueHeadBG;
	private GuiPanel blueBG;

	private List<PLine> lines = new ArrayList<>();
	private boolean rendered = false;
	private QGGameMode qg = (QGGameMode) GameCore.getInstance().getGame().getGameMode();

	public QGPlayerListScreen(GuiCanvas parent) {
		super(parent);
		this.parent = parent;

		redHeadBG = new GuiPanel(Display.getInstance().getWidth()/2-10, Display.getInstance().getHeight()/2-196, 300, 31);
		redHeadBG.setOrigin(GuiComponent.GuiOrigin.RC);
		redHeadBG.setScreenParent(GuiComponent.GuiCorner.CENTER);
		redHeadBG.setColor(new Color4f(0, 0, 0, 0.35f));
		super.add(redHeadBG);

		redBG = new GuiPanel(Display.getInstance().getWidth()/2-10, Display.getInstance().getHeight()/2, 300, 350);
		redBG.setOrigin(GuiComponent.GuiOrigin.RC);
		redBG.setScreenParent(GuiComponent.GuiCorner.CENTER);
		redBG.setColor(new Color4f(0, 0, 0, 0.35f));
		super.add(redBG);

		blueHeadBG = new GuiPanel(Display.getInstance().getWidth()/2+10, Display.getInstance().getHeight()/2-196, 300, 31);
		blueHeadBG.setOrigin(GuiComponent.GuiOrigin.LC);
		blueHeadBG.setScreenParent(GuiComponent.GuiCorner.CENTER);
		blueHeadBG.setColor(new Color4f(0, 0, 0, 0.35f));
		super.add(blueHeadBG);

		blueBG = new GuiPanel(Display.getInstance().getWidth()/2+10, Display.getInstance().getHeight()/2, 300, 350);
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


		redHeadBG.setUseable(rendered);
		redBG.setUseable(rendered);
		blueHeadBG.setUseable(rendered);
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
				if(qg.getPlayerTeam(p.getID()) == qg.getRedTeam()){
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

		SLine red = new SLine(redHeadBG);
		SLine blue = new SLine(blueHeadBG);
		red.render(gs);
		blue.render(gs);

/*
		System.out.println("--------------------------------------");
		PlayerStats ps = GameCore.getInstance().getGame().getGameMode().getPlayerStats();

		for(Map.Entry<Integer, HashMap<PlayerStats.Stats, Object>> e : ps.get().entrySet()){
			System.out.println("Key : " + e.getKey() + " Value :" + e.getValue());
		}
		System.out.println("--------------------------------------");*/


		for(PLine pl : lines){
			pl.render(gs);
		}
	}



	class PLine
	{
		FontRenderer nameLabel;
		FontRenderer pingLabel;
		FontRenderer killsLabel;
		FontRenderer deathsLabel;
		String name;
		int ping;
		int x, y;
		Color4f pingColor;
		Color4f nameColor;

		int kills;
		int deaths;

		public PLine(Player player, GuiComponent parent, int i)
		{
			this.name = player.getName();
			this.ping = player.getPing();
			this.x = parent.getX();
			this.y = parent.getY() + i * 25;
			this.nameLabel = new FontRenderer(FONT, name, x + 5, y + 2);
			this.pingLabel = new FontRenderer(FONT, ping + " ms", x + parent.getW() - 5, y + 2);
			this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);
			this.pingColor = new Color4f(0f, 1f, 0f, 1f);
			this.nameColor = Color4f.WHITE;
			if (player.getTeam() != null)
				this.nameColor = player.getTeam().getColor();

			try {
				this.kills = (int) GameCore.getInstance().getGame().getGameMode().getPlayerStats().get(player.getID()).get(PlayerStats.Stats.KILLS);
				this.deaths = (int) GameCore.getInstance().getGame().getGameMode().getPlayerStats().get(player.getID()).get(PlayerStats.Stats.DEATHS);
			} catch(Exception e){
				System.out.println("Null pointer on player stats !");
				kills = 0;
				deaths = 0;
			}
			this.killsLabel = new FontRenderer(FONT, "" +  + kills, x + parent.getW() - 150, y + 2);
			this.deathsLabel = new FontRenderer(FONT, "" + deaths, x + parent.getW() - 110, y + 2);
		}

		void render(GuiShader shader)
		{
			this.nameLabel.render(shader, nameColor, 1);
			this.pingLabel.render(shader, pingColor, 1);
			this.killsLabel.render(shader, Color4f.WHITE, 1);
			this.deathsLabel.render(shader, Color4f.WHITE, 1);
		}
	}

	class SLine{

		FontRenderer nameLabel;
		FontRenderer killsLabel;
		FontRenderer deathsLabel;
		FontRenderer pingLabel;

		int x, y;

		public SLine(GuiComponent parent){
			this.x = parent.getX();
			this.y = parent.getY();
			this.nameLabel = new FontRenderer(FONT, "Name", x + 2, y + 2);
			this.killsLabel = new FontRenderer(FONT, "K", x + parent.getW() - 150, y + 2);
			this.deathsLabel = new FontRenderer(FONT, "D", x + parent.getW() - 110, y + 2);
			this.pingLabel = new FontRenderer(FONT, "Ping", x + parent.getW() - 2, y + 2);
			this.pingLabel.setPosition(x + parent.getW() - 5 - pingLabel.getWidth(), y + 2);
		}

		void render(GuiShader shader)
		{
			this.nameLabel.render(shader, Color4f.WHITE, 1);
			this.killsLabel.render(shader, Color4f.WHITE, 1);
			this.deathsLabel.render(shader, Color4f.WHITE, 1);
			this.pingLabel.render(shader, Color4f.WHITE, 1);
		}
	}
}
