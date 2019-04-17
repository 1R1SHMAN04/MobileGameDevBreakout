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

    /**
     * Current state of the game, Playing, Paused, Victory or Loss
     */
    private GameState gameState;

    /**
     * Return to Menu Button, rendered while Paused
     */
    private TexturedElement returnToMenu;

    /**
     * Pause Button, rendered while Playing
     */
    private TexturedElement pause;

    /**
     * Frames since the game has been paused, game runs at 60fps
     */
    private int framesSincePauseChanged;

    /**
     * Constructor to set up the screen and start on GameState.Paused
     *
     * @param speed Speed of the ball, selected by the buttons on ScreenMenu
     */
    ScreenGame(int speed) {
        Texture img = new Texture("breakout_pieces.png");

        paddle = new Paddle(Gdx.graphics.getWidth() / 2 - 32, 0,
                new TextureRegion(img, 48, 8, 64, 16));

        ball = new Ball(Gdx.graphics.getWidth() / 2 - 4 /*200*/, 100,
                new TextureRegion(img, 47, 30, 8, 8), speed);
        // Set up list of possible brick sprites
        brickSprites = new TextureRegion[4];
        brickSprites[0] = new TextureRegion(img, 8, 8, 32, 16);
        brickSprites[1] = new TextureRegion(img, 8, 28, 32, 16);
        brickSprites[2] = new TextureRegion(img, 8, 48, 32, 16);
        brickSprites[3] = new TextureRegion(img, 8, 68, 32, 16);
        bricks = new ArrayList<Brick>();
        // For each row
        for (int yOffset : new int[]{448, 432, 416, 400}) {
            int xOffset = 0;
            // Make 20 bricks with random colours
            for (int j = 0; j < 20; j++, xOffset += 32)
                bricks.add(new Brick(xOffset, yOffset, randomBrickSprite()));
        }
        setGameState(GameState.Paused);

        Texture returnIcon = new Texture("Return to Menu.png");
        Texture pauseIcon = new Texture("Pause.png");
        returnToMenu = new TexturedElement(Gdx.graphics.getWidth() - returnIcon.getWidth(),
                Gdx.graphics.getHeight() - returnIcon.getHeight(), returnIcon);
        pause = new TexturedElement(Gdx.graphics.getWidth() - pauseIcon.getWidth(),
                pauseIcon.getHeight(), pauseIcon);
    }

    /**
     * Updated positions of Ball and Paddle, as well as the Orientation of the Ball, and the states
     * of all bricks
     *
     * @param batch SpriteBatch to draw to
     */
    private void update(SpriteBatch batch) {
        boolean isTouched = Gdx.input.isTouched();
        // Depending on the Game State, do some stuff
        switch (gameState) {
            case Paused:
                framesSincePauseChanged++;
                // Draw the return button
                draw(returnToMenu, batch);
                // If you are touching the screen
                if (isTouched) {
                    // If the touch is on the returnToMenu button
                    if (MyGdxGame.clickingElement(returnToMenu))
                        // Return to the menu
                        MyGdxGame.setScreen(new ScreenMenu());
                    // If you are touching somewhere other than the pause button
                    if (!MyGdxGame.clickingElement(returnToMenu) && pausedNotRecently())
                        // Set state to playing
                        setGameState(GameState.Playing);
                }
                break;
            case Playing:
                framesSincePauseChanged++;
                // Draw the pause button
                draw(pause, batch);
                if (isTouched) {
                    // If you are touching the pause button and the game hasn't been paused in the
                    // 60 frames
                    if (MyGdxGame.clickingElement(pause) && pausedNotRecently()) {
                        // If pressing pause, set gameState to pause
                        setGameState(GameState.Paused);
                        break;
                    } else { // If not touching pause, move the paddle
                        float centerOfPaddle = paddle.getX() + (paddle.getWidth() / 2);
                        float x = Gdx.input.getX();
                        // If input is to the right
                        if (x > centerOfPaddle) {
                            // And the input is further away than the paddles speed
                            // Move to the right
                            if (x - centerOfPaddle > paddle.speed) paddle.move(true);
                        }
                        // If input is the the left
                        else // And the input is further away than the paddles speed
                            if (x < centerOfPaddle) // Move to the left
                                if (centerOfPaddle - x > paddle.speed)
                                    paddle.move(false);
                    }
                }
                // Move the ball
                ball.move();
                // Hit the left of the screen
                if (ball.getX() < 0) {
                    ball.rectangle.x = 0;
                    ball.flipHorizontal();
                }
                // Hit the right of the screen
                else if (ball.getX() > Gdx.graphics.getWidth() - ball.getWidth()) {
                    ball.flipHorizontal();
                }
                // Hit the bottom
                else if (ball.getY() < paddle.getY() + paddle.getHeight() - 4) {
                    //TODO: Make sure this is the right way around before submitting
                    //rectangle.y = screenGame.paddle().getY() + screenGame.paddle().getHeight();
                    //flipVertical();
                    setGameState(ScreenGame.GameState.Loss);
                }
                // Hit the top
                else if (ball.getY() > Gdx.graphics.getHeight()) {
                    ball.flipVertical();
                }
                // If the paddle and ball overlap, make the ball travel up
                if (paddle.overlaps(ball))
                    ball.flipVertical();
                boolean doINeedToFlip = false;
                boolean allDead = true;
                for (Brick brick : bricks)
                    //TODO: Find out where the overlap happened, top, bottom, right, or left
                    // For each alive brick
                    if (brick.isAlive())
                        /* If the ball is touching overlapping with it, kill the brick and flip the
                        Vertical axis of the ball */
                        if (brick.overlaps(ball)) {
                            doINeedToFlip = true;
                            brick.kill();
                        } else // If not touching the ball there are still more bricks alive
                            allDead = false;
                if (doINeedToFlip)
                    ball.flipVertical();
                if (allDead)
                    setGameState(GameState.Victory);
                break;
            case Victory:
                // yeah idk, display the score?
                break;
            case Loss:
                // Display you are loser
        }
    }

    /**
     * Wipes screen clean and renders all elements except the dead bricked
     *
     * @param batch SpriteBatch to draw to
     */
    public void render(SpriteBatch batch) {
        update(batch);
        // Render all the bricks, the paddle and the ball
        for (Brick brick : bricks)
            if (brick.isAlive())
                draw(brickSprites[brick.textureRegion], brick, batch);
        draw(paddle, batch);
        draw(ball, batch);
    }

    /**
     * Dispose method, called when the object is about to be eaten by Java's Garbage Collector
     */
    public void dispose() {
    }

    /**
     * Get a number index of the brickSprites array, for use when generating random colours
     *
     * @return int pertaining to position in brickSprites
     */
    private int randomBrickSprite() {
        return MyGdxGame.randomInt(0, brickSprites.length);
    }

    /**
     * Updates the GameState and sets framesSincePauseChanged to 0 if it is being paused/un-paused
     *
     * @param gameState The GameState to set
     */
    private void setGameState(GameState gameState) {
        if (gameState == GameState.Paused || gameState == GameState.Playing) {
            framesSincePauseChanged = 0;
        }
        this.gameState = gameState;
    }

    /**
     * Checks to see if the game was paused/un-paused in the 60 frames. Change the 60 value to
     * change the time that it checks
     *
     * @return True if it has been more than allotted time, false otherwise
     */
    private boolean pausedNotRecently() {
        return framesSincePauseChanged > 60;
    }

    /**
     * States for the game to be in, duh
     */
    public enum GameState {
        Paused, Playing, Victory, Loss
    }

}

