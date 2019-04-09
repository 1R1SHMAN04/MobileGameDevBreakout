package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {

    private static Screen scrCurrent;
    private SpriteBatch batch;

    static void setScreen(Screen screen) {
        scrCurrent.dispose();
        scrCurrent = screen;
    }

    @Override
    public void create() {
        resize(10, 5);
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
}

abstract class Element {
    Rectangle rectangle;

    Element(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    boolean contains(Rectangle rectangle) {
        return rectangle.contains(rectangle);
    }

    boolean contains(float x, float y) {
        return rectangle.contains(x, y);
    }

    boolean contains(Circle circle) {
        return rectangle.contains(circle);
    }

    boolean contains(Vector2 point) {
        return rectangle.contains(point);
    }

}
