package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Screen {
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
    void draw(TexturedElement element, SpriteBatch batch) {
        draw(element.textureRegion, element.rectangle.x,element.rectangle.y, element.rectangle.width, element.rectangle.height, batch);
    }

    void draw(TextureRegion textureRegion, Element element, SpriteBatch batch) {
        draw(textureRegion, element.rectangle.x,element.rectangle.y, element.rectangle.width, element.rectangle.height, batch);
    }

    void draw(TextureRegion textureRegion, float x, float y, float width, float height, SpriteBatch batch) {
        batch.draw(textureRegion, x, y, width, height);
    }
}
