package GameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Main.Galaga;
import Main.GameLoop;
import Utils.Input;

public class Instructions extends GameState {
	
	// the array of instructions
	private static final String[] instructions = {
			"ESC: Back",
			"WASD or Arrow keys: Move",
			"F or P: Shoot"
	};

	Instructions(GameLoop gameLoop) {
		super(gameLoop);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		// if the user pressed escape, take them to the home page
		if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
			gameLoop.setState(new Menu(gameLoop));
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		g.setColor(Color.red);
		g.drawString("Instructions", (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth("Instructions")) / 2, 100);
		
		for (int i = 0; i < instructions.length; i++)
			g.drawString(instructions[i], 100, 450 + 50 * i);
	}

}
