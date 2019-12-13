package objects;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.state.StateBasedGame;

import resources.FrameTimer;

public class Player extends GameObject{

	private FrameTimer timer; // frame timer to slow down snake to make it easier to watch
	
	/*
	 * direction is a number between 0-3, and each number dictates a direction
	 * which is stored as final vars. 
	 * curDir is to stop from spamming input whilst the game delay is high
	 * for example, during the 5 frames of delay, the player could
	 * start going up, and quickly press right then down which would make them go
	 * into themselves, therefore curdir is used to stop that.
	 */
	private final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;
	private int dir = RIGHT, curDir = dir;
	
	private ArrayList<Node> tail; // all 'player' nodes which is used to check for tail.
	private int tailLength = 3, // starting length, this var dictates length of the tail array.
				tailMul = 10, //amount of blocks to add after eating
				speed = 2; // number of frames per movement.
	
	private boolean AI, //player controlled or AI-controlled
				alive = true;
	private Color col = Color.red;
	
	public Player(Grid grid, float x, float y, boolean AI, Color col) {
		super(x,y);
		//add this to grid so we can have path scores.
		grid.addPlayer(this);
		this.col = col;
		Node n = grid.getNode(x, y);
		n.setPlayer(true);
		tail = new ArrayList<Node>();
		tail.add(n);
		
		this.AI = AI;
		
		timer = new FrameTimer(speed, false);
	}
	
