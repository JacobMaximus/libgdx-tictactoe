package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final Main game;
    ShapeRenderer shapeRenderer;

    float gridSize = 300; // size of the whole grid (square)
    float cellSize = gridSize / 3f;
    float startX;
    float startY;

    public GameScreen(final Main game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        drawGrid(); // Optional: your grid drawing function
        game.batch.end();

        logic();

    }

    private void logic(){
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);

//            System.out.println(startX + " " + startY + " " + touchPos);
            if(touchPos.x > startX && touchPos.x < startX + 3 * cellSize
                && touchPos.y > startY && touchPos.y < startY + 3 * cellSize)
                performPlayerAction(touchPos);
        }
    }

    private void performPlayerAction(Vector2 touchPos) {
        if (touchPos.x < startX + cellSize && touchPos.y < startY + cellSize) System.out.println('0');
    }


    private void drawGrid() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        // Vertical lines
        for (int i = 1; i < 3; i++) {
            float x = startX + (i * cellSize);
            shapeRenderer.line(x, startY, x, startY + gridSize);
        }

        // Horizontal lines
        for (int i = 1; i < 3; i++) {
            float y = startY + (i * cellSize);
            shapeRenderer.line(startX, y, startX + gridSize, y);
        }

        shapeRenderer.end();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        startX = (game.viewport.getWorldWidth() - gridSize) / 2f; // center X
        startY = (game.viewport.getWorldHeight() - gridSize) / 2f; // center Y
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
