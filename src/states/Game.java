package states;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import objects.Food;
import objects.GameObject;
import objects.Grid;
import objects.Handler;
import objects.Node;
import objects.Player;
import resources.FrameTimer;
import resources.UI;

public class Game extends BasicGameState{

	private Handler handler;
	private Node lastNodeWalled = null; // boolean to help "paint" walls.
	
	private UI ui;
	
	@Override
	public void init(GameContainer gc, StateBasedGame gsm) throws SlickException {
		//create UI buttons for main menu.
		ui = new UI();
		ui.addList("Game", 1020, 600, 260, 50, 5);
		ui.getList("Game").addButton("New Game");
		ui.getList("Game").addButton("Pause");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame gsm, Graphics g) throws SlickException {
		if(handler != null) { 
			handler.getGrid().render(gc, gsm, g);
			handler.render(gc, gsm, g);
		}
		ui.draw(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame gsm, int delta) throws SlickException {
		if(handler != null)handler.update(gc, gsm, delta);
		input(gc, gsm, delta);
	}
	
	public void input(GameContainer gc, StateBasedGame gsm, int delta) {
		//input for things such as creating walls in real time and selecting AI.
		Input in = gc.getInput();
		if(handler != null) {
			//was the mouse pressed inside the game window?
			if(in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && in.getMouseX() < 1000) {
				if(in.isKeyDown(Input.KEY_LCONTROL)) {
					//was the mouse pressed whilst holding CTRL? create wall if so.
					Node node = handler.getGrid().getNodeByPosition(in.getMouseX(), in.getMouseY());
					if(node != null && node != lastNodeWalled && node.isFree()) { 
						if(node.isWall())node.setWall(false);
						else node.setWall(true);
						lastNodeWalled = node;
					}
				} else {
					//did the mouse click on a snake node?
					// if so then get the parent snake and set it to 'selected'
					// allows us to watch which food it is targeting and node values.
					Node node = handler.getGrid().getNodeByPosition(in.getMouseX(), in.getMouseY());
					if(node != null && node.isPlayer()) { 
						for(GameObject o : handler.getObjectList()) {
							if(o instanceof Player) {
								for(Node n : ((Player) o).getTail()) {
									if(node == n) {
										handler.getGrid().setSelectedPlayer((Player) o);
									}
								}
							}
						}
					} else {
						handler.getGrid().setSelectedPlayer(null);
					}
				}
			} else lastNodeWalled = null;
		}
		
		//UI to create the side menu, which lets the user start a new game
		//or pause to allow them to add walls or select snakes.
		Point loc = new Point(in.getMouseX(), in.getMouseY());
		ui.mouseOver(loc);
		if(ui.getList("Game").isPressed("New Game", loc, in)) newGame();
		else if(ui.getList("Game").isPressed("Pause", loc, in) && handler != null) {
			for(GameObject o : handler.getObjectList()) {
				if(o instanceof Player) {
					FrameTimer timer = ((Player) o).getTimer();
					if(timer.isPaused()) timer.start();
					else timer.pause();
				}
			}
		}
		//pause the game if space is presed.
		if(in.isKeyPressed(Input.KEY_SPACE)) {
			for(GameObject o : handler.getObjectList()) {
				if(o instanceof Player) {
					FrameTimer timer = ((Player) o).getTimer();
					if(timer.isPaused()) timer.start();
					else timer.pause();
				}
			}
		}
	}
	
	public void newGame() {
		/*
		 * Needed a quick basic UI that lets me input data
		 * to quickly customise games based on what I need to see
		 * such as Number of snakes, tail growth and more.
		 * So I used JFrame to create a window that lets me input data
		 * Currently data is not checked for the right format.
		 */
		
		Thread thread = new Thread() {
			public void run() {
				//create the JFrame
				JFrame options = new JFrame("New Game");
				GridBagLayout layout = new GridBagLayout();
				options.setLayout(layout);
				options.setSize(new Dimension(300, 200));
				options.setFocusable(true);
				options.setResizable(false);
				
				//Create the JComponents to put in the JFrame
				JLabel widthLabel = new JLabel("Grid Width:");
				JTextField widthText = new JTextField(15);
				widthText.setText("30");
				JLabel heightLabel = new JLabel("Grid Height:");
				JTextField heightText = new JTextField(15);
				heightText.setText("30");
				JLabel AOSLabel = new JLabel("Snakes:");
				JTextField AOSText = new JTextField(15);
				AOSText.setText("1");
				JLabel speedLabel = new JLabel("Speed:");
				JTextField speedText = new JTextField(15);
				speedText.setText("8");
				JLabel tailLabel = new JLabel("TailGrowth:");
				JTextField tailText = new JTextField(15);
				tailText.setText("3");
				JRadioButton loop = new JRadioButton("loop");
				
				//Use the GridBag Layout to display options correctly.
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weighty = 0.1;
				options.add(widthLabel, c);
				c.gridx = 1;
				c.gridy = 0;
				options.add(widthText, c);

				c.gridx = 0;
				c.gridy = 1;
				options.add(heightLabel, c);
				c.gridx = 1;
				c.gridy = 1;
				options.add(heightText, c);

				c.gridx = 0;
				c.gridy = 2;
				options.add(AOSLabel, c);
				c.gridx = 1;
				c.gridy = 2;
				options.add(AOSText, c);

				c.gridx = 0;
				c.gridy = 3;
				options.add(speedLabel, c);
				c.gridx = 1;
				c.gridy = 3;
				options.add(speedText, c);

				c.gridx = 0;
				c.gridy = 4;
				options.add(tailLabel, c);
				c.gridx = 1;
				c.gridy = 4;
				options.add(tailText, c);
				
				c.gridx = 0;
				c.gridy = 5;
				c.gridwidth = 2;
				options.add(loop, c);
				
				//create the submit button which needs to parse data through it.
				JButton submit = new JButton("Submit");	
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						options.dispose(); 		// get rid of the JFrame.
						setupHandler(Integer.parseInt(widthText.getText()),
								Integer.parseInt(heightText.getText()),
								Integer.parseInt(AOSText.getText()),
								Integer.parseInt(speedText.getText()),
								Integer.parseInt(tailText.getText()),
								loop.isSelected());
					}
				});
				
				c.gridx = 0;
				c.gridy = 6;
				c.gridwidth = 2;
				c.anchor = GridBagConstraints.CENTER;
				options.add(submit, c);
				
				options.setVisible(true);
			}
		};
		thread.start(); //display the JFrame
	}
	
	public void setupHandler(int width, int height, int snakes, int speed, int tail, boolean loop) {
		//Parse the data from the JFrame
		handler = new Handler(new Grid(0, 0, 1000, 1000, width, height, loop));
		Random rand = new Random(); // used for random colours.
		for(int i = 0; i < snakes; i++) { 
			Player snake = new Player(handler.getGrid(), true, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
			snake.setSpeed(speed);
			snake.setTailGrowth(tail);
			handler.addObject(snake);
		}
		for(int i = 0; i < snakes; i++) {
			handler.addObject(new Food(handler.getGrid()));
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}
