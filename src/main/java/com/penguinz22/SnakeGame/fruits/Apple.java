package com.penguinz22.SnakeGame.fruits;

public class Apple extends Fruit {

    private static final String TEXTURE_PATH = "images/apple.png";

    public Apple(int locX, int locY) {
        super(TEXTURE_PATH, locX, locY);
    }

    @Override
    public FruitBuff getBuff() {
        return new FruitBuff(1, 0, 0);
    }
}
