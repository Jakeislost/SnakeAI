package objects;

import java.util.HashMap;

public class Node {

	private float x, y, w, h; // place and size of node
	private boolean free = true; // is it free and not a wall, player or food?
	private boolean player = false, food = false, wall = false; // type booleans (could be an enum?)
	
	private HashMap<Player, Integer> score; //storage for individual scores for every AI I spawn.
	
	private Node[] adjacent; //adjacent nodes to this one. (E.G: north, east, south, west)
	
	public Node(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		score = new HashMap<Player, Integer>();
	}
	
	public void addPlayerData(Player player) {
		score.put(player, 0);
	}
	
	public boolean isFree() { return free; }
	public void setFree(boolean free) { this.free = free; }
	public boolean isPlayer() { return player; }
	public void setPlayer(boolean player) { this.player = player; free = (player) ? false : true;}
	public boolean isFood() { return food; }
	public void setFood(boolean food) { this.food = food; free = (food) ? false : true;}
	public boolean isWall() { return wall; }
	public void setWall(boolean wall) { this.wall = wall; free = (wall) ? false : true;}
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return w; }
	public float getHeight() { return h; }
	
	public void setScore(Player player, int score) { this.score.replace(player, score); }
	public int getScore(Player player) { return score.get(player); }
	
	public void resetScore(Player player) {
		score.replace(player, 0);
		if(this.player || wall) score.replace(player, -1);
	}
	
	
	public void setAdjacaent(Node right, Node down, Node left, Node up) {		
		adjacent = new Node[4];
		adjacent[0] = right;
		adjacent[1] = down;
		adjacent[2] = left;
		adjacent[3] = up;
	}
	public void setAdjacaent(Node[] adj) {
		adjacent = adj;
	}
	public Node getAbove() {
		return adjacent[3];
	}
	public Node getLeft() {
		return adjacent[2];
	}
	public Node getRight() {
		return adjacent[0];
	}
	public Node getBelow() {
		return adjacent[1];
	}
	public Node[] getAdjacent() {
		return adjacent;
	}
	
	//debug
	@Override
	public String toString() {
		return "Node | X : " + x + " | Y : " + y + " | W : " + w + " | H : " + h;
	}
}
