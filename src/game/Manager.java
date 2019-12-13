package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.Game;
import states.Main;

public class Manager extends StateBasedGame{

	public Manager(String name) {
		super(name);
	}
	
	public static void main(String[] args) {
		//setup game container from slick2D library
		try {
			AppGameContainer game = new AppGameContainer(new Manager("Snake"));
			game.setTargetFrameRate(60);				// FPS for renderer
			game.setMaximumLogicUpdateInterval(60);		// How many times the Update() func is called.
			game.setVSync(true);
			game.setDisplayMode(1300, 1000, false);
			game.setAlwaysRender(true);
			game.start();		//starts Game.
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		//load states into the game (works via ID's, main is 0 therefore loads first and will show first)
		addState(new Main()); // main menu
		addState(new Game()); // main game state
	}

}
