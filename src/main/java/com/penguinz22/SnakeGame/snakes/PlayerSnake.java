package com.penguinz22.SnakeGame.snakes;

import com.Penguinz22.Rex.Core;
import com.Penguinz22.Rex.utils.Key;

public class PlayerSnake extends Snake {

    private Key keyUp, keyRight, keyLeft, keyDown;

    public PlayerSnake(Position position, String snakeHead, String snakeBody, Key keyUp, Key keyRight, Key keyLeft, Key keyDown) {
        super(position, snakeHead, snakeBody);
        this.keyUp = keyUp;
        this.keyRight = keyRight;
        this.keyLeft = keyLeft;
        this.keyDown = keyDown;
    }

    @Override
    public Direction getDirInput() {
        if(Core.input.isKeyPressed(keyUp))
            return Direction.UP;
        if(Core.input.isKeyPressed(keyDown))
            return Direction.DOWN;
        if(Core.input.isKeyPressed(keyRight))
            return Direction.RIGHT;
        if(Core.input.isKeyPressed(keyLeft))
            return Direction.LEFT;
        return null;
    }
}
