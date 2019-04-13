package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    static boolean inputIsOnElement(Element element) {
        return (Gdx.input.isTouched() && element.contains(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()));
    }

    static int randomInt(int start, int end) {
        return (int) (Math.random() * end) + start;
    }

    static boolean rectanglesOverlap(Rectangle first, Rectangle second) {
        return oneOverlap(first, second) || oneOverlap(second, first);
    }

    static boolean clippingCorner(Rectangle first, Rectangle second) {
        return oneClip(first, second) || oneClip(second, first);
    }

    private static boolean oneClip(Rectangle first, Rectangle second) {
        return ((first.height + first.y  == second.y) && (first.x == second.x + second.width)) ||
                (first.x + first.width == second.x && first.y + first.height == second.y);
    }

    private static boolean oneOverlap(Rectangle first, Rectangle second) {
        return between(second.x, first.x, first.x + first.width) &&
                between(second.y, first.y, first.y + first.height);
    }

    private static boolean between(float number, float min, float max) {
        if (number >= min && number < max)
            return true;
        return false;
    }

    static boolean between(int number, int min, int max) {
        if (number >= min && number < max)
            return true;
        return false;
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

    boolean contains(Element element) {
        return contains(element.rectangle);
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

    float getX() {
        return rectangle.x;
    }

    float getY() {
        return rectangle.y;
    }

    float getWidth() {
        return rectangle.width;
    }

    float getHeight() {
        return rectangle.height;
    }

}

class TexturedElement extends Element {
    TextureRegion textureRegion;

    TexturedElement(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle);
        this.textureRegion = textureRegion;
    }

    TexturedElement(int x, int y, Texture texture) {
        super(new Rectangle(x, y, texture.getWidth(), texture.getHeight()));
        this.textureRegion = new TextureRegion(texture);
    }

}
