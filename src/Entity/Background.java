package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import GameState.GameState;
import Main.Galaga;

public class Background extends Entity {
	
	// the speed at which the background scrolls
	private float scrollSpeed;

	public Background(GameState gameState, String imgSrc) {
		super(gameState);
		
		// set it to the top of the screen
		y = 0;
		
		// 300 pixels / second
		scrollSpeed = 150;
		
		// load the image source provided
		drawable = new SpriteRenderer(imgSrc);
	}

	@Override
	public void update(float dt) {
		
		// move based on scroll speed and time
		y += scrollSpeed * dt;
		
		// keep the image within screen bounds
		y %= Galaga.WINDOW_HEIGHT;

	}

	@Override
	public void draw(Graphics g) {

		// get the current image from the drawable
		BufferedImage sprite = drawable.getImage();

		// draw it twice so the whole screen gets covered
		g.drawImage(sprite, 0, (int) y, Galaga.WINDOW_WIDTH, Galaga.WINDOW_HEIGHT, null);
		g.drawImage(sprite, 0, (int) y - Galaga.WINDOW_HEIGHT, Galaga.WINDOW_WIDTH, Galaga.WINDOW_HEIGHT, null);
	}

}
