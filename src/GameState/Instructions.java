package GameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Main.Galaga;
import Main.GameLoop;
import Utils.Input;
import UI.Text;

public class Instructions extends GameState {
	
	// the array of instructions
	private static final String[] instructions = {
			"ESC: Back",
			"WASD or Arrow keys: Move",
			"F or P: Shoot"
	};

	public Instructions() {
		super();

		Text instructionsText = new Text("Instructions");
		instructionsText.setPosition(Galaga.WINDOW_WIDTH * 0.5f, 100);
		instructionsText.setColor(Color.red);
		instructionsText.spawn();

		for (int i = 0; i < instructions.length; i++) {
			Text text = new Text(instructions[i]);
			text.setPosition(100, 450 + 50 * i);
			text.setColor(Color.red);
			text.spawn();
		}
	}

	@Override
	public void init() {

	}
	
	@Override
	public void update(float dt) {
		
		// if the user pressed escape, take them to the home page
		if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
			setState(Galaga.MENU);
	}

}
