package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

class ScreenGame extends Screen {

    /**
     * Paddle element
     */
    private Paddle paddle;

    /**
     * Ball element, with sprite and rectangle
     */
    private Ball ball;

    /**
     * Array of possible Sprites for Bricks
     */
    private TextureRegion[] brickSprites;

    /**
     * List of Bricks, with their own randomly selected sprite
     */
    private List<Brick> bricks;

    ScreenGame() {
        Texture img = new Texture("breakout_pieces.png");

        paddle = new Paddle(new Rectangle(32, 0, 64, 16),
                new TextureRegion(img, 48, 8, 64, 16));

        ball = new Ball(new Rectangle(100, 100, 8, 8),
                new TextureRegion(img, 47, 30, 8, 8));
        // Set up list of possible brick sprites
        brickSprites = new TextureRegion[4];
        brickSprites[0] = (new TextureRegion(img, 8, 8, 32, 16));
        brickSprites[1] = (new TextureRegion(img, 8, 28, 32, 16));
        brickSprites[2] = (new TextureRegion(img, 8, 48, 32, 16));
        brickSprites[3] = (new TextureRegion(img, 8, 68, 32, 16));
        bricks = new ArrayList<Brick>();
        // For each row
        for (int yOffset : new int[]{448, 432, 416, 400}) {
            int xOffset = 0;
            // Make 20 bricks with random colours
            for (int j = 0; j < 20; j++, xOffset += 32)
                bricks.add(new Brick(new Rectangle(xOffset, yOffset, 32, 16),
                        randomBrickSprite()));
        }
    }

    public void render(SpriteBatch batch) {
        for (Brick brick : bricks)
            if (brick.contains(ball.rectangle))
                bricks.remove(brick);
            else
                draw(brickSprites[brick.textureRegion], brick, batch);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            MyGdxGame.setScreen(new ScreenMenu());
        draw(paddle, batch);
        draw(ball, batch);
    }

    public void dispose() {
    }

    /**
     * Get a number index of the brickSprites array, for use when generating random colours
     *
     * @return int pertaining to position in brickSprites
     */
    private int randomBrickSprite() {
        return (int) (Math.random() * (brickSprites.length));
    }

}

abstract class TexturedElement extends Element {
    TextureRegion textureRegion;

    TexturedElement(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle);
        this.textureRegion = textureRegion;
    }
}

class Brick extends Element {
    int textureRegion;

    Brick(Rectangle rectangle, int textureRegion) {
        super(rectangle);
        this.textureRegion = textureRegion;
    }
}

class Ball extends TexturedElement {
    int angle;
    int speed;

    Ball(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
        this.speed = 0;
        this.angle = 180;
    }

    void move() {
    }
}

class Paddle extends TexturedElement {
    Paddle(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
    }
}

