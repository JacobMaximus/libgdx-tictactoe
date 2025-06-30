package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class GameLogic {
    final Main game;
    ShapeRenderer shapeRenderer;

//    float gridSize = 300; // size of the whole grid (square)
//    float cellSize = gridSize / 3f;
//    float startX;
//    float startY;

    int[][] matrix = {
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0}
    };
    int currentPlayer = 1;

    public GameLogic(Main game) {
        this.game = game;
    }

    public boolean logic(Vector2 touchPos, float startX, float startY, float cellSize, int player){
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);

//            System.out.println(startX + " " + startY + " " + touchPos);
            if(touchPos.x > startX && touchPos.x < startX + 3 * cellSize
                && touchPos.y > startY && touchPos.y < startY + 3 * cellSize)
                return performPlayerAction(touchPos, player);
        }
        return false;
    }

    private boolean performPlayerAction(Vector2 touchPos, int player) {
        int x = 0, y = 0;
        boolean valid = false;
        if (touchPos.x < startX + cellSize && touchPos.y < startY + cellSize) { x = 2; y = 0; valid = true;}
        else if (touchPos.x < startX + 2*cellSize && touchPos.y < startY + cellSize) { x = 2; y = 1; valid = true;}
        else if (touchPos.x < startX + 3*cellSize && touchPos.y < startY + cellSize) { x = 2; y = 2; valid = true;}

        else if (touchPos.x < startX + cellSize && touchPos.y < startY + 2*cellSize) { x = 1; y = 0; valid = true;}
        else if (touchPos.x < startX + 2*cellSize && touchPos.y < startY + 2*cellSize) { x = 1; y = 1; valid = true;}
        else if (touchPos.x < startX + 3*cellSize && touchPos.y < startY + 2*cellSize) { x = 1; y = 2; valid = true;}

        else if (touchPos.x < startX + cellSize && touchPos.y < startY + 3*cellSize) { x = 0; y = 0; valid = true;}
        else if (touchPos.x < startX + 2*cellSize && touchPos.y < startY + 3*cellSize) { x = 0; y = 1; valid = true;}
        else if (touchPos.x < startX + 3*cellSize && touchPos.y < startY + 3*cellSize) { x = 0; y = 2; valid = true;}

        if (valid)
            return updateBoard(x, y, player);
        return false;
    }

    private boolean updateBoard(int x, int y, int player){
        if (matrix[x][y] != 0){
            System.out.println("Box Filled");
//            logic(player); no need to call this as logic() is already under a loop. calling logic() will result in infinite recursion.
            return false;
        } else {
            matrix[x][y] = player;
            System.out.println(Arrays.deepToString(matrix));
            return true;
        }
    }
}
