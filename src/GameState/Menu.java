package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Entity.Background;
import Main.Galaga;
import Main.GameLoop;
import Utils.Input;

public class Menu extends GameState {
	
	// all of the options on the main menu
	private final String[] options = {
		"Start", "Instructions", "Quit"	
	};
	
	// font and colors for title and options
	private Font titleFont;
	private Color titleColor;
	private Font optionsFont;
	private Color optionsColor;
	
	// current option
	private int currentOption;

	public Menu(GameLoop gameLoop) {
		super(gameLoop);
		
		// spawn a background for the menu
		spawn(new Background(this, "sprites/Background.png"));
		
		// initialize fonts, colors, and current option
		titleFont = new Font("Arial", Font.BOLD, 36);
		titleColor = Color.white;
		optionsFont = new Font("Arial", Font.PLAIN, 24);
		optionsColor = Color.red;
		
		// the starting option is the first option
		currentOption = 0;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		
		// check if the user has selected an option
		if (Input.getKeyDown(KeyEvent.VK_ENTER)) {
			
			// play the game
			if (currentOption == 0)
				gameLoop.setState(new InGame(gameLoop));
			
			// instructions
			else if (currentOption == 1)
				gameLoop.setState(new Instructions(gameLoop));
			
			// close the game
			else 
				System.exit(0);
		}
		
		// check if the user has pressed the up key
		if (Input.getKeyDown(KeyEvent.VK_UP))
			currentOption = (currentOption == 0) ? (options.length - 1) : (currentOption - 1);
		
		// and the down key
		if (Input.getKeyDown(KeyEvent.VK_DOWN))
			currentOption = (currentOption + 1) % options.length;

		// close the window if they pressed escape
		if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		// draw the title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Galaga", (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth("Galaga")) / 2, 150);
		
		// draw the options
		g.setFont(optionsFont);
		for (int i = 0; i < options.length; i++) {
			
			// draw the current option as white
			if (currentOption == i)
				g.setColor(Color.white);
			else
				g.setColor(optionsColor);
			
			// draw the option in the middle of the screen
			g.drawString(options[i], (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth(options[i])) / 2, 450 + 50 * i);
		}
	}

}
