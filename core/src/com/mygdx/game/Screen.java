package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Screen {
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
    void drawRectangle(TextureRegion sprite, Rectangle rectangle, SpriteBatch batch) {
        batch.draw(sprite, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }
}
