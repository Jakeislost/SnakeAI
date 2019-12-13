package objects;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class Food extends GameObject{

	private Node node;
	
	public Food(Grid grid) {
		super(0, 0);
		
		/*
		 * create new food, place on an empty tile
		 * if it is on a current player-filled tile or another food, choose new location
		 * we store the node which is food so we can check if isFood is true or not,
		 * which means food and player objects do not communicate.
		 * which saves the game looping through every node every time the snake moves
		 */
		Random rand = new Random();
		x = rand.nextInt((int) grid.getTotalTileWidth()); y = rand.nextInt((int) grid.getTotalTileHeight());
		node = grid.getNode(x, y);
		while(!node.isFree()) {
			x = rand.nextInt((int) grid.getTotalTileWidth()); y = rand.nextInt((int) grid.getTotalTileHeight());
			node = grid.getNode(x, y);
		}
		//found an empty space, claim it as food!
		node.setFood(true);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g, Handler handler) {
		//render fruit to screen, black outline to make it stand out during AI path mode.
		g.setColor(Color.green);
		g.fillRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
		g.setColor(Color.black);
		g.setLineWidth(2);
		g.drawRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
		g.resetLineWidth();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame gsm, int delta, Handler handler) {
		//if it is no longer food, then the player has eaten it, therefore destroy this and create a new food.
		if(!node.isFood()) {
			handler.addObject(new Food(handler.getGrid()));
			handler.removeObject(this);
		}
	}

	@Override
	public void input(GameContainer gc, StateBasedGame gsm, Input in) {
		//no input needed
	}
	
	public Node getFoodNode() {
		return node;
	}

}
