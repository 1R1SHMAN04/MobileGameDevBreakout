package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

class ScreenMenu extends Screen {

    private TexturedElement quitButton;
    private TexturedElement gameButton;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        TextureRegion quitIcon = new TextureRegion(new Texture("Quit to Desktop.png"));
        TextureRegion gameIcon = new TextureRegion(new Texture("Start Game.png"));
        quitButton = new TexturedElement(new Rectangle(height / 3, width / 3,
                quitIcon.getRegionWidth(), quitIcon.getRegionHeight()), quitIcon);
        gameButton = new TexturedElement(new Rectangle(height / 3, width / 3 - 50,
                gameIcon.getRegionWidth(), gameIcon.getRegionHeight()), gameIcon);
    }

    public void render(SpriteBatch batch) {
        draw(gameButton, batch);
        draw(quitButton, batch);
        if (MyGdxGame.isInputOnRectangle(gameButton.rectangle))
            MyGdxGame.setScreen(new ScreenGame());
        else if (MyGdxGame.isInputOnRectangle(quitButton.rectangle))
            Gdx.app.exit();
    }

    public void dispose() {
    }

}