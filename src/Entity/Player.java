package Entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import GameState.GameState;
import GameState.InGame;
import Main.Galaga;
import Utils.Input;

public class Player extends SpaceShip {
	
	// the minimum height value the player can have
	private static final int MIN_HEIGHT = 650;

	// the maximum number of lives the player can have
	private static final int MAX_LIVES = 5;
	
	// the different powerups affecting the player
	private ArrayList<Powerup> powerups;
	
	// score variable
	private int score;
	
	// shooting variables
	private float shotTimeout;
	private float timeBetweenLastShot;

	public Player(GameState gameState) {
		super(gameState);
		
		// set the bounds of the player with x in the center
		width = height = 50;
		x = (Galaga.WINDOW_WIDTH - width) / 2.f;
		y = Galaga.WINDOW_HEIGHT - 100;
		
		// create powerups array list
		powerups = new ArrayList<>();
		
		// can move 300 pixels per second, 3 lives to start
		speed = 300;
		score = 0;
		lives = 3;
		
		// the player starts off not flinching
		flinching = false;
		flinchTime = 0;
		
		// start by shooting on the left
		shootLeft = true;
		
		// 1 bullet per shot to start
		bulletCount = 1;
		
		// shoot twice a second at max
		shotTimeout = 0.5f;
		timeBetweenLastShot = 0;
		
		// load the sprite
		drawable = new SpriteRenderer("sprites/Player.png");
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		
		// update the shoot timer
		timeBetweenLastShot += dt;
		
		// move the player based on inputs
		if (Input.getKey(KeyEvent.VK_RIGHT) || Input.getKey(KeyEvent.VK_D))
			x += speed * dt;
		if (Input.getKey(KeyEvent.VK_LEFT) || Input.getKey(KeyEvent.VK_A))
			x -= speed * dt;
		if (Input.getKey(KeyEvent.VK_UP) || Input.getKey(KeyEvent.VK_W))
			y -= speed * dt;
		if (Input.getKey(KeyEvent.VK_DOWN) || Input.getKey(KeyEvent.VK_S))
			y += speed * dt;
		
		// shoot with f or j
		if (Input.getKey(KeyEvent.VK_F) || Input.getKey(KeyEvent.VK_J))
			shoot();
		
		// keep the player within the screen
		clampPosition();
	}
	
	/** shoots a bullet if possible */
	private void shoot() {
		
		// if the time waited is not long enough, don't shoot
		if (timeBetweenLastShot < shotTimeout) return;
		
		// check bullet count
		if (bulletCount == 1) 
			
			// spawn the bullet based on whether or not the shot
			// is from the left or right sides
			shoot(shootLeft ? (x - 2) : (x + 42));
			
		else if (bulletCount == 2) {
			
			// spawn bullets on both sides
			shoot(x - 2);
			shoot(x + 42);
			
		} else if (bulletCount == 3) {
			
			// spawn bullets on both sides and the center
			shoot(x - 2);
			shoot(x + 22);
			shoot(x + 42);
			
		} else {

			// spawn a bullet for each bullet count
			for (int i = 0; i < bulletCount; i++) {
				shoot(x + 22 - (bulletCount - 1) * 10 + i * 20);
			}

		}
		
		// if it can shoot, reset the timer
		timeBetweenLastShot = 0;
		
		// swap shooting sides
		shootLeft = !shootLeft;
	}
	
	/** spawns in a bullet at the desired x position */
	private void shoot(float x) {
		
		// create a bullet from the spaceship's spawnBullet method
		Bullet bullet = spawnBullet(x, y, false);
		
		// add score when it is destroyed
		bullet.setOnDestroy(() -> {
			
			// if the bullet killed an enemy, add to the score
			// and also allow the game state to check the
			// number of enemies so the next wave can be spawned
			if (bullet.hitEntity() != null) {
				Enemy enemy = (Enemy) bullet.hitEntity();
				if (enemy.isAlive())
					return;
				score += enemy.getScore();
				((InGame) gameState).checkEnemyCount();
			}
		});
	}

	/** keeps the player within the bounds of the screen */
	private void clampPosition() {

		// if x is outside the x bounds, snap it back
		if (x < 0) x = 0;
		else if (x > Galaga.WINDOW_WIDTH - width)
			x = Galaga.WINDOW_WIDTH - width;

		// if y is outside the y bounds, snap it back
		if (y < MIN_HEIGHT) y = MIN_HEIGHT;
		else if (y > Galaga.WINDOW_HEIGHT - height)
			y = Galaga.WINDOW_HEIGHT - height;
	}
	
	/** returns the player's score */
	public int getScore() { return score; }
	
	/** returns the player's lives */
	public int getLives() { return lives; }
	/** sets the player's lives */
	void setLives(int lives) { this.lives = Math.min(lives, MAX_LIVES); }
	
	/** returns the powerups affecting the player */
	public ArrayList<Powerup> getPowerups() { return powerups; }
	
	/** adds a powerup to the player */
	void addPowerup(Powerup powerup) { powerups.add(powerup); }
	/** removes a powerup from the player */
	void removePowerup(Powerup powerup) { powerups.remove(powerup); }
	
	/** change the fire rate */
	void setShotTimeout(float timeout) { shotTimeout = timeout; }
	/** reset the fire rate */
	void resetShotTimeout() { shotTimeout = 0.5f; }
	
	/** reset the bullet count */
	void resetBulletCount() { bulletCount = 1; }
	
	/** reset speed */
	void resetSpeedBoost() { speed = 300; }

}
