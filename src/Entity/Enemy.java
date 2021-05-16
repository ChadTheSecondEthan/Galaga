package Entity;

import GameState.GameState;

public class Enemy extends SpaceShip {
	
	// the chance that this enemy will shoot each second and the chance to drop a powerup
	private float shootChance;
	private float powerupChance;
	
	// difficulty of this enemy
	private int difficulty;

	public Enemy(GameState gameState, int difficulty, float x, float y) {
		super(gameState);
		
		this.difficulty = difficulty;
		this.x = x;
		this.y = y;
		width = height = 40;
		
		// 10% chance to shoot per second + 10% per difficulty level, 20% chance to drop a powerup
		shootChance = 0.1f + 0.1f * difficulty;
		powerupChance = 0.2f;
		
		// 25 pixels per second of speed
		speed = 25;
		
		// 1 life + 1 per level of difficulty
		lives = 1 + difficulty;
		
		// load the sprite
		drawable = new SpriteRenderer("sprites/Enemy1.png");
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		
		// shoot based on the random chance
		if (Math.random() < shootChance * dt) shoot();
		
		// switch directions every so often
		if (gameState.getGameLoop().stateTime() % 8 < 2f || gameState.getGameLoop().stateTime() % 8 > 6f)
			x += speed * dt;
		else 
			x -= speed * dt;
	}
	
	/** shoots a bullet */
	private void shoot() {
		
		// create a bullet at this position
		Bullet bullet = spawnBullet(x, y + height, true);
		
		// set the speed to be 400 plus 50 per level of difficulty
		bullet.setSpeed(400 + 50 * difficulty);
		
		// change shooting side
		shootLeft = !shootLeft;
	}
	
	/**
	 *  The amount of score the player gets from this enemy. 
	 *  Returns 100 + 50 per level of difficulty
	 */
	public int getScore() {
		return 100 + 50 * difficulty;
	}
	
	@Override
	public void destroy() {

		// update lives and flinching
		super.destroy();

		// don't destroy if this enemy is dead
		if (isAlive()) return;
		
		// check if a powerup should be dropped
		if (Math.random() < powerupChance) {
			Powerup powerup = new Powerup(gameState, x, y + height);
			gameState.spawn(powerup);
		}
	}

}
