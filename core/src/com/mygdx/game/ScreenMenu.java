package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class ScreenMenu extends Screen {

    private Rectangle quit;
    private Rectangle game;
    private Texture quitIcon;
    private Texture gameIcon;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        quitIcon = new Texture("Quit to Desktop.png");
        gameIcon = new Texture("Start Game.png");
        quit = new Rectangle(height / 3, width / 3, quitIcon.getWidth(), quitIcon.getHeight());
        game = new Rectangle(height / 3, width / 3 - 50, gameIcon.getWidth(), gameIcon.getHeight());
    }

    public void render(SpriteBatch batch) {
        batch.draw(gameIcon, game.getX(), game.getY(), game.width, game.height);
        batch.draw(quitIcon, quit.getX(), quit.getY(), quit.width, quit.height);

        if (Gdx.input.isTouched() && game.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
            MyGdxGame.setScreen(new ScreenGame());
        else if (Gdx.input.isTouched() && quit.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
            System.exit(0);

        }
    }

    public void dispose() {
        quitIcon.dispose();
        gameIcon.dispose();
    }

}
