package Entity;

import java.util.ArrayList;

import GameState.GameState;
import Main.Galaga;

public class Powerup extends Entity {
	
	// types of powerups and the type of this powerup
	private static final int QUICK_FIRE = 0;
	private static final int DOUBLE_FIRE = 1;
	private static final int TRIPLE_FIRE = 2;
	private static final int SPEED_BOOST = 3;
	private static final int HEALTH_BOOST = 4;
	private int type;
	
	// powerup time
	private int time;
	
	// pixels per second
	private float speed;

	public Powerup(GameState gameState, float x, float y) {
		super(gameState);
		
		this.x = x;
		this.y = y;

		// set the width and height
		width = height = 36;
		
		// get a random type
		type = (int) (Math.random() * 4);
		
		// time is a random number from 5 (inclusive) - 10 (exclusive) seconds
		if (type != HEALTH_BOOST)
			time = (int) (Math.random() * 5 + 5);
		
		// speed in pixels per second
		speed = 200;
		
		// load sprite base on powerup
		drawable = new SpriteRenderer("sprites/Powerup" + type + ".png");
	}

	@Override
	public void update(float dt) {
		
		// update the y value of the powerup
		y += speed * dt;
		
		// destroy if it hits a wall
		if (y > Galaga.WINDOW_HEIGHT)
			destroy();
	
		// find the player entity
		for (Entity e : gameState.getEntities()) {
			
			// if the powerup touches a player, give the player the powerup
			if (e instanceof Player && intersects(e)) {
				destroy();
				applyPowerup((Player) e);
				return;
			}
		}
	}
	
	/** applys the powerup's effects to the player */
	private void applyPowerup(final Player player) {
		
		// add this powerup to the player
		player.addPowerup(this);
		
		// apply this powerup to the player
		switch (type) {
		case QUICK_FIRE:
			player.setShotTimeout(0.2f);
			break;
		case DOUBLE_FIRE:
			if (player.getBulletCount() != 3)
				player.setBulletCount(2);
			break;
		case TRIPLE_FIRE:
			player.setBulletCount(3);
			break;
		case SPEED_BOOST:
			player.setSpeed(500);
			break;
		case HEALTH_BOOST:
			player.setLives(player.getLives() + 1);
			break;
		}

		if (type == HEALTH_BOOST) return;
		
		// wait time seconds, then reset the powerup
		new Thread(() -> {

			// wait first
			try {
				Thread.sleep(time * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// reset the powerups
			resetPowerup(player);
		}).start();
		
	}
	
	/** resets the powerup's effects to the player */
	private void resetPowerup(final Player player) {
		
		// remove this powerup from the player
		player.removePowerup(this);

		// get the new list of powerups from the player
		ArrayList<Powerup> powerups = player.getPowerups();
		for (Powerup p : powerups)
			if (p.type == type)
				return;
		
		// depending on the type, reset a certain value of the player
		switch (type) {
		case QUICK_FIRE:
			player.resetShotTimeout();
			break;
		case DOUBLE_FIRE:
			for (Powerup p : powerups)
				if (p.type == TRIPLE_FIRE)
					return;

			player.resetBulletCount();;
			break;
		case TRIPLE_FIRE:
			for (Powerup p : powerups)
				if (p.type == DOUBLE_FIRE)
					return;

			player.resetBulletCount();;
			break;
		case SPEED_BOOST:
			player.resetSpeedBoost();
			break;
		}
	}
	
	@Override
	public String toString() {
		// returns a string based on the type of powerup
		switch (type) {
		case QUICK_FIRE:
			return "Quick Fire";
		case DOUBLE_FIRE:
			return "Double Fire";
		case TRIPLE_FIRE:
			return "Triple Fire";
		case SPEED_BOOST:
			return "Speed Boost";
		default:
			return null;
		}
	}

}
