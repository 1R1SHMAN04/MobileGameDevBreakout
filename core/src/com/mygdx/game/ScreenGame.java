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
    private int framesSincePauseChanged;

    ScreenGame() {
        Texture img = new Texture("breakout_pieces.png");

        paddle = new Paddle(new Rectangle(Gdx.graphics.getWidth() / 2f - 32, 0, 64, 16),
                new TextureRegion(img, 48, 8, 64, 16));

        ball = new Ball(new Rectangle(Gdx.graphics.getWidth() / 2f - 4, 100, 8, 8),
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

        Texture returnIcon = new Texture("Return to Menu.png");
        Texture pauseIcon = new Texture("Pause.png");
        returnToMenu = new TexturedElement(Gdx.graphics.getWidth() - returnIcon.getWidth(),
                Gdx.graphics.getHeight() - returnIcon.getHeight(), returnIcon);
        pause = new TexturedElement(Gdx.graphics.getWidth() - pauseIcon.getWidth(),
                pauseIcon.getHeight(), pauseIcon);
    }

    public void render(SpriteBatch batch) {
        // Depending on the Game State, do some stuff
        switch (gameState) {
            case Paused:
                framesSincePauseChanged++;
                // Draw the pause button
                draw(returnToMenu, batch);
                // If you are touching the screen
                if (Gdx.input.isTouched()) {
                    // If the touch is on the returnToMenu button
                    if (MyGdxGame.inputIsOnElement(returnToMenu)) {
                        // Return to the menu
                        MyGdxGame.setScreen(new ScreenMenu());
                    }
                    // If you are touching somewhere other than the pause button
                    if (!MyGdxGame.inputIsOnElement(returnToMenu) && pausedNotRecently()) {
                        // Set state to playing
                        setGameState(GameState.Playing);
                    }
                }
                break;
            case Playing:
                framesSincePauseChanged++;
                draw(pause, batch);
                if (Gdx.input.isTouched()) {
                    if (MyGdxGame.inputIsOnElement(pause) && pausedNotRecently()) {
                        // If pressing pause, set gameState to pause
                        setGameState(GameState.Paused);
                        break;
                    } else {
                        if (pausedNotRecently(30)) {
                            float centerOfPaddle = paddle.getX() + (paddle.getWidth() / 2);
                            float x = Gdx.input.getX();
                            // If input is to the right
                            if (x > centerOfPaddle) {
                                if (x - centerOfPaddle > paddle.speed) {
                                    paddle.move(true);
                                }
                            }
                            // If input is the the left
                            else if (x < centerOfPaddle) {
                                if (centerOfPaddle - x > paddle.speed) {
                                    paddle.move(false);
                                }
                            }
                        }
                    }
                }
                ball.move();

                //TODO: Move the ball
                //  TODO: if(paddle.contains(ball) change the balls speed/direction
                //TODO: If ball is lower than screen you lose

                /* If the ball is intersecting with any brick, delete the brick and change balls
                direction */
                for (Brick brick : bricks) {
                    if (brick.contains(ball)) {
                        // TODO: Update direction of ball
                        bricks.remove(brick);
                    }
                }
                break;
            case PostGame:
                // yeah idk, display the score?
                break;
        }
        // Render all the bricks, the paddle and the ball
        for (Brick brick : bricks)
            draw(brickSprites[brick.textureRegion], brick, batch);
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
        return MyGdxGame.randomInt(0, brickSprites.length);
        //return (int) (Math.random() * (brickSprites.length));
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
        return pausedNotRecently(60);
    }

    private boolean pausedNotRecently(int frames) {
        return framesSincePauseChanged > frames;
    }

    public enum GameState {Paused, Playing, PostGame}

}

/**
 * A Brick Doesn't store it's own texture to save on RAM space, although the space saved is minimal
 */
class Brick extends Element {
    int textureRegion;

    Brick(Rectangle rectangle, int textureRegion) {
        super(rectangle);
        this.textureRegion = textureRegion;
    }
}

class Ball extends TexturedElement {

    private int speed;
    private Compass direction;
    private int angle;

    Ball(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
        this.speed = 1;
        //setAngle(MyGdxGame.randomInt(135, 225));
        setAngle(200);
    }

    private void setAngle(int angle) {
        while (angle > 360) {
            angle -= 360;
        }
        this.angle = angle;
        setDirection();
    }

    private void setDirection() {
        // IF angle is 90, 180, 270, or 0 add one to it
        if (between(angle, 0, 90))
            direction = Compass.NE;
        if (between(angle, 90, 180))
            direction = Compass.SE;
        if (between(angle, 180, 270))
            direction = Compass.SW;
        if (between(angle, 270, 360))
            direction = Compass.NW;
    }

    private boolean between(int number, int min, int max) {
        if (number >= min && number < max)
            return true;
        return false;
    }

    void move() {
        // TODO: Delta Time Fuckery (rectangle.x += speed * getDeltaTime();)
        double triAngle = angle;
        switch (direction) {
            case NE:
                break;
            case SE:
                triAngle -= 90;
                break;
            case SW:
                triAngle -= 180;
                break;
            case NW:
                triAngle -= 270;
                break;
        }
        triAngle = Math.abs(triAngle);
        double sinValue = Math.sin(Math.toRadians(triAngle));
        int hypotenuse = speed;
        float opposite = (float) (sinValue * hypotenuse);
        float adjacent = (float) Math.sqrt((hypotenuse * hypotenuse) - (opposite * opposite));
        switch (direction) {
            case NE:
                rectangle.x += adjacent;
                rectangle.y -= opposite;
                break;
            case SE:
                rectangle.x += adjacent;
                rectangle.y += opposite;
                break;
            case SW:
                rectangle.x -= adjacent;
                rectangle.y += opposite;
                break;
            case NW:
                rectangle.x -= adjacent;
                rectangle.y -= opposite;
                break;
        }
        if (rectangle.x <= 0) {
            rectangle.x = 0;
            if (direction.equals(Compass.NE) || direction.equals(Compass.NW)) setAngle(angle + 90);
            if (direction.equals(Compass.SE) || direction.equals(Compass.SW)) setAngle(angle - 90);
        }
        if(rectangle.x >= Gdx.graphics.getWidth() - rectangle.width) {
            if (direction.equals(Compass.NE) || direction.equals(Compass.NW)) setAngle(angle + 90);
            if (direction.equals(Compass.SE) || direction.equals(Compass.SW)) setAngle(angle - 90);
        }

        if (angle > 360 || angle < 0) System.out.println("This ain't it chief");
        System.out.println(angle);
    }

    enum Compass {NE, SE, SW, NW}
}

class Paddle extends TexturedElement {

    /**
     * Pixels moved per frame
     */
    float speed;

    Paddle(Rectangle rectangle, TextureRegion textureRegion) {
        super(rectangle, textureRegion);
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