	public Player(Grid grid, boolean AI, Color col) {
		super(0,0);
		
		//this constructor has no position input therefore we randomise location
		//if we spawn on another already spawned snake we try again.
		Random rand = new Random();
		x = rand.nextInt((int) grid.getTotalTileWidth());
		y = rand.nextInt((int) grid.getTotalTileHeight());
		Node n = grid.getNode(x, y);
		while(n.isPlayer()) {
			x = rand.nextInt((int) grid.getTotalTileWidth());
			y = rand.nextInt((int) grid.getTotalTileHeight());
			n = grid.getNode(x, y);
		}
		
		//add this to grid so we can have path scores.
		grid.addPlayer(this);
		this.col = col;
		n.setPlayer(true);
		tail = new ArrayList<Node>();
		tail.add(n);
		
		this.AI = AI;
		
		timer = new FrameTimer(speed, false);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g, Handler handler) {
		Node[][] nodes = handler.getGrid().getNodes();
		//draw snake
		for(Node n : tail) {
			g.setColor((n == tail.get(tail.size() - 1)) ? new Color(150,0,0) : col);
			g.fillRect(n.getX(), n.getY(), n.getWidth(), n.getHeight());
		}
		//render the AI path using yellow line that connects nodes
		//uses same algorithm so the path finding is always correct.
		//duplicated code here so that we dont physically move the snake whilst rendering
		if(AI) {
			g.setColor(Color.yellow);
			Node nextNode = tail.get(tail.size() - 1);
			Node curNode = nextNode;
			int nextDir = dir;
			float x = this.x;
			float y = this.y;
			boolean done = false;
			for(int i = 0; i < 30; i++) {
				nextDir = getDirection(nextNode, nextDir);
				if(nextDir == RIGHT) {
					nextNode = handler.getGrid().getNode(x + 1, y);
				} else if(nextDir == LEFT) {
					nextNode = handler.getGrid().getNode(x - 1, y);
				} else if(nextDir == UP) {
					nextNode = handler.getGrid().getNode(x, y - 1);
				} else if(nextDir == DOWN) {
					nextNode = handler.getGrid().getNode(x, y + 1);
				}
				if(nextNode != null) { 
					x = (nextNode.getX() - handler.getGrid().getX()) / handler.getGrid().getTileWidth();
					y = (nextNode.getY() - handler.getGrid().getY()) / handler.getGrid().getTileHeight();
					Line path = new Line(curNode.getX() + curNode.getWidth() / 2, curNode.getY() + curNode.getHeight() / 2, 
							nextNode.getX() + nextNode.getWidth() / 2, nextNode.getY() + nextNode.getHeight() / 2);
					if(path.length() < handler.getGrid().getTileWidth() * 2)g.draw(path);
					if(nextNode.isFood()) break;
				}
				curNode = nextNode;
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame gsm, int delta, Handler handler) {
		if(timer.isDone()) {
			timer.resetTimer();
			if(AI) {
				getShortestPath(handler); // update path scores.
			}
			
			//the path scores dictate dir, therefore just move in what direction we calculated
			if(dir == RIGHT) {
				if(!move(1, 0, handler)) delete(handler);
			} else if(dir == LEFT) {
				if(!move(-1, 0, handler)) delete(handler);
			} else if(dir == UP) {
				if(!move(0, -1, handler)) delete(handler);
			} else if(dir == DOWN) {
				if(!move(0, 1, handler)) delete(handler);
			}
			//remove tail since we just moved forward
			if(tail.size() > tailLength) {
				tail.get(0).setPlayer(false);
				tail.remove(0);
			}
			curDir = dir;
			
			
		} else timer.update();
		
		//DEBUG TEMP CODE
		Input in = gc.getInput();
		if(in.isKeyDown(Input.KEY_LSHIFT) && in.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Node n = handler.getGrid().getNodeByPosition(in.getMouseX(), in.getMouseY());
			if(n != null) System.out.println("Selected node's position for player : x : " + (n.getX() - handler.getGrid().getX()) / handler.getGrid().getTileWidth()
					+ " | y : " + (n.getY() - handler.getGrid().getY()) / handler.getGrid().getTileHeight()
					+ " | wall : " + n.isWall());
		}
	}

	@Override
	public void input(GameContainer gc, StateBasedGame gsm, Input in) {
		//input for non-AI
		if(!AI) {
			if(in.isKeyPressed(Input.KEY_D) && curDir != LEFT) {
				dir = RIGHT;
			} else if(in.isKeyPressed(Input.KEY_A) && curDir != RIGHT) {
				dir = LEFT;
			} else if(in.isKeyPressed(Input.KEY_W) && curDir != DOWN) {
				dir = UP;
			} else if(in.isKeyPressed(Input.KEY_S) && curDir != UP) {
				dir = DOWN;
			}
		}
		
	}
	
	public void delete(Handler handler) {
		//called on death
		for(Node n : tail) {
			n.setPlayer(false);
		}
		alive = false;
		handler.removeObject(this);
	}
	
	public int getDirection(Node loc, int dir) {
		
		int prevBody = this.getOppositeDirection(dir);
		if(loc == null) return dir;
		Node closest = null;
		for(int i = 0; i < loc.getAdjacent().length; i++) {
			Node n = loc.getAdjacent()[i];
			if(i != prevBody && n != null && n.getScore(this) > 0) {
				if(closest == null) {
					dir = i;
					closest = n;
				} else {
					if(n.getScore(this) < closest.getScore(this)) { 
						dir = i;
						closest = n;
					}
				}
			}
		}
		if(closest == null) {
			for(int i = 0; i < loc.getAdjacent().length; i++) {
				Node n = loc.getAdjacent()[i];
				if(i != prevBody && n != null && n.getScore(this) != -1) {
					int empty = 0;
					for(Node adj : n.getAdjacent()) {
						if(adj != null && adj.getScore(this) != -1) empty++;
					}
					if(empty != 0)dir = i;
				}
			}
		}
		return dir;
	}
	
	public int getOppositeDirection(int dir) {
		if(dir == LEFT) return RIGHT;
		else if(dir == RIGHT) return LEFT;
		else if(dir == UP) return DOWN;
		else if(dir == DOWN) return UP;
		return 0;
	}
	
	public void getShortestPath(Handler handler) {
		/*
		 * this algorith calculates scores 'radiating' from destination to find a path
		 * for example
		 * a node mostly has 4 adjacent nodes,
		 * the destination(food) is scored at 0
		 * the adjacent 4 nodes of the destination get a score of 1,
		 * those 4 nodes have adjacents
		 * which get a score of 2
		 * etc
		 * this creates a downward 'hill' from the player
		 * meaning the player just has to choose the lowest number
		 * which will lead to the food
		 * as it is located at 0
		 * 
		 * however since path nodes that can not be reached due to other snakes bodies
		 * or its own tail, they would have a score of 0 as they have no adjacent nodes that lead to the destination,
		 * 0 is obviously lower than the downward hill that would most likely be above 5
		 * therefore we split the algorithm into checking nodes at score 1 and above
		 * and if there are nodes that have a score of 0, it means the snake is trapped, therefore we
		 * repeat the algorithm in order to stay alive the longest but this time chose a node at a score of 0.
		 * 
		 * we calculate the score for everey food, and go for the one with the smallest amount of steps.
		 */
		Node[][] nodes = handler.getGrid().getNodes();
		Node bestFood = null;
		int topScore = 1000;
		
		for(GameObject o : handler.getObjectList()) {
			if(o instanceof Food) {
				Node curNode = ((Food) o).getFoodNode();
				if(curNode.isFood()) {
					handler.getGrid().resetScore(this);
					calculateScore(curNode);
					int curScore = 0;
					for(Node n : tail.get(tail.size() - 1).getAdjacent()) {
						if(n != null) curScore = Math.max(curScore, n.getScore(this));
					}
					if(curScore != 0 && curScore < topScore) {
						topScore = curScore;
						bestFood = curNode;
					 
					}
				}
			}
		}
		handler.getGrid().resetScore(this);
		calculateScore(bestFood);
		dir = getDirection(tail.get(tail.size() - 1), dir);
	}
	
	public void getClosestFood(Handler handler) {
		//Find the food
		Node[][] nodes = handler.getGrid().getNodes();
		Node closest = null;
		Line dist = null;
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				Node n = nodes[i][j];
				if(n.isFood()) {
					if(closest == null) { 
						closest = n;
						dist = new Line(tail.get(tail.size() - 1).getX(), tail.get(tail.size() - 1).getY(), closest.getX(), closest.getY());
					}
					else {
						Line testLine = new Line(tail.get(tail.size() - 1).getX(), tail.get(tail.size() - 1).getY(), n.getX(), n.getY());
						if(testLine.length() < dist.length()) { 
							dist = testLine;
							closest = n;
						}
					}
				}
			}
		}
		handler.getGrid().resetScore(this);
		calculateScore(closest);
		dir = getDirection(tail.get(tail.size() - 1), dir);
	}
	
