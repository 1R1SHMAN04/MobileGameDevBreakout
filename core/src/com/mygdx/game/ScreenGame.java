package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
    private GameState gameState;
    private TexturedElement returnToMenu;
    private TexturedElement pause;
    private int framesSincePaused;

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
        setGameState(GameState.Paused);

        TextureRegion returnIcon = new TextureRegion(new Texture("Return to Menu.png"));
        TextureRegion pauseIcon = new TextureRegion(new Texture("Pause.png"));

        returnToMenu = new TexturedElement(new Rectangle(
                Gdx.graphics.getWidth() - returnIcon.getRegionWidth(), Gdx.graphics.getHeight() - returnIcon.getRegionHeight(),
                returnIcon.getRegionWidth(), returnIcon.getRegionHeight()), returnIcon);
        pause = new TexturedElement(new Rectangle(
                Gdx.graphics.getWidth() - pauseIcon.getRegionWidth(), pauseIcon.getRegionHeight(),
                pauseIcon.getRegionWidth(), pauseIcon.getRegionHeight()), pauseIcon);
    }

    public void render(SpriteBatch batch) {
        framesSincePaused++;
        // Render all the bricks, the paddle and the ball
        for (Brick brick : bricks)
            if (brick.contains(ball.rectangle))
                bricks.remove(brick);
            else
                draw(brickSprites[brick.textureRegion], brick, batch);
        draw(paddle, batch);
        draw(ball, batch);
        // Depending on the Game State, do some stuff
        switch (gameState) {
            case Paused:
                // Draw the pause button
                draw(returnToMenu, batch);
                // If you are touching the screen
                if (Gdx.input.isTouched()) {
                    // If the touch is on the returnToMenu button
                    if (MyGdxGame.isInputOnRectangle(returnToMenu.rectangle)) {
                        // Return to the menu
                        MyGdxGame.setScreen(new ScreenMenu());
                    }
                    if (!MyGdxGame.isInputOnRectangle(pause.rectangle) && framesSincePaused > 60) {
                        setGameState(GameState.Playing);
                    }
                }
                break;
            case Playing:
                draw(pause, batch);
                if (MyGdxGame.isInputOnRectangle(pause.rectangle)) {
                    setGameState(GameState.Paused);
                }
                // If pressing return, set gameState to pause
                break;

            case PostGame:
                // yeah idk, display the score?
                break;
        }
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

    private void setGameState(GameState gameState) {
        if (gameState == GameState.Paused) {
            framesSincePaused = 0;
        }
        this.gameState = gameState;
    }

    public enum GameState {Paused, Playing, PostGame}

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

    void move(int amount) {
        //Maths
        //rectangle.x += speed;
    }
}

class Paddle extends TexturedElement {
    Paddle(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
    }
}

