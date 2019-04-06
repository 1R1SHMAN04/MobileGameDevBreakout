package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

class ScreenMenu extends Screen {

    private Rectangle quit;
    private Rectangle game;
    private TextureRegion quitIcon;
    private TextureRegion gameIcon;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        quitIcon = new TextureRegion(new Texture("Quit to Desktop.png"));
        gameIcon = new TextureRegion(new Texture("Start Game.png"));
        quit = new Rectangle(height / 3, width / 3, quitIcon.getRegionWidth(), quitIcon.getRegionHeight());
        game = new Rectangle(height / 3, width / 3 - 50, gameIcon.getRegionWidth(), gameIcon.getRegionHeight());
    }

    public void render(SpriteBatch batch) {
        drawRectangle(gameIcon, game, batch);
        drawRectangle(quitIcon, quit, batch);
        if (Gdx.input.isTouched() && game.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
            MyGdxGame.setScreen(new ScreenGame());
        else if (Gdx.input.isTouched() && quit.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
            Gdx.app.exit();
    }

    public void dispose() { }

}
