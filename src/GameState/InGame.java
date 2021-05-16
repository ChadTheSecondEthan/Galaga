package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.*;
import Main.Galaga;
import Main.GameLoop;
import Utils.Functions;
import Utils.Input;

public class InGame extends GameState {
	
	// strings for winning and losing
	private static final String winString = "You Win! Press ESC to go back or R to restart";
	private static final String loseString = "You Lose. Press ESC to go back or R to restart";
	
	// has the game been won?
	private boolean gameWon;

	// current wave
	private int wave;

	// the player
	private Player player;

	// font and color for displaying the score
	private Font scoreFont;
	private Color scoreColor;

	InGame(GameLoop gameLoop) {
		super(gameLoop);
		
		// the game is not already won
		gameWon = false;

		// set current wave
		wave = 0;

		// spawn the background, player, and first wave
		spawn(new Background(this, "sprites/Background.png"));
		spawn(player = new Player(this));
		spawnWave(getCurrentWave());

		// initialize font and color
		scoreFont = new Font("Arial", Font.BOLD, 24);
		scoreColor = Color.red;
	}
	
	@Override
	public void update(float dt) {
			
		// if they press escape, go back
		if (Input.getKey(KeyEvent.VK_ESCAPE))
			gameLoop.setState(new Menu(gameLoop));
		
		// if the press r, restart
		if (Input.getKey(KeyEvent.VK_R))
			gameLoop.setState(new InGame(gameLoop));

		// only update entities when the player is alive
		if (player.getLives() > 0)
			super.update(dt);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		// set the font and color
		g.setColor(scoreColor);
		g.setFont(scoreFont);
		
		// display "You win" if the game has been won
		if (gameWon) {
			g.drawString(winString, (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth(winString)) / 2, 200);
			return;
		}
		
		// if the player is dead, display it and their score
		if (player.getLives() == 0) {
			g.drawString(loseString, (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth(loseString)) / 2, 200);
			
			String scoreString = "Score: " + player.getScore();
			g.drawString(scoreString, (Galaga.WINDOW_WIDTH - g.getFontMetrics().stringWidth(scoreString)) / 2, 300);
			return;
		}

		// draw the player's score and the current wave
		g.drawString("Score: " + player.getScore(), 10, 50);
		g.drawString("Wave: " + (wave + 1), 10, 75);

		// draw the player's lives as the player's image
		for (int i = 0; i < player.getLives(); i++) 
			g.drawImage(player.getCurrentImage(), 10 + 60 * i, Galaga.WINDOW_HEIGHT - 60,
					(int) player.getWidth(), (int) player.getHeight(), null);
		
		// get the player's powerups
		ArrayList<Powerup> powerups = player.getPowerups();
		
		// draw "Powerups"
		g.drawString("Powerups:", Galaga.WINDOW_WIDTH - 10 - 
				g.getFontMetrics().stringWidth("Powerups:"), 
				Galaga.WINDOW_HEIGHT - 20 - 30 * powerups.size());
		
		// draw the player's powerups as strings on the sides
		for (int i = 0; i < powerups.size(); i++) 
			g.drawString(powerups.get(i).toString(), Galaga.WINDOW_WIDTH - 10 - 
					g.getFontMetrics().stringWidth(powerups.get(i).toString()), 
					Galaga.WINDOW_HEIGHT - 20 - 30 * i);
	}
	
	/** checks if there are enemies alive. If not, the next wave is spawned */
	public void checkEnemyCount() {
		
		// if any of the entities are enemies, return
		for (Entity e : getEntities())
			if (e instanceof Enemy)
				return;
		
		// if no enemies exist, spawn the next wave in 1 second
		// using a new thread to not disrupt the game loop
		new Thread(() -> {

			// First, wait the specified amount of time
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// then, spawn the next wave
			spawnWave(getNextWave());
		}).start();
	}
	
	/** spawns the given wave of enemies */
	private void spawnWave(Enemy[] wave) {
		for (Enemy e : wave)
			spawn(e);
	}
	
	/** returns the wave of enemies at the current wave index */
	private Enemy[] getCurrentWave() {
		
		// create a wall variable as null to begin
		ArrayList<Enemy> wave = new ArrayList<>();
		
		// find the wave based on the current wave number
		switch(this.wave) {

		case 0:
			// wave 1 is a row of 6 regular enemies at difficulty 0
			wave.addAll(createEnemyRow(100, 0, 6));
			break;
			
		case 1:
			// wave 2 is two rows --- 5 in the back, 8 in the front

			// the row in the back will be enemies of difficulty 1
			wave.addAll(createEnemyRow(100, 1, 5));
			
			// the front row will be enemies of difficulty 0
			wave.addAll(createEnemyRow(150, 0, 8));
			break;
			
		case 2:
			// wave 3 is also two rows --- 10 in each

			// the row in the back will be enemies of difficulty 2
			wave.addAll(createEnemyRow(100, 2, 10));
			
			// the front row will be enemies of difficulty 1
			wave.addAll(createEnemyRow(150, 1, 10));
			break;
			
		case 3:
			// wave 4 is 3 rows of 10 --- 
			// 1st row -> difficulty 1, 2nd row -> difficulty 2, ...
			for (int row = 0; row < 3; row++)
				wave.addAll(createEnemyRow(200 - 50 * row, 1 + row, 10));
			break;
			
		case 4:
			// wave 5 is 5 rows of enemies with difficulty 4
			for (int row = 0; row < 5; row++)
				wave.addAll(createEnemyRow(300 - 50 * row, 4, 10));
			break;
			
		default:
			// meaning the game has been won
			gameWon = true;
		}
		
		return wave.toArray(new Enemy[0]);
	}

	/** creates a line of enemies */
	private ArrayList<Enemy> createEnemyRow(float y, int difficulty, int numEnemies) {
		// get the subline points for the enemies
		float[] positions = Functions.getSublinePoints(Galaga.WINDOW_WIDTH,
				numEnemies, 40, false, false);

		// create the enemy array and position them
		ArrayList<Enemy> enemies = new ArrayList<>();
		for (int i = 0; i < numEnemies; i++)
			enemies.add(new Enemy(this, difficulty, positions[i], y));

		// return the enemies
		return enemies;
	}

	/** returns the next wave of enemies */
	private Enemy[] getNextWave() {
		wave++;
		return getCurrentWave();
	}

}
