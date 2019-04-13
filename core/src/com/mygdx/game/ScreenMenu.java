package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class ScreenMenu extends Screen {

    private TexturedElement quitButton;
    private TexturedElement gameButton;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        quitButton = new TexturedElement(height / 3, width / 3,
                new Texture("Quit to Desktop.png"));
        gameButton = new TexturedElement(height / 3, width / 3 - 50,
                new Texture("Start Game.png"));
    }

    public void render(SpriteBatch batch) {
        draw(gameButton, batch);
        draw(quitButton, batch);
        if (MyGdxGame.inputIsOnElement(gameButton))
            MyGdxGame.setScreen(new ScreenGame());
        else if (MyGdxGame.inputIsOnElement(quitButton))
            Gdx.app.exit();
    }

    public void dispose() {
    }

}