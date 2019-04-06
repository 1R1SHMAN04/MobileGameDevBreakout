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
     * Sprite for the Paddle
     */
    private TextureRegion paddleSprite;

    /**
     * Position, width and height of the paddle
     */
    private Rectangle paddle;

    /**
     * Sprite for the Ball
     */
    private TextureRegion ballSprite;

    /**
     * Position, width and height of the Ball
     */
    private Rectangle ball;

    /**
     * Array of possible Sprites for Bricks
     */
    private TextureRegion[] brickSprites;

    /**
     * List of Bricks, with their own randomly selected sprite
     */
    private List<Brick> bricks;

    ScreenGame() {
        // Set up on non-block sprites
        Texture img = new Texture("breakout_pieces.png");
        paddle = new Rectangle(32, 0, 64, 16);
        paddleSprite = new TextureRegion(img, 49, 80, 64, 16);
        ball = new Rectangle(100, 100, 8, 8);
        ballSprite = new TextureRegion(img, 48, 66, 8, 8);
        // Set up list of possible brick sprites
        brickSprites = new TextureRegion[4];
        brickSprites[0] = (new TextureRegion(img, 8, 8, 32, 16));
        brickSprites[1] = (new TextureRegion(img, 8, 28, 32, 16));
        brickSprites[2] = (new TextureRegion(img, 8, 48, 32, 16));
        brickSprites[3] = (new TextureRegion(img, 8, 68, 32, 16));
        bricks = new ArrayList<Brick>();
        List<Brick> line = new ArrayList<Brick>();
        // For each row
        for (int yOffset : new int[]{448, 432, 416, 400}) {
            int xOffset = 0;
            // Make 20 bricks with random colours
            for (int j = 0; j < 20; j++, xOffset += 32)
                line.add(new Brick(new Rectangle(xOffset, yOffset, 32, 16),
                        randomBrickSprite()));
        }
        bricks.addAll(line);
    }

    public void render(SpriteBatch batch) {
        drawRectangle(paddleSprite, paddle, batch);
        drawRectangle(ballSprite, ball, batch);
        for (Brick brick : bricks)
            drawRectangle(brickSprites[brick.textureRegion], brick.rectangle, batch);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            MyGdxGame.setScreen(new ScreenMenu());
    }

    public void dispose() { }

    /**
     * Get a number index of the brickSprites array, for use when generating random colours
     * @return int pertaining to position in brickSprites
     */
    private int randomBrickSprite() {
        return (int)(Math.random()*(brickSprites.length));
    }

}

/**
 * Brick class to store the rectangle and textureRegion of a breakable brick
 */
class Brick {
    Rectangle rectangle;
    int textureRegion;

    Brick(Rectangle rectangle, int textureRegion) {
        this.rectangle = rectangle;
        this.textureRegion = textureRegion;
    }
}
