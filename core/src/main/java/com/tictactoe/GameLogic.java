package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public boolean gameOver = false;

    public Map<Integer, Integer> playerScore = new HashMap<>(Map.of(1, 0, 2, 0));

    public GameLogic(Main game) {
        this.game = game;
    }

    public boolean logic(Vector2 touchPos, float startX, float startY, float cellSize, int player){

//            System.out.println(startX + " " + startY + " " + touchPos);
        if(touchPos.x > startX && touchPos.x < startX + 3 * cellSize
            && touchPos.y > startY && touchPos.y < startY + 3 * cellSize)
            return performPlayerAction(touchPos, startX, startY, cellSize, player);

        return false;
    }

    private boolean performPlayerAction(Vector2 touchPos, float startX, float startY, float cellSize, int player) {
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
            currentPlayer = 3 - currentPlayer;

//            System.out.println(Arrays.deepToString(matrix));
            return true;
        }
    }

    public int winner(){
        for (int i = 0; i < 3; i++){
            if (matrix[i][0] == 0) continue;
            if (matrix[i][0] == matrix[i][1] && matrix[i][1]== matrix[i][2]){
                gameOver = true;
                playerScore.put(matrix[i][0], playerScore.get(matrix[i][0]) + 1);
                return matrix[i][0];
            }
        }
        for (int i = 0; i < 3; i++){
            if (matrix[0][i] == 0) continue;
            if (matrix[0][i] == matrix[1][i] && matrix[1][i]== matrix[2][i]){
                gameOver = true;
                playerScore.put(matrix[0][i], playerScore.get(matrix[0][i]) + 1);
                return matrix[0][i];
            }
        }

        if (matrix[1][1] == 0) return 0;
        if (matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2]
        || matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0]){
            gameOver = true;
            playerScore.put(matrix[1][1], playerScore.get(matrix[1][1]) + 1);
            return matrix[1][1];
        }

        boolean allFilled = true;
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell == 0) {
                    allFilled = false;
                    break;
                }
            }
        }
        if (allFilled) {
            gameOver = true;
            return 3; // 3 = draw
        }

        return 0;
    }

    public void newGame(){
        matrix = new int[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        gameOver = false;

    }
}

