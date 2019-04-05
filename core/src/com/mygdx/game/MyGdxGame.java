package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
    static Screen scrCurrent;
    SpriteBatch batch;
    Texture img;

    static void setScreen(Screen screen) {
        scrCurrent = screen;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("breakout_pieces.png");
        scrCurrent = new ScreenMenu();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        scrCurrent.render(batch);
        /*
        if (scrCurrent.getClass() == ScreenMenu.class) {
            scrCurrent.render(batch);
        } else if (scrCurrent.getClass() == ScreenGame.class) {

        } else {
            System.exit(1);
        }
        */
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