/**
 * A Brick Doesn't store it's own texture to save on RAM space, although the space saved is minimal
 */
class Brick extends Element {
    /**
     * Index of the brickSprites array to be used when drawing
     */
    int textureRegion;

    /**
     * Is the brick alive or ded?
     */
    private boolean ded;

    /**
     * Basic Constructor for the class
     *
     * @param x             X position of the Brick
     * @param y             Y position of the Brick
     * @param textureRegion Index for sprite
     */
    Brick(int x, int y, int textureRegion) {
        super(new Rectangle(x, y, 32, 16));
        this.textureRegion = textureRegion;
        ded = false;
    }

    /**
     * Tells if the Brick has been hit yet
     *
     * @return True if the brick hasn't been hit, false otherwise
     */
    boolean isAlive() {
        return !ded;
    }

    /**
     * Sets ded to true
     */
    void kill() {
        ded = true;
    }

}

/**
 * Class to represent the ball, and take care of how to move it, and what direction to move in
 */
class Ball extends TexturedElement {

    /**
     * Pixels to move per frame
     */
    private int speed;

    /**
     * Travelling up if true, down if false
     */
    private CompassHorizontal up;

    /**
     * Travelling right if true, left if false
     */
    private CompassVertical right;

    /**
     * Basic Constructor for the class
     * @param x X Position of the Ball
     * @param y Y Position of the Ball
     * @param textureRegion Sprite of the ball
     * @param speed Speed for the Ball, chosen by the ScreenMenu buttons
     */
    Ball(int x, int y, TextureRegion textureRegion, int speed) {
        super(x, y, textureRegion);
        this.speed = speed;
        this.right = CompassVertical.S;
        this.up = CompassHorizontal.W;
    }

