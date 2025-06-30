package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Arrays;
import java.util.Scanner;

public class GameScreen implements Screen {
    final Main game;
    GameLogic gameLogic;
    ShapeRenderer shapeRenderer;

    float gridSize = 300; // size of the whole grid (square)
    float cellSize = gridSize / 3f;
    float startX;
    float startY;

//    int[][] matrix = {
//        {0, 0, 0},
//        {0, 0, 0},
//        {0, 0, 0}
//    };
//
//    int currentPlayer = 1;

    public GameScreen(final Main game) {
        this.game = game;
        this.gameLogic = new GameLogic(game);
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
        drawSymbols();
        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            if (gameLogic.logic(touchPos, startX, startY, cellSize, gameLogic.currentPlayer))
                gameLogic.currentPlayer = 3 - gameLogic.currentPlayer;
        }

//        if (logic(currentPlayer))
//            currentPlayer = 3 - currentPlayer;
//        System.out.println(currentPlayer);

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
    private void drawSymbols() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float padding = 15f; // padding from cell edges
        float thickness = 5f; // line thickness for boldness

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                float x = startX + col * cellSize;
                float y = startY + row * cellSize;

                int flippedRow = 2 - row; // flip vertically to match logic

                if (gameLogic.matrix[flippedRow][col] == 1) {
                    // RED X
                    shapeRenderer.setColor(Color.RED);
                    drawThickLine(x + padding, y + padding, x + cellSize - padding, y + cellSize - padding, thickness);
                    drawThickLine(x + cellSize - padding, y + padding, x + padding, y + cellSize - padding, thickness);

                } else if (gameLogic.matrix[flippedRow][col] == 2) {
                    // BLUE O
                    shapeRenderer.setColor(Color.BLUE);
                    drawHollowCircle(x + cellSize / 2, y + cellSize / 2, (cellSize / 2 - padding), 5f);

                }
            }
        }

        shapeRenderer.end();
    }
    private void drawThickLine(float x1, float y1, float x2, float y2, float thickness) {
        // Calculate direction vector
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;

        // Calculate perpendicular vector for thickness
        float px = -dy * thickness / 2;
        float py = dx * thickness / 2;

        // Draw a quad (rectangle) for thickness
        shapeRenderer.triangle(x1 + px, y1 + py, x1 - px, y1 - py, x2 + px, y2 + py);
        shapeRenderer.triangle(x2 + px, y2 + py, x2 - px, y2 - py, x1 - px, y1 - py);
    }
    private void drawHollowCircle(float cx, float cy, float radius, float thickness) {
        int segments = 100;
        float angleStep = 2 * (float) Math.PI / segments;

        for (int i = 0; i < segments; i++) {
            float angle1 = i * angleStep;
            float angle2 = (i + 1) * angleStep;

            float x1Outer = cx + (float) Math.cos(angle1) * radius;
            float y1Outer = cy + (float) Math.sin(angle1) * radius;
            float x2Outer = cx + (float) Math.cos(angle2) * radius;
            float y2Outer = cy + (float) Math.sin(angle2) * radius;

            float x1Inner = cx + (float) Math.cos(angle1) * (radius - thickness);
            float y1Inner = cy + (float) Math.sin(angle1) * (radius - thickness);
            float x2Inner = cx + (float) Math.cos(angle2) * (radius - thickness);
            float y2Inner = cy + (float) Math.sin(angle2) * (radius - thickness);

            // Draw two triangles to simulate a ring segment
            shapeRenderer.triangle(x1Outer, y1Outer, x2Outer, y2Outer, x1Inner, y1Inner);
            shapeRenderer.triangle(x2Outer, y2Outer, x2Inner, y2Inner, x1Inner, y1Inner);
        }
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
