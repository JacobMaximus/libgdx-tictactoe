package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    float endMessageTimer = 0f;
    String endMessage = "";
    boolean roundEnded = false;

    BitmapFont font;
//    int currentWinner = 0;

    public GameScreen(final Main game) {
        this.game = game;
        this.gameLogic = new GameLogic(game);
        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 64; // HIGH resolution
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        font = generator.generateFont(parameter);
        font.getData().setScale(0.5f); // Optional, scale it to look better in-game
        generator.dispose();



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
//        gameStatus();
        game.batch.end();

        game.batch.begin();

        font.setColor(Color.BLACK);

// Position both players centered *above* the grid
        float scoreY = startY + gridSize + 70;  // well above the grid, but within viewport
        float p1X = startX + 15;                // near left edge of grid
        float p2X = startX + gridSize - 135;    // near right edge of grid

        font.draw(game.batch, "Player 1", p1X, scoreY+5);
        font.draw(game.batch, "Score: " + gameLogic.playerScore.get(1), p1X, scoreY - 30);

        font.draw(game.batch, "Player 2", p2X, scoreY+5);
        font.draw(game.batch, "Score: " + gameLogic.playerScore.get(2), p2X, scoreY - 30);

        game.batch.end();

        if (!gameLogic.gameOver && Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            if (gameLogic.logic(touchPos, startX, startY, cellSize, gameLogic.currentPlayer)){
                int winner = gameLogic.winner();
                if (winner != 0){
                    roundEnded = true;
                    endMessageTimer = 2f; // 2 seconds
                    if (winner == 3){
                        endMessage = "DRAW!";
                    } else {
                        endMessage = "PLAYER " + winner + " - WINNER";
                    }
                    System.out.println(gameLogic.playerScore.get(winner) + " " + gameLogic.playerScore.get(3 - winner));

                }
            }
        }

        if (roundEnded) {
            endMessageTimer -= delta;
            if (endMessageTimer <= 0) {
                roundEnded = false;
                endMessage = "";
//                currentWinner = 0;
                gameLogic.newGame();
            }
        }

    }

    private void gameStatus() {
        // --- Always draw the scores ---
        font.setColor(Color.BLACK);
        font.draw(game.batch, "Player 1", startX - 90, startY + gridSize + 60);
        font.draw(game.batch, "" + gameLogic.playerScore.get(1), startX - 70, startY + gridSize + 30);

        font.draw(game.batch, "Player 2", startX + gridSize + 10, startY + gridSize + 60);
        font.draw(game.batch, "" + gameLogic.playerScore.get(2), startX + gridSize + 30, startY + gridSize + 30);

        // --- Only show this during round end ---
        if (roundEnded && !endMessage.isEmpty()) {
            font.setColor(Color.FIREBRICK);
            float msgX = startX + gridSize / 2 - font.getRegion().getRegionWidth() / 2f;
            float msgY = startY + gridSize / 2;
            font.draw(game.batch, endMessage, msgX, msgY);
        }
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
        font.dispose();
        shapeRenderer.dispose();
    }
}