    /**
     * Flips the Horizontal axis, what more can I say?
     */
    void flipHorizontal() {
        if (up.equals(CompassHorizontal.E)) {
            up = CompassHorizontal.W;
            return;
        }
        if (up.equals(CompassHorizontal.W)) {
            up = CompassHorizontal.E;
        }
    }

    /**
     * Flips the vertical axis, what more can I say?
     */
    void flipVertical() {
        if (right.equals(CompassVertical.S)) {
            right = CompassVertical.N;
            return;
        }
        if (right.equals(CompassVertical.N)) {
            right = CompassVertical.S;
        }
    }

    /**
     * Uses Trigonometry to move the ball
     */
    void move() {
        double triAngle;
        if (right.equals(CompassVertical.N))
            if (up.equals(CompassHorizontal.E))
                triAngle = 315;
            else
                triAngle = 45;
        else if (up.equals(CompassHorizontal.E))
            triAngle = 225;
        else
            triAngle = 135;

        float dt = Gdx.graphics.getDeltaTime() * 60;
        int hypotenuse = speed;
        float width = Math.abs((float) (Math.sin(Math.toRadians(triAngle)) * hypotenuse)) * dt;
        float height = Math.abs((float) Math.sqrt((hypotenuse * hypotenuse) - (width * width))) * dt;

        if (right.equals(CompassVertical.N)) rectangle.y += height;
        else rectangle.y -= height;
        if (up.equals(CompassHorizontal.E)) rectangle.x -= width;
        else rectangle.x += width;
    }

    enum CompassVertical {N, S}

    enum CompassHorizontal {E, W}
}

class Paddle extends TexturedElement {
    /**
     * Pixels moved per frame
     */
    float speed;

    /**
     * Basic Constructor for the class
     *
     * @param x             X position of the Brick
     * @param y             Y position of the Brick
     * @param textureRegion Sprite
     */
    Paddle(int x, int y, TextureRegion textureRegion) {
        super(x, y, textureRegion);
        speed = 5;
    }

    /**
     * Move in a given direction, at the paddles speed, but don't navigate off the side of the
     * screen
     *
     * @param direction Direction to move, false for left, true for right
     */
    void move(boolean direction) {
        if (direction)
            // If moving right would put you off the screen
            if (rectangle.x + speed > Gdx.graphics.getWidth() - rectangle.width)
                // Then go to the side
                rectangle.x = Gdx.graphics.getWidth() - rectangle.width;
                // Otherwise just move
            else rectangle.x += speed;
        else
            // If moving left would put you off the screen
            if (rectangle.x - speed < 0)
                // Then go to the side
                rectangle.x = 0;
                // Otherwise just move
            else rectangle.x -= speed;
    }
}


