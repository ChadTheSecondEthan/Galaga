package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Entity.Background;
import Main.Galaga;
import UI.Button;
import UI.Text;
import UI.UIElement;
import Utils.Input;

import javax.swing.text.TabExpander;

public class Menu extends GameState {

	public Menu() {
		super("gameStates/menu");

		((Button) findEntityByName("startButton")).addOnClickListener(() -> setState(Galaga.IN_GAME));
		((Button) findEntityByName("shopButton")).addOnClickListener(() -> setState(Galaga.SHOP));
		((Button) findEntityByName("helpButton")).addOnClickListener(() -> setState(Galaga.INSTRUCTIONS));
		((Button) findEntityByName("quitButton")).addOnClickListener(() -> System.exit(0));

		Text title = findEntityByName("title");
		title.setFont(new Font("Arial", Font.BOLD, 48));
		title.setColor(Color.red);
	}

	@Override
	public void init() {

	}

	@Override
	public void update(float dt) {
		super.update(dt);

		// close the window if they pressed escape
		if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
	}

}
