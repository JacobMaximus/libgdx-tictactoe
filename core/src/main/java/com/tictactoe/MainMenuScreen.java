package com.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tictactoe.firebase.FirebaseService;

import java.util.Scanner;

public class MainMenuScreen implements Screen {
    final Main game;

    public MainMenuScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        //draw text. Remember that x and y are in meters
        game.font.draw(game.batch, "Welcome to Drop!!! ", 1, 10.5f);
        game.font.draw(game.batch, "Tap anywhere to begin!", 1, 1);
        game.batch.end();

        if (Gdx.input.isTouched()) {
//            game.setScreen(new GameScreen(game));
//            dispose();
            String roomCode = onCreateRoomClicked();
           onJoinRoomClicked(roomCode);
        }
    }

    public String onCreateRoomClicked() {
        String myRoomCode = FirebaseService.createRoom();
        System.out.println("My Room Code: " + myRoomCode);
        // Optionally, store this to show on screen or switch to GameScreen
        return myRoomCode;
    }
    public void onJoinRoomClicked(String enteredCode) {
        boolean success = FirebaseService.joinRoom(enteredCode);
        if (success) {
            System.out.println("Joined room " + enteredCode);
//            game.setScreen(new GameScreen(game, enteredCode)); // Pass roomCode to GameScreen
        } else {
            System.out.println("Invalid room code.");
        }
    }



    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
