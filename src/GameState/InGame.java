package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.*;
import Main.Galaga;
import Main.GameLoop;
import Main.GameStats;
import UI.Text;
import Utils.Functions;
import Utils.Input;

public class InGame extends GameState {
	
	// strings for winning and losing
	private static final String winString = "You Win! Press ESC to go back or R to restart";
	private static final String loseString = "You Lose. Press ESC to go back or R to restart";
	
	// has the game been won or lost?
	private boolean gameWon;
	private boolean gameLost;

	// current wave
	private int wave;

	// the player
	private Player player;

	// texts
	private Text scoreText;
	private Text statusText;
	private Text waveText;
	private Text coinsText;

	public InGame() {
		super();
		
		// the game is not already won or lost
		gameWon = false;
		gameLost = false;

		// set current wave
		wave = 0;

		// spawn the background, player, and first wave
		new Background("sprites/Background.png").spawn();
		(player = new Player()).spawn();
		player.addDestroyListener(this::onGameLose);
		spawn(getCurrentWave());

		// initialize font and color
		Text.defaultFont = new Font("Arial", Font.BOLD, 24);
		Text.defaultColor = Color.red;

		// text for when the player wins or loses
		statusText = new Text("");
		statusText.centerX();
		statusText.setY(200);
		statusText.spawn();
		statusText.setVisible(false);

		// text for the player's score
		scoreText = new Text("");
		scoreText.setAlignment(Text.TextAlign.LEFT);
		scoreText.setPosition(10, 50);
		scoreText.spawn();

		// text for the current wave
		waveText = new Text("");
		waveText.setAlignment(Text.TextAlign.LEFT);
		waveText.setPosition(10, 75);
		waveText.spawn();

		coinsText = new Text("");
		coinsText.setAlignment(Text.TextAlign.LEFT);
		coinsText.setPosition(10, 100);
		coinsText.spawn();
	}

	@Override
	public void init() {}
	
	@Override
	public void update(float dt) {
			
		// if they press escape, go back
		if (Input.getKey(KeyEvent.VK_ESCAPE))
			setState(Galaga.MENU);
		
		// if the press r, restart
		if (Input.getKey(KeyEvent.VK_R))
			setState(Galaga.IN_GAME);

		// only update entities when the player is alive
		if (player.getLives() > 0)
			super.update(dt);

		// update texts
		scoreText.setText("Score: " + player.getScore());
		waveText.setText("Wave: " + (wave + 1));
		coinsText.setText("Coins: " + GameStats.instance().getCoins());
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);

		// display "You win" if the game has been won
		if (gameWon || gameLost) return;

		// draw the player's lives as the player's image
		for (int i = 0; i < player.getLives(); i++)
			g.drawImage(((SpriteRenderer) player.getDrawable()).getImage(), 10 + 60 * i, Galaga.WINDOW_HEIGHT - 60,
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

	/** called when the game is won */
	private void onGameWin() {
		gameWon = true;
		statusText.setVisible(true);
		statusText.setText(winString);

		scoreText.setVisible(false);
		waveText.setVisible(false);
		coinsText.setVisible(false);
	}

	/** called when the player dies */
	private void onGameLose() {
		gameLost = true;
		scoreText.setAlignment(Text.TextAlign.CENTER);

		scoreText.centerX();
		scoreText.setY(300);
		waveText.setVisible(false);
		coinsText.setVisible(false);

		statusText.setVisible(true);
		statusText.setText(loseString);
	}
	
	/** checks if there are enemies alive. If not, the next wave is spawned */
	public void checkEnemyCount() {
		
		// if any of the entities are enemies, return
		for (Entity e : getEntities())
			if (e instanceof Enemy)
				return;

		// save game stats
		GameStats.instance().save();
		
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
			spawn(getNextWave());
		}).start();
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
			onGameWin();
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
		for (int i = 0; i < numEnemies; i++) {
			Enemy enemy = new Enemy(difficulty);
			enemy.setPosition(positions[i], y);
			enemies.add(enemy);
		}

		// return the enemies
		return enemies;
	}

	/** returns the next wave of enemies */
	private Enemy[] getNextWave() {
		wave++;
		return getCurrentWave();
	}

}
