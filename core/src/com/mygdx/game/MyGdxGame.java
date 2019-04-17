package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {

    /**
     * Button Click sound, for use in both Menu and Game Screens
     */
    static Sound buttonClick;

    /**
     * Current Screen
     */
    private static Screen CurrentScreen;

    /**
     * SpriteBatch to draw to
     */
    private SpriteBatch batch;

    /**
     * This Lovely world of mine, the piece of garbage I made in year 12 music in 2016 using
     * Garageband on a *shudders* "Mac"
     */
    private Sound thisLovelyWorldOfMine;

    /**
     * Sets the new Screen and disposes the old Screen
     *
     * @param screen Screen to set
     */
    static void setScreen(Screen screen) {
        CurrentScreen.dispose();
        CurrentScreen = screen;
    }

    /**
     * Function to tell if the user is the user is pressing on the given Element
     *
     * @param element Element to test
     * @return True if if both hovering over Element, and pressing on screen
     */
    static boolean pressingElement(Element element) {
        return (Gdx.input.isTouched() && element.contains(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()));
    }

    /**
     * Get's a random int between the begin and end
     *
     * @param begin Minimum number
     * @param end   Maximum number
     * @return Random int between being and end
     */
    static int randomInt(int begin, int end) {
        return (int) (Math.random() * end + begin);
    }

    @Override
    public void create() {
        buttonClick = Gdx.audio.newSound(Gdx.files.internal("Button Click.mp3"));
        thisLovelyWorldOfMine =
                Gdx.audio.newSound(Gdx.files.internal("This Lovely World Of Mine.mp3"));
        thisLovelyWorldOfMine.loop(20);
        batch = new SpriteBatch();
        CurrentScreen = new ScreenMenu();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        CurrentScreen.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        CurrentScreen.dispose();
    }

}

abstract class Element {

    Rectangle rectangle;

    Element(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    boolean overlaps(Element other) {
        return rectangle.overlaps(other.rectangle);
    }

    boolean contains(float x, float y) {
        return rectangle.contains(x, y);
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

    void centerToScreen() {
        rectangle.x = Gdx.graphics.getWidth() / 2f - rectangle.width / 2f;
        rectangle.y = Gdx.graphics.getHeight() / 2f - rectangle.height / 2f;
    }
}

class TexturedElement extends Element {

    TextureRegion textureRegion;

    TexturedElement(int x, int y, Texture texture) {
        super(new Rectangle(x, y, texture.getWidth(), texture.getHeight()));
        this.textureRegion = new TextureRegion(texture);
    }

    TexturedElement(int x, int y, TextureRegion textureRegion) {
        super(new Rectangle(x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight()));
        this.textureRegion = textureRegion;
    }

    private void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.rectangle.width = textureRegion.getRegionWidth() + 1;
        this.rectangle.height = textureRegion.getRegionHeight() + 1;
    }

    void setTextureRegion(Texture texture) {
        setTextureRegion(new TextureRegion(texture));
    }
}
