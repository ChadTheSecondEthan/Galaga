package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import GameState.GameState;
import Main.Galaga;
import Utils.Resources;

public class Background extends Entity {
	
	// the speed at which the background scrolls
	private float scrollSpeed;

	public Background() { this(null); }

	public Background(String imgSrc) {
		super();
		
		// set it to the top of the screen
		setY(0);
		
		// 150 pixels / second
		scrollSpeed = 150;
		
		// load the image source provided
		drawable = new SpriteRenderer(imgSrc);
	}

	@Override
	public void update(float dt) {

		float y = getY();
		
		// move based on scroll speed and time
		y += scrollSpeed * dt;
		
		// keep the image within screen bounds
		y %= Galaga.WINDOW_HEIGHT;

		setY(y);
	}

	@Override
	public void draw(Graphics g) {

		// get the current image from the drawable
		BufferedImage sprite = ((SpriteRenderer) drawable).getImage();

		// draw it twice so the whole screen gets covered
		g.drawImage(sprite, 0, (int) getY(), Galaga.WINDOW_WIDTH, Galaga.WINDOW_HEIGHT, null);
		g.drawImage(sprite, 0, (int) getY() - Galaga.WINDOW_HEIGHT, Galaga.WINDOW_WIDTH, Galaga.WINDOW_HEIGHT, null);
	}

	@Override
	public boolean setAttribute(String attr, String value) {
		if (attr.toLowerCase().equals("src")) {
			((SpriteRenderer) drawable).setImage(value);
			return true;
		}

		return super.setAttribute(attr, value);
	}
}
