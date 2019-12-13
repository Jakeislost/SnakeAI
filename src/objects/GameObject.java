package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameObject {

	protected float x, y;
	
	/*
	 * abstract class so we can group objects together, useful for duplicating AI's or foods.
	 * This is so we can have AI's and Food in the same array
	 */
	
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void render(GameContainer gc, StateBasedGame gsm, Graphics g, Handler handler);
	public abstract void update(GameContainer gc, StateBasedGame gsm, int delta, Handler handler);
	public abstract void input(GameContainer gc, StateBasedGame gsm, Input in);

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	
	
}
