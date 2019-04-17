package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Text to show up when you win or lose
     */
    private TexturedElement youWinOrLose;

    /**
     * Text to display upon end of Game, telling player how to restart
     */
    private TexturedElement restart;

    /**
     * Text to display upon end of Game, telling player how to quit
     */
    private TexturedElement quit;

    /**
     * Sound to play when a Brick is killed
     */
    private Sound brickKill;

    /**
     * Sound to play when the Ball impacts the Paddle
     */
    private Sound paddleImpact;

    /**
     * Sound to play when the Ball impacts with the side of the screen
     */
    private Sound sideImpact;

    /**
     * Sound to play when the player wins the game
     */
    private Sound win;

    /**
     * Sound to play when the player loses the game
     */
    private Sound lose;

    /**
     * Frames since the game has been paused, game runs at 60fps
     */
    private int framesSinceStateChanged;

    /**
     * Constructor to set right the screen and start on GameState.Paused
     *
     * @param speed Speed of the ball, selected by the buttons on ScreenMenu
     */
    ScreenGame(int speed) {
        Initialize(speed);
    }

    /**
     * Initializes the screen so that Game can be restarted
     *
     * @param speed Speed that the Ball will move
     */
    private void Initialize(int speed) {
        Texture img = new Texture("breakout_pieces.png");
        paddle = new Paddle(Gdx.graphics.getWidth() / 2 - 32, 0,
                new TextureRegion(img, 48, 8, 64, 16));
        ball = new Ball(MyGdxGame.randomInt(0, Gdx.graphics.getWidth()), 350,
                new TextureRegion(img, 47, 30, 8, 8), speed);
        // Set right list of possible brick sprites
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
        Texture inTheEndGameNow = new Texture("You Win!.png");
        returnToMenu = new TexturedElement(Gdx.graphics.getWidth() - returnIcon.getWidth(),
                Gdx.graphics.getHeight() - returnIcon.getHeight(), returnIcon);
        pause = new TexturedElement(Gdx.graphics.getWidth() - pauseIcon.getWidth(),
                pauseIcon.getHeight(), pauseIcon);
        youWinOrLose = new TexturedElement(0, 0, inTheEndGameNow);
        restart = new TexturedElement(0, 0, new Texture("Restart.png"));
        restart.centerToScreen();
        restart.rectangle.y -= youWinOrLose.rectangle.height * 2;
        restart.rectangle.x -= youWinOrLose.rectangle.width;
        quit = new TexturedElement(0, 0, new Texture("Quit to Desktop.png"));
        quit.centerToScreen();
        quit.rectangle.y -= youWinOrLose.rectangle.height * 2;
        quit.rectangle.x += youWinOrLose.rectangle.width;
        win = Gdx.audio.newSound(Gdx.files.internal("Win.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.internal("Lose.wav"));
        brickKill = Gdx.audio.newSound(Gdx.files.internal("Brick Kill.mp3"));
        sideImpact = Gdx.audio.newSound(Gdx.files.internal("Side Impact.mp3"));
        paddleImpact = Gdx.audio.newSound(Gdx.files.internal("Paddle Impact.mp3"));

    }

    /**
     * Updated positions of Ball and Paddle, as well as the Orientation of the Ball, and the states
     * of all bricks
     *
     * @param batch SpriteBatch to draw to
     */
    private void update(SpriteBatch batch) {
        boolean isTouched = Gdx.input.isTouched();
        framesSinceStateChanged++;
        // Depending on the Game State, do some stuff
        switch (gameState) {
            case Paused:
                // Draw the return button
                draw(returnToMenu, batch);
                // If you are touching the screen
                if (isTouched) {
                    // If the touch is on the returnToMenu button
                    if (MyGdxGame.pressingElement(returnToMenu)) {
                        MyGdxGame.buttonClick.play();
                        // Return to the menu
                        MyGdxGame.setScreen(new ScreenMenu());
                    }
                    // If you are touching somewhere other than the pause button
                    if (!MyGdxGame.pressingElement(returnToMenu) && pausedNotRecently())
                        // Set state to playing
                        setGameState(GameState.Playing);
                }
                break;
            case Playing:
                // Draw the pause button
                draw(pause, batch);
                if (isTouched) {
                    /* If you are touching the pause button and the game hasn't been paused in the
                     last 60 frames */
                    if (MyGdxGame.pressingElement(pause) && pausedNotRecently()) {
                        MyGdxGame.buttonClick.play();
                        // If pressing pause, set gameState to pause
                        setGameState(GameState.Paused);
                        break;
                    } else { // If not touching pause, move the paddle
                        paddle.move();
                    }
                }
                // Move the ball
                ball.move();
                // Hit the left of the screen
                if (ball.getX() < 0) {
                    ball.rectangle.x = 0;
                    sideImpact.play();
                    ball.flipHorizontal();
                }
                // Hit the right of the screen
                else if (ball.getX() > Gdx.graphics.getWidth() - ball.getWidth()) {
                    ball.rectangle.x = Gdx.graphics.getWidth() - ball.getWidth();
                    sideImpact.play();
                    ball.flipHorizontal();
                }
                // Hit the bottom
                else if (ball.getY() < paddle.getHeight() / 4) {
                    lose.play();
                    setGameState(ScreenGame.GameState.Loss);
                }
                // Hit the top
                else if (ball.getY() > Gdx.graphics.getHeight() - ball.getHeight()) {
                    ball.rectangle.y = Gdx.graphics.getHeight() - ball.getHeight();
                    sideImpact.play();
                    ball.flipVertical();

                }
                // If the paddle and ball overlap, make the ball travel north
                if (paddle.overlaps(ball)) {
                    paddleImpact.play();
                    ball.flipVertical();
                }
                boolean flipVertical = false;
                boolean flipHorizontal = false;
                boolean allDead = true;
                for (Brick brick : bricks)
                    // For each alive brick
                    if (brick.isAlive())
                        /* If the ball is overlapping with it, kill the brick and figure out where
                        the collision happened, flipping the vertical axis if approached from north
                        or south, and flipping horizontal axis if approached from east or west */
                        if (brick.overlaps(ball)) {
                            List<Compass> ballRelativeToBrick =
                                    new ArrayList<Compass>(Arrays.asList(Compass.values()));
                            if (brick.getY() > ball.lastPosition.y) // If Ball is below the Brick
                                keepOnlySouths(ballRelativeToBrick);
                            else // If Ball is above Brick
                                if (brick.getY() + brick.getHeight() < ball.lastPosition.y)
                                    keepOnlyNorths(ballRelativeToBrick);
                                else // If Ball is within the bounds of Brick
                                    removeNorthAndSouths(ballRelativeToBrick);
                            if (brick.getX() > ball.lastPosition.x) // If Ball is west of Brick
                                keepOnlyWests(ballRelativeToBrick);
                            else // If ball is east of Brick
                                if (brick.getX() + brick.getWidth() < ball.lastPosition.x)
                                    keepOnlyEasts(ballRelativeToBrick);
                                else // If ball is withing bounds of Brick
                                    removeEastAndWests(ballRelativeToBrick);
                            /* If after all that garbage, the ball is north or south of the brick,
                             flip vertically and if it is east or west of the brick, flip
                             horizontally */
                            if (ballRelativeToBrick.get(0).name().contains("N") ||
                                    ballRelativeToBrick.get(0).name().contains("S"))
                                flipVertical = true;
                            if (ballRelativeToBrick.get(0).name().contains("E") ||
                                    ballRelativeToBrick.get(0).name().contains("W"))
                                flipHorizontal = true;
                            brick.kill();
                            brickKill.play();
                        } else // If not touching the ball there are still more bricks alive
                            allDead = false;
                /* Perform any needed flips, as decided above. Done this way in case two collisions
                 are ever detected on the same frame so that it doesn't flip twice, continuing in
                 the same direction */
                if (flipHorizontal)
                    ball.flipHorizontal();
                if (flipVertical)
                    ball.flipVertical();
                if (allDead) {
                    win.play();
                    setGameState(GameState.Victory);
                }
                break;
            case Victory:
                youWinOrLose.setTextureRegion(new Texture("You Win!.png"));
                youWinOrLose.centerToScreen();
                draw(youWinOrLose, batch);
                draw(quit, batch);
                draw(restart, batch);
                if (isTouched)
                    if (MyGdxGame.pressingElement(quit))
                        Gdx.app.exit();
                    else if (pausedNotRecently())
                        Initialize((int) paddle.speed);
                break;
            case Loss:
                youWinOrLose.setTextureRegion(new Texture("You Died.png"));
                youWinOrLose.centerToScreen();
                draw(youWinOrLose, batch);
                draw(quit, batch);
                draw(restart, batch);
                if (isTouched)
                    if (MyGdxGame.pressingElement(quit))
                        Gdx.app.exit();
                    else if (pausedNotRecently())
                        Initialize((int) paddle.speed);
                break;
        }
    }

    /**
     * Remove all the Non-North directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void keepOnlyNorths(List<Compass> locations) {
        locations.remove(Compass.Center);
        locations.remove(Compass.E);
        locations.remove(Compass.SE);
        locations.remove(Compass.S);
        locations.remove(Compass.SW);
        locations.remove(Compass.W);
    }

    /**
     * Remove all the Non-East directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void keepOnlyEasts(List<Compass> locations) {
        locations.remove(Compass.Center);
        locations.remove(Compass.N);
        locations.remove(Compass.S);
        locations.remove(Compass.SW);
        locations.remove(Compass.W);
        locations.remove(Compass.NW);
    }

    /**
     * Remove all the Non-South directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void keepOnlySouths(List<Compass> locations) {
        locations.remove(Compass.Center);
        locations.remove(Compass.N);
        locations.remove(Compass.NE);
        locations.remove(Compass.E);
        locations.remove(Compass.W);
        locations.remove(Compass.NW);
    }

    /**
     * Remove all the Non-West directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void keepOnlyWests(List<Compass> locations) {
        locations.remove(Compass.Center);
        locations.remove(Compass.N);
        locations.remove(Compass.NE);
        locations.remove(Compass.E);
        locations.remove(Compass.SE);
        locations.remove(Compass.S);
    }

    /**
     * Remove all the North and South directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void removeNorthAndSouths(List<Compass> locations) {
        locations.remove(Compass.N);
        locations.remove(Compass.NE);
        locations.remove(Compass.SE);
        locations.remove(Compass.S);
        locations.remove(Compass.SW);
        locations.remove(Compass.NW);
    }

    /**
     * Remove all the East and West directions from the list
     *
     * @param locations Potential locations a ball could be in, relative to the brick it is
     *                  overlapping with
     */
    private void removeEastAndWests(List<Compass> locations) {
        locations.remove(Compass.NE);
        locations.remove(Compass.E);
        locations.remove(Compass.SE);
        locations.remove(Compass.SW);
        locations.remove(Compass.W);
        locations.remove(Compass.NW);
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
     * Updates the GameState and sets framesSinceStateChanged to 0 if it is being paused/un-paused
     *
     * @param gameState The GameState to set
     */
    private void setGameState(GameState gameState) {
        framesSinceStateChanged = 0;
        this.gameState = gameState;
    }

    /**
     * Checks to see if the game was paused/un-paused in the 60 frames. Change the 60 value to
     * change the time that it checks
     *
     * @return True if it has been more than allotted time, false otherwise
     */
    private boolean pausedNotRecently() {
        return framesSinceStateChanged > 60;
    }

    /**
     * States for the game to be in, duh
     */
    public enum GameState {
        Paused, Playing, Victory, Loss
    }

    /**
     * Enum of Directions, for used when testing Collision
     */
    private enum Compass {
        N, NE, E, SE, S, SW, W, NW, Center
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
     * Position that the Ball was last frame, to help with collision detection
     */
    Vector2 lastPosition;

    /**
     * Pixels to move per frame
     */
    private int speed;

    /**
     * Travelling right if true, down if false
     */
    private boolean right;

    /**
     * Travelling up if true, right if false
     */
    private boolean up;

    /**
     * Basic Constructor for the class
     *
     * @param x             X Position of the Ball
     * @param y             Y Position of the Ball
     * @param textureRegion Sprite of the ball
     * @param speed         Speed for the Ball, chosen by the ScreenMenu buttons
     */
    Ball(int x, int y, TextureRegion textureRegion, int speed) {
        super(x, y, textureRegion);
        this.speed = speed;
        this.up = false;
        this.right = false;
        lastPosition = new Vector2();
    }

    /**
     * Flips the Horizontal axis, what more can I say?
     */
    void flipHorizontal() {
        right = !right;
    }

    /**
     * Flips the vertical axis, what more can I say?
     */
    void flipVertical() {
        up = !up;
    }

    /**
     * Uses Trigonometry to move the ball
     */
    void move() {
        lastPosition = rectangle.getCenter(lastPosition);
        double triAngle;
        if (up)
            if (right)
                triAngle = 315;
            else
                triAngle = 45;
        else if (right)
            triAngle = 225;
        else
            triAngle = 135;

        float width = Math.abs((float) (Math.sin(Math.toRadians(triAngle)) * speed));
        float height = Math.abs((float) Math.sqrt((speed * speed) - (width * width)));
        /* Multiplying deltaTime by 60 makes it approximately equal to 1, so my speed calculations
         are more or less the same. Also fun fact, for some reason if I add "* dt" onto the previous
         line, it causes height to calculate as a NaN for some reason, but doing the exact same
         two lines later is perfectly alright. */
        float dt = Gdx.graphics.getDeltaTime() * 60;
        width *= dt;
        height *= dt;

        if (up) rectangle.y += height;
        else rectangle.y -= height;
        if (right) rectangle.x -= width;
        else rectangle.x += width;
    }

}

/**
 * Class to represent the paddle
 */
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
     * Move the paddle, at the paddles speed, but don't navigate off the side of the
     * screen
     */
    void move() {
        boolean direction;
        float centerOfPaddle = rectangle.x + (rectangle.width / 2);
        float x = Gdx.input.getX();
        /* If input is to the east, and the input is further away than the paddles speed,
         move to the east */
        if (x > centerOfPaddle && x - centerOfPaddle > speed)
            direction = true;
        /* If input is the the west, and the input is further away than the paddles speed,
         move to the west */
        else if (x < centerOfPaddle && centerOfPaddle - x > speed)
            direction = false;
        else
            return;
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
