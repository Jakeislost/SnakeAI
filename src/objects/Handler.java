package objects;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Handler {

	private ArrayList<GameObject> objects;
	private Grid grid;
	
	//basic handler class that splits up classes and allows for infinite addition of objects.
	
	public Handler(Grid grid) {
		objects = new ArrayList<GameObject>();
		this.grid = grid;
	}
	
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g) {
		for(GameObject o : new ArrayList<GameObject>(objects)) o.render(gc, gsm, g, this);
	}
	public void update(GameContainer gc, StateBasedGame gsm, int delta) {
		for(GameObject o : new ArrayList<GameObject>(objects)) { 
			o.input(gc, gsm, gc.getInput());
			o.update(gc, gsm, delta, this);
		}
	}
	
	public ArrayList<GameObject> getObjectList() { return objects; }
	public Grid getGrid() { return grid; }
	
	public void addObject(GameObject o) {
		objects.add(o);
	}
	public void removeObject(GameObject o) {
		objects.remove(o);
	}
	
	public int countSnakes() {
		int snakes = 0;
		for(GameObject o : objects) {
			if(o instanceof Player) snakes++;
		}
		return snakes;
	}
	
}
