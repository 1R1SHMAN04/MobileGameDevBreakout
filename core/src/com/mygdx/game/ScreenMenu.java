package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

class ScreenMenu extends Screen {

    private Button quit;
    private Button game;

    ScreenMenu() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        TextureRegion quitIcon = new TextureRegion(new Texture("Quit to Desktop.png"));
        TextureRegion gameIcon = new TextureRegion(new Texture("Start Game.png"));
        quit = new Button(new Rectangle(height / 3, width / 3,
                quitIcon.getRegionWidth(), quitIcon.getRegionHeight()), quitIcon);
        game = new Button(new Rectangle(height / 3, width / 3 - 50,
                gameIcon.getRegionWidth(), gameIcon.getRegionHeight()), gameIcon);
    }

    public void render(SpriteBatch batch) {
        draw(game, batch);
        draw(quit, batch);
        if (Gdx.input.isTouched() && game.contains(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()))
            MyGdxGame.setScreen(new ScreenGame());
        else if (Gdx.input.isTouched() && quit.contains(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()))
            Gdx.app.exit();
    }

    public void dispose() {
    }

}

class Button extends TexturedElement {
    Button(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
    }
}