package states;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import resources.UI;

public class Main extends BasicGameState{
	
	private Input IM; // used for controls and mouse data.
	private UI ui; // custom UI Class
	
	private TrueTypeFont font;
	private String gameName = "Snake"; //Main Menu Title.
	
	@Override
	public void init(GameContainer gc, StateBasedGame gsm) throws SlickException {
		IM = gc.getInput();
		
		//create a UI list that is scaled with the window's size.
		ui = new UI();
		ui.addList("Main Menu", gc.getWidth() / 4, 500, (gc.getWidth() / 4) * 2, 60, 10);
		ui.getList("Main Menu").addButton("Play");
		ui.getList("Main Menu").addButton("Options");
		ui.getList("Main Menu").addButton("Quit");
		
		font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 50), true);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g) throws SlickException {
		ui.draw(g);
		g.setColor(Color.cyan);
		g.setFont(font);
		g.drawString(gameName, gc.getWidth() / 2 - (g.getFont().getWidth(gameName) / 2), 300);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame gsm, int delta) throws SlickException {
		//Mouse updates
		mouse(gc, gsm);
	}
	
	public void mouse(GameContainer gc, StateBasedGame gsm) {
		//test for button presses.
		Point loc = new Point(IM.getMouseX(), IM.getMouseY());
		ui.mouseOver(loc);
		if(ui.getList("Main Menu").isPressed("Quit", loc, IM)) {
			System.exit(0);
		} else if(ui.getList("Main Menu").isPressed("Play", loc, IM)) {
			gsm.enterState(1);
		}
		
		
	}

	@Override
	public int getID() {
		return 0;
	}

	

}
