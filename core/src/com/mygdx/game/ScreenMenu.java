package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

class ScreenMenu extends Screen {

    private TexturedElement quitButton;
    private TexturedElement gameButton;
    private List<SpeedButton> speedButtons;
    private int speed;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        quitButton = new TexturedElement(height / 3, width / 3,
                new Texture("Quit to Desktop.png"));
        gameButton = new TexturedElement(height / 3, width / 3 - 50,
                new Texture("Start Game.png"));
        speed = 5;
        speedButtons = new ArrayList<SpeedButton>();
        int yOffset = 50;
        for (int speed : new int[]{20, 15, 10, 5, 2}) {
            speedButtons.add(new SpeedButton(50, (yOffset += 50),
                    new Texture(speed + ".png"), speed));
        }
    }

    private void update() {
        for (SpeedButton speedButton : speedButtons)
            if (speed != speedButton.speed && MyGdxGame.pressingElement(speedButton)) {
                MyGdxGame.buttonClick.play();
                speed = speedButton.speed;
            }
    }

    public void render(SpriteBatch batch) {
        update();
        draw(gameButton, batch);
        draw(quitButton, batch);
        for (SpeedButton speedButton : speedButtons)
            if (speedButton.speed != speed)
                draw(speedButton, batch);
        if (MyGdxGame.pressingElement(gameButton)) {
            MyGdxGame.buttonClick.play();
            MyGdxGame.setScreen(new ScreenGame(speed));
        } else if (MyGdxGame.pressingElement(quitButton)) {
            MyGdxGame.buttonClick.play();
            Gdx.app.exit();
        }
    }

    public void dispose() {
    }

}

class SpeedButton extends TexturedElement {
    int speed;

    SpeedButton(int x, int y, Texture texture, int speed) {
        super(x, y, texture);
        this.speed = speed;
    }
}