package com.tictactoe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
//    private Texture image;
    public BitmapFont font;
    public FitViewport viewport;

    @Override
    public void create() {
//        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");

        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();
        viewport = new FitViewport(360, 640); // portrait layout


        //font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);

        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());


        this.setScreen(new MainMenuScreen(this));

    }

    @Override
    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        batch.draw(image, 140, 210);
//        batch.end();
        super.render(); // important!
    }

    @Override
    public void dispose() {
//        batch.dispose();
//        image.dispose();
        batch.dispose();
        font.dispose();
    }
}
