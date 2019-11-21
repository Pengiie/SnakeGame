package com.penguinz22.SnakeGame.fruits;

import com.Penguinz22.Rex.Core;
import com.Penguinz22.Rex.assets.Texture;
import com.Penguinz22.Rex.utils.Color;
import com.Penguinz22.Rex.utils.Rotation;
import com.penguinz22.SnakeGame.GameSettings;

public abstract class Fruit {
    public int x, y;
    final Texture texture;

    public Fruit(String textureLoc, int locX, int locY) {
        Core.assets.load(textureLoc, Texture.class);
        Core.assets.finishLoading();
        this.texture = Core.assets.get(textureLoc);
        this.x = locX;
        this.y = locY;
    }

    public void draw() {
        Core.renderer.draw(texture, Color.white, x* GameSettings.TILE_SIZE, y*GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, new Rotation(0));
    }

    public abstract FruitBuff getBuff();
}