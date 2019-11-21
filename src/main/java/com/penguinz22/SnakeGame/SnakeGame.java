package com.penguinz22.SnakeGame;

import com.Penguinz22.Rex.Core;
import com.Penguinz22.Rex.assets.AssetManager;
import com.Penguinz22.Rex.assets.Texture;
import com.Penguinz22.Rex.graphics.BatchRenderer;
import com.Penguinz22.Rex.graphics.Draw;
import com.Penguinz22.Rex.gui.GuiRenderer;
import com.Penguinz22.Rex.gui.components.GuiImage;
import com.Penguinz22.Rex.gui.constraints.FinalConstraint;
import com.Penguinz22.Rex.gui.constraints.GuiConstraints;
import com.Penguinz22.Rex.gui.constraints.RelativeConstraint;
import com.Penguinz22.Rex.listeners.ApplicationListener;
import com.Penguinz22.Rex.utils.Key;
import com.penguinz22.SnakeGame.fruits.Apple;
import com.penguinz22.SnakeGame.fruits.Fruit;
import com.penguinz22.SnakeGame.snakes.AISnake;
import com.penguinz22.SnakeGame.snakes.PlayerSnake;
import com.penguinz22.SnakeGame.snakes.Snake;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame implements ApplicationListener {

    private GameMode mode;

    private List<Snake> snakes = new ArrayList<>();
    private Fruit fruit;

    private final int TICK_PER_SECOND = 8;
    private float partialSeconds = 0;

    private boolean startedGame = false;

    @Override
    public void init() {
        Core.renderer = new BatchRenderer();
        Core.assets = new AssetManager(true);
        Core.guiRenderer = new GuiRenderer();

        Core.assets.load("images/singlePlayer.png", Texture.class);
        Core.assets.load("images/twoPlayer.png", Texture.class);
        Core.assets.load("images/ai.png", Texture.class);

        Core.assets.load("images/singlePlayer-hover.png", Texture.class);
        Core.assets.load("images/twoPlayer-hover.png", Texture.class);
        Core.assets.load("images/ai-hover.png", Texture.class);
        Core.assets.finishLoading();

        GuiConstraints constraints = new GuiConstraints();
        constraints.setXConstraint(new RelativeConstraint(0.25f));
        constraints.setWidthConstraint(new RelativeConstraint(0.5f));
        constraints.setYConstraint(new RelativeConstraint(0.725f));
        constraints.setHeightConstraint(new FinalConstraint(Core.window.getHeight()/7));

        GuiImage singleImage = new GuiImage(Core.assets.get("images/singlePlayer.png"));
        Core.guiRenderer.screenComponent.add(singleImage, constraints);

        singleImage.setMouseEnterCallback(() -> {
            singleImage.texture = Core.assets.get("images/singlePlayer-hover.png");
        });

        singleImage.setMouseLeaveCallback(() -> {
            singleImage.texture = Core.assets.get("images/singlePlayer.png");
        });

        singleImage.setMouseClickCallback(() -> {
            mode = GameMode.SINGLEPLAYER;
            startGame();
        });

        constraints.setYConstraint(new RelativeConstraint(0.425f));

        GuiImage twoImage = new GuiImage(Core.assets.get("images/twoPlayer.png"));
        Core.guiRenderer.screenComponent.add(twoImage, constraints);

        twoImage.setMouseEnterCallback(() -> {
            twoImage.texture = Core.assets.get("images/twoPlayer-hover.png");
        });

        twoImage.setMouseLeaveCallback(() -> {
            twoImage.texture = Core.assets.get("images/twoPlayer.png");
        });

        twoImage.setMouseClickCallback(() -> {
            mode = GameMode.TWOPLAYER;
            startGame();
        });

        constraints.setYConstraint(new RelativeConstraint(0.125f));

        GuiImage aiImage = new GuiImage(Core.assets.get("images/ai.png"));
        Core.guiRenderer.screenComponent.add(aiImage, constraints);

        aiImage.setMouseEnterCallback(() -> {
            aiImage.texture = Core.assets.get("images/ai-hover.png");
        });

        aiImage.setMouseLeaveCallback(() -> {
            aiImage.texture = Core.assets.get("images/ai.png");
        });

        aiImage.setMouseClickCallback(() -> {
            mode = GameMode.TWOPLAYERAI;
            startGame();
        });

    }

    private void startGame() {
        startedGame = true;
        randomFruit();

        if(mode == GameMode.SINGLEPLAYER) {
            snakes.add(new PlayerSnake(
                    new Snake.Position(5, 5),
                    "images/green_snake_head.png",
                    "images/green_snake_body.png",
                    Key.KEY_UP, Key.KEY_LEFT,Key.KEY_RIGHT,Key.KEY_DOWN));
        } else if(mode == GameMode.TWOPLAYER) {
            snakes.add(new PlayerSnake(
                    new Snake.Position(5, 5),
                    "images/green_snake_head.png",
                    "images/green_snake_body.png",
                    Key.KEY_UP, Key.KEY_LEFT,Key.KEY_RIGHT,Key.KEY_DOWN));
            snakes.add(new PlayerSnake(
                    new Snake.Position(15, 5),
                    "images/red_snake_head.png",
                    "images/red_snake_body.png",
                    Key.KEY_W,Key.KEY_A,Key.KEY_D,Key.KEY_S));
        } else if(mode == GameMode.TWOPLAYERAI) {
            Snake other = null;
            snakes.add(other = new PlayerSnake(
                    new Snake.Position(5, 5),
                    "images/green_snake_head.png",
                    "images/green_snake_body.png",
                    Key.KEY_UP, Key.KEY_LEFT,Key.KEY_RIGHT,Key.KEY_DOWN));
            snakes.add(new AISnake(
                    new Snake.Position(15, 5),
                    "images/red_snake_head.png",
                    "images/red_snake_body.png",
                    other, fruit));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void update() {
        // Display debug FPS and Delta Time data
        //System.out.println(Core.timings.getDeltaTime()+" "+ Core.timings.getFps());

        // Check if should tick
        if(!startedGame) {
            Core.guiRenderer.updateScreen();
            return;
        }
        boolean shouldTick = false;

        partialSeconds += Core.timings.getDeltaTime();
        if(partialSeconds >= (float) 1/TICK_PER_SECOND) {
            shouldTick = true;
            partialSeconds = 0;
        }

        // Update snaked and tick them if needed
        for (Snake snake : snakes) {
            snake.update(shouldTick);
        }

        // Check snake collision

        // Check fruit vs snake collision
        int hitCount = 0;
        for (Snake snake : snakes) {
            if(snake.getHeadPos().x == fruit.x && snake.getHeadPos().y == fruit.y) {
                snake.applyBuffs(fruit.getBuff());
                snake.ateFruit();
                hitCount++;
            }
        }
        if(hitCount > 1) {
            gameOver("No one wins its a tie, you both tried to get the fruit");
        } else if(hitCount == 1) {
            randomFruit();
        }

        // Check snake vs wall collision

        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if(snake.getHeadPos().x < 0 || snake.getHeadPos().x >= GameSettings.SIZE ||
            snake.getHeadPos().y < 0 || snake.getHeadPos().y >= GameSettings.SIZE) {
                gameOver("Snake player "+(i+1)+" has died and lost :(!");
            }
        }

        // Check snake vs snake

        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            for (Snake tempSnake : snakes) {
                for (int j = 0; j < tempSnake.getSegments().size(); j++) {
                    Snake.Position segment = tempSnake.getSegments().get(j);
                    if(j == 0)
                        continue;
                    if(snake.getHeadPos().x == segment.x && snake.getHeadPos().y == segment.y) {
                        if(i != snakes.indexOf(tempSnake))
                            gameOver("Snake player " + (i + 1) + " decided to run into player " + (snakes.indexOf(tempSnake) + 1) + " and died!");
                        else
                            gameOver("Snake player " + (i + 1) + " decided to end their own life and died!");
                    }
                }
            }
        }
    }

    private void gameOver(String message) {
        Core.application.shutdownApplication();
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public void render() {
        Draw.clear();
        if(!startedGame) {
            Core.guiRenderer.renderScreen();
            return;
        }
        fruit.draw();

        // Render each snake in orders from player one down
        for (Snake snake : snakes) {
            snake.draw();
        }

        Core.renderer.finish();
    }

    private void randomFruit() {
        Random random = new Random();
        boolean foundSpot = false;
        int x = 0;
        int y = 0;
        while(!foundSpot) {
            x = random.nextInt(GameSettings.SIZE);
            y = random.nextInt(GameSettings.SIZE);
            boolean good = true;
            for (Snake snake : snakes) {
                for (Snake.Position segment : snake.getSegments()) {
                    if(segment.x == x && segment.y == y)
                        good = false;
                }
            }
            if(good)
                foundSpot = true;
        }
        this.fruit = new Apple(x, y);
        for (Snake snake : snakes) {
            if(snake instanceof AISnake)
                ((AISnake) snake).updateFruit(fruit);
        }
    }

}
