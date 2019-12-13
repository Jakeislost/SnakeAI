package resources;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;

public class UIList {

	//list of buttons, also a container.
	
	private String name; // name so that we can have multiple lists.
	private int x, y, width, height, // pos and size
			buttonCount, //count of total buttons (used in calculations)
			spacing; //pixel space between buttons vertically.
	private ArrayList<UIButton> buttons = new ArrayList<UIButton>();
	
	public UIList(String text, int x, int y, int width, int height, int spacing) {
		this.name = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.spacing = spacing;
	}
	
	public void draw(Graphics g) {
		for(UIButton b : buttons) b.draw(g);
	}
	
	public void mouseOver(Point loc) {
		for(UIButton b : buttons) {
			if(b.getBounds().contains(loc)) b.setHighlighted(true);
			else b.setHighlighted(false);
		}
	}
	
	public void addButton(String text) {
		//adds a button to the list, underneath the previous button.
		buttons.add(new UIButton(text, x, y + buttonCount * (height + spacing), width, height));
		buttonCount++;
	}
	public void addFreeButton(String text, int x, int y, int w, int h) {
		//adds a button separate from the list but is kept in the array to help minimise extra code.
		buttons.add(new UIButton(text, x, y, w, h));
	}
	public void addFreeButton(String text, int x, int y) {
		//adds a button that has the width and height from the constructor
		addFreeButton(text, x, y, width, height);
	}
	public void removeButton(String text) {
		for(UIButton b : buttons) {
			if(b.getText() == text) {
				buttons.remove(b);
				return;
			}
		}
	}
	public void clearList() {
		buttons.clear();
		buttonCount = 0;
	}
	
	public UIButton getButton(String text) {
		for(UIButton b : buttons) {
			if(b.getText() == text) {
				return b;
			}
		}
		return null;
	}
	
	public boolean isPressed(String text, Point loc, Input IM) {
		for(UIButton b : buttons) {
			if(b.getText() == text && b.getBounds().contains(loc) && IM.isMousePressed(IM.MOUSE_LEFT_BUTTON)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ArrayList<UIButton> getButtons() {
		return buttons;
	}
	
	
}
