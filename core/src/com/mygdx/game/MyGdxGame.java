package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {

    private static Screen scrCurrent;
    private SpriteBatch batch;

    @Override
    public void create() {
        resize(10,5);
        batch = new SpriteBatch();
        scrCurrent = new ScreenMenu();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        scrCurrent.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        scrCurrent.dispose();
    }

    static void setScreen(Screen screen) {
        scrCurrent.dispose();
        scrCurrent = screen;
    }
}
