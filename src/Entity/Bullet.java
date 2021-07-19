package Entity;

import GameState.GameState;
import Main.Galaga;

public class Bullet extends Entity {
	
	// does the bullet go up or down?
	private boolean goesDown;
	
	// the entity that the bullet hit
	private Entity hitEntity;
	
	// the speed of the bullet
	private float speed;

	/** create a bullet given the game state, a starting position, and a direction */
	public Bullet(boolean goesDown) {
		super();
		
		this.goesDown = goesDown;
		
		// hasn't initially hit an entity
		hitEntity = null;
		
		// 400 pixels / second, size of 10
		speed = 400;
		width = 10;
		height = goesDown ? -10 : 10;
		
		// load the sprite
		drawable = new SpriteRenderer("sprites/Bullet.png");
	}

	@Override
	public void update(float dt) {
		
		// update the y value of the bullet based on the speed and direction
		setRelativeY(getRelativeY() + speed * dt * (goesDown ? 1 : -1));
		
		// destroy if it hits a wall
		if (getAbsoluteY() < 0 || getAbsoluteY() > Galaga.WINDOW_HEIGHT)
			destroy();
	
		// destroy if it hits an entity
		for (Entity e : GameState.current().getEntities()) {
			
			// it cannot hit itself or another bullet
			if (e instanceof Bullet) continue;
			
			// if the bullet is not going down, it was shot by the player
			// and therefore can only destroy an enemy
			if (!goesDown && !(e instanceof Enemy)) continue;
			
			// if the bullet is going down, it was shot by an enemy
			// and therefore can only destroy the player
			if (goesDown && !(e instanceof Player)) continue;
			
			// if the bullet touches another entity, destroy both of them
			if (intersects(e)) {
				hitEntity = e;
				e.destroy();
				destroy();
				return;
			}
		}
	}
	
	/** returns the entity that was hit if there is one */
	public Entity hitEntity() { return hitEntity; }
	
	/** allow a change in the bullet's speed in pixels per second */
	public void setSpeed(int pixelsPerSecond) { speed = pixelsPerSecond; }
}
