package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class ScreenGame extends Screen {

    Rectangle paddle;
    Texture paddleSprite;

    ScreenGame() {
        paddle = new Rectangle(0,0,5,5);
        paddleSprite = new Texture("breakout_pieces.png");
    }
    public void render(SpriteBatch batch) {
    }


}
