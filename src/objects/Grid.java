package objects;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Grid {
	
	private float x, y, // start offset
				w, h, //total width and height
				tw, th, //individual tile width and height
				gw, gh; //total blocks in the grid width and height
	private Node[][] nodes; // array of nodes
	private boolean loop; // does the snake go from one side of the map to the other when it hits screen edge
	
	private ArrayList<Player> players;  // list of players for path rendering
	private Player selectedPlayer = null; // selected player for path rendering
	private int pathBrightness = 2; // multiplier for tile score when snake is selected
	private TrueTypeFont font;
	
	public Grid(float x, float y, float w, float h, int gw, int gh, boolean loop) {
		this.x = x;
		this.y = y;
		this.gw = gw;
		this.gh = gh;
		tw = w / gw;
		th = h / gh;
		this.w = w;
		this.h = h;
		this.loop = loop;
		
		//create new node according to constructor details
		nodes = new Node[gw][gh];
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				nodes[i][j] = new Node(x + (i * tw), y + (j * th), tw, th);
			}
		}
		//setup adjacents
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				/*
				 * gets the adjacent nodes from one specific point
				 * 'getnode()' has automatic null return for outofbounds
				 * Right, Down, Left, Up
				 */
				nodes[i][j].setAdjacaent(getNode(i+1, j), getNode(i, j+1), getNode(i-1, j), getNode(i, j-1));
			}
		}
		
		players = new ArrayList<Player>();
		
	}
	
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g) {
		if(font == null) font = new TrueTypeFont(new Font("Verdana", Font.BOLD, 30), false); // work around for an error when there is no openGL context.
		g.setColor(Color.white);
		g.drawRect(x, y, w, h);
		
		//render the selected AI path if we have an AI selected
		if(selectedPlayer != null && selectedPlayer.isAlive()) {
			for(int i = 0 ; i < nodes.length; i++) {
				for(int j = 0; j < nodes[0].length; j++) {
					Node n = nodes[i][j];
					g.setColor(new Color(n.getScore(selectedPlayer) * pathBrightness, n.getScore(selectedPlayer) * pathBrightness, n.getScore(selectedPlayer) * pathBrightness));
					g.fillRect(n.getX(), n.getY(), n.getWidth(), n.getHeight());
				}
			}
		}
		//Render walls that block the snake
		for(int i = 0 ; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				Node n = nodes[i][j];
				if(n.isWall()) {
					g.setColor(Color.white);
					g.fillRect(n.getX(), n.getY(), n.getWidth(), n.getHeight());
				}
			}
		}
		
		//draw the scores to the UI per player
		int space = (int) (gc.getWidth() - w);
		//g.setFont(font);
		g.setColor(Color.lightGray);
		g.drawString("Scores", w + space / 2 - g.getFont().getWidth("Scores") / 2, 10);
		for(int i = 0; i < players.size(); i++) {
			Player cur = players.get(i);
			g.setColor(cur.getCol());
			String score = "" + cur.getTail().size();
			if(!cur.isAlive()) score = "X " + score + " X";
			g.drawString(score,  w + space / 2 - g.getFont().getWidth(score) / 2, 50 + i * 30);
		}
	}
	
	public void resetScore(Player player) {
		//hashmap in the node has scores for individual AI therefore needs the player object to find their score.
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				nodes[i][j].resetScore(player);
			}
		}
	}
	public void addPlayer(Player player) {
		//add player to hashmap in the node map (for seperate path-scores)
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[0].length; j++) {
				nodes[i][j].addPlayerData(player);
			}
		}
		players.add(player);
	}
	public void setSelectedPlayer(Player player) {
		//choose AI to start rendering path.
		this.selectedPlayer = player;
	}
	
	public Node[][] getNodes() {
		return nodes;
	}
	public Node getNode(float x, float y) {
		/*
		 * loop around edges (stops Null from being returned if looped turned on)
		 * returns null if loop is false and is out of bounds to avoid nullPointerException
		 * this returns a node from an array coordinate
		 */
		if(loop) {
			if((int)x > nodes.length - 1) x = 0;
			else if(x < 0) x = nodes.length - 1;
			if((int)y > nodes[0].length - 1) y = 0;
			else if(y < 0) y = nodes[0].length - 1;
		} else if(x > nodes.length - 1 || y > nodes[0].length - 1 || x < 0 || y < 0) return null;
		return nodes[(int)x][(int)y];
	}
	public Node getNodeByPosition(float x, float y) {
		//returns node based on pixal coordinate
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes.length; j++) {
				Node curNode = nodes[i][j];
				if(new Rectangle(curNode.getX(), curNode.getY(), curNode.getWidth(), curNode.getHeight()).contains(x, y)) {
					return curNode;
				}
			}
		}
		return null;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return w;
	}

	public float getHeight() {
		return h;
	}
	
	public float getTileWidth() {
		return tw;
	}

	public float getTileHeight() {
		return th;
	}

	public float getTotalTileWidth() {
		return gw;
	}
	public float getTotalTileHeight() {
		return gh;
	}
	
	
	
}
