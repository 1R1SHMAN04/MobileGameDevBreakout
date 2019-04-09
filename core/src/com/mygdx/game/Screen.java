package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Screen {
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
    void draw(TexturedElement element, SpriteBatch batch) {
        batch.draw(element.textureRegion, element.rectangle.x, element.rectangle.y,
                element.rectangle.width, element.rectangle.height);
    }

    void draw(TextureRegion sprite, Element element, SpriteBatch batch) {
        batch.draw(sprite, element.rectangle.x, element.rectangle.y,
                element.rectangle.width, element.rectangle.height);
    }
}