	public void calculateScore(Node food) {
		if(food == null) return;
		boolean done = false;
		int curScore = 1;
		food.setScore(this, 1);
		ArrayList<Node> adj = new ArrayList<Node>(), next = new ArrayList<Node>();
		adj.add(food);
		
		while(!done) {
			for(Node n : adj) {
				Node[] adjacent = n.getAdjacent();
				for(Node nadj : adjacent) {
					if(nadj != null && nadj.getScore(this) == 0) {
						nadj.setScore(this, curScore + 1);
						next.add(nadj);
					}
				}
			}
			if(next.size() == 0) done = true;
			else {
				adj = new ArrayList<Node>(next);
				next.clear();
				
				curScore++;
			}
		}
	}
	
	public boolean move(float xDif, float yDif, Handler handler) {
		if(handler.getGrid().getNode(x + xDif, y + yDif) != null) {
			Node nextNode = handler.getGrid().getNode(x + xDif, y + yDif);
			if(nextNode.isFood()) {
				tailLength += tailMul;
				nextNode.setFood(false);
				
			} else if(nextNode.isWall()) {
				this.delete(handler);
			}
			if(!checkForBody(nextNode, handler)) {
				nextNode.setPlayer(true);
				
				tail.add(nextNode);
				x = (tail.get(tail.size() - 1).getX() - handler.getGrid().getX()) / handler.getGrid().getTileWidth();
				y = (tail.get(tail.size() - 1).getY() - handler.getGrid().getY()) / handler.getGrid().getTileHeight();
				return true;
			}
		}
		return false;
	}
	
	private boolean checkForBody(Node head, Handler handler) {
		Node[][] nodes = handler.getGrid().getNodes();
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				Node n = nodes[i][j];
				if(head == n && n.isPlayer()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<Node> getTail() { return tail; }
	public Color getCol() { return col; }
	public FrameTimer getTimer() { return timer; }
	public boolean isAlive() { return alive; }
	public String toString() { return "Player(" + this.hashCode() + ") Colour: + " + col + " | Alive: + " + alive + " | Tail: " + tail.size() + " | AI: " + AI + " | x: " + x + " | y: " + y; }

	public void setSpeed(int speed) {
		this.speed = speed;
		timer = new FrameTimer(speed, false);
	}
	public void setTailGrowth(int tail) {
		tailMul = tail;
	}
	
}
