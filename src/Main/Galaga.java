package Main;

import GameState.*;
import Utils.Stats;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Ethan Fisher
 * School: Franklin Academy HS
 */

public class Galaga {

    public static final int WINDOW_WIDTH = 720;
    public static final int WINDOW_HEIGHT = 960;

    public static final int MENU = 0;
    public static final int IN_GAME = 1;
    public static final int INSTRUCTIONS = 2;
    public static final int SHOP = 3;

    public static void main(String[] args) {
        Game game = new Game("Galaga");
        game.getWindow().setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        game.getWindow().setLocationRelativeTo(null);

        GameStats.createInstance();

        game.setStates(new GameState[] {
                new Menu(),
                new InGame(),
                new Instructions(),
                new Shop()
        });
        game.start(MENU);
    }
	
//	// default window sizes for when entities are drawn
//	public static final int WINDOW_WIDTH = 720;
//	public static final int WINDOW_HEIGHT = 960;
//
//	// start the game by making a new instance of galaga
//	public static void main(String[] args) { new Galaga(); }
//
//	public Galaga() {
//
//		// create a new window
//		JFrame window = new JFrame("Galaga");
//
//		// set up some basic stuff
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		// set the size to the height of the screen - 50 pixels
//		// the width is 0.75 times that. The
//		// minimum size will be half of the original size
//		int height = Toolkit.getDefaultToolkit().getScreenSize().height - 50;
//		window.setSize((int) (height * 0.75), height);
//		window.setMinimumSize(new Dimension(window.getWidth() / 2, window.getHeight() / 2));
//
//		// center the window
//		window.setLocationRelativeTo(null);
//
//		// add the key listener
//		window.addKeyListener(this);
//
//		// show the window
//		window.setVisible(true);
//
//		// create the game loop
//		new GameLoop(window);
//	}
//
//	/*
//	 * Key methods. For each one, I just send the info
//	 * to the Input class for handling
//	 */
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		Input.keyPressed(e.getKeyCode());
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		Input.keyReleased(e.getKeyCode());
//	}
//
//	@Override
//	public void keyTyped(KeyEvent e) {}

}
