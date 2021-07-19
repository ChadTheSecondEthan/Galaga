package GameState;

import Entity.*;
import Main.Galaga;
import UI.Button;
import UI.Image;
import UI.ScrollView;
import Utils.GameFile;
import Utils.Input;
import Shop.ShopItem;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Shop extends GameState {

    // ui
    private ScrollView scrollView;
    private Button[] shopItems;
    private Button buyButton;
    private Image buyImage;

    public Shop() {
        super("gameStates/shop");

        // create the scroll view
        scrollView = findEntityByName("scrollView");
        scrollView.setDrawable(new ColorRenderer(Color.gray));

        // find the shop items from the file
        ArrayList<ShopItem> items = new ArrayList<>();
        new GameFile("shopItems").forEachLine(line -> {
            ShopItem item = new ShopItem();
            String[] attrs = line.split(",");

            item.name = attrs[0].trim();
            item.cost = Integer.parseInt(attrs[1].trim());
            item.imgPath = attrs[2].trim();
            items.add(item);
        });

        // create buttons from the shop items
        shopItems = new Button[items.size()];
        for (int i = 0; i < shopItems.length; i++) {
            Button button = Button.fromFile("elements/shopEntry");
            button.setParent(scrollView);
            button.setBounds(25, 25 + 75 * i, 50, 50);
            button.setDrawable(new SpriteRenderer(items.get(i).imgPath));
            button.spawn();

            final int num = i;
            button.addOnClickListener(() -> inspect(items.get(num)));

            shopItems[i] = button;
        }

        // set progress for the scroll view
        scrollView.setMinProgress(0);
        scrollView.setMaxProgress(75f * shopItems.length - scrollView.getHeight() + 25f);

        // create the buy area ui
        buyImage = new Image(null);
        buyImage.setAbsoluteBounds(500, 100, 200, 200);
        buyImage.setVisible(false);
        buyImage.spawn();

        // buy button
        buyButton = new Button();
        buyButton.setAbsoluteBounds(550, 400, 100, 40);
        buyButton.setVisible(false);
        buyButton.spawn();
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // if the user pressed escape, take them to the home page
        if (Input.getKeyDown(KeyEvent.VK_ESCAPE))
            setState(Galaga.MENU);
    }

    private void inspect(ShopItem shopItem) {

        if (buyImage.isVisible()) {
            buyImage.setVisible(false);
            buyButton.setVisible(false);
        }

        else {
            buyImage.setImage(shopItem.imgPath);
            buyImage.setVisible(true);

            buyButton.setVisible(true);
        }
    }
}
