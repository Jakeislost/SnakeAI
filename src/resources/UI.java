package resources;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

public class UI {

	/*
	 * custom UI container class that holds custom UI elements
	 * mostly buttons for this snake game due to UI not being highly
	 * useful. therefore scrolling and combo-boxes are not needed.
	 * JFrame is not directly compatible with Slick2D Game Engine
	 * So I can't use that for a main menu.
	 */
	
	private ArrayList<UIList> lists;
	
	public UI() {
		lists = new ArrayList<UIList>();
	}
	
	public void draw(Graphics g) {
		for(UIList l : lists) l.draw(g);
	}
	
	public void mouseOver(Point loc) {
		for(UIList l : lists) l.mouseOver(loc);
	}
	
	
	
	public void addList(String text, int x, int y, int w, int h, int s) {
		lists.add(new UIList(text, x, y, w, h, s));
	}
	public void removeList(String text) {
		for(UIList l : lists) {
			if(l.getName() == text) {
				lists.remove(l);
				return;
			}
		}
	}
	public UIList getList(String name) {
		for(UIList l : lists) {
			if(l.getName() == name) {
				return l;
			}
		}
		return null;
	}
	
}
