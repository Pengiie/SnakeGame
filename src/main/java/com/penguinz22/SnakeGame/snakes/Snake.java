package com.penguinz22.SnakeGame.snakes;

import com.Penguinz22.Rex.Core;
import com.Penguinz22.Rex.assets.Texture;
import com.Penguinz22.Rex.utils.Color;
import com.Penguinz22.Rex.utils.Rotation;
import com.penguinz22.SnakeGame.GameSettings;
import com.penguinz22.SnakeGame.fruits.FruitBuff;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public abstract class Snake {

    private final int LAG = 6;
    private int counter = 0;
    private List<Position> laggedSegments = new ArrayList<>();

    private List<Position> segments = new ArrayList<>();
    private Texture headTexture, bodyTexture;

    private Direction dir = Direction.UP;
    protected Direction tmpDir = dir;

    private boolean justAte = false;

    public abstract Direction getDirInput();

    public Snake(Position position, String snakeHead, String snakeBody) {
        this.segments.add(position);
        this.segments.add(new Position(position.x, position.y-1));
        Core.assets.load(snakeHead, Texture.class);
        Core.assets.load(snakeBody, Texture.class);
        Core.assets.finishLoading();
        this.headTexture = Core.assets.get(snakeHead);
        this.bodyTexture = Core.assets.get(snakeBody);
    }

    public void update(boolean shouldTick) {
        Direction tmpDir = getDirInput();
        if(tmpDir != null) {
            if(allowedMovement(tmpDir))
                this.tmpDir = tmpDir;
        }

        if(shouldTick) {
            this.dir = this.tmpDir;
            if(this.dir == Direction.UP) {
                updateSegments(0, 1);
            } else if(this.dir == Direction.DOWN) {
                updateSegments(0, -1);
            } else if(this.dir == Direction.RIGHT) {
                updateSegments(-1, 0);
            } else if(this.dir == Direction.LEFT) {
                updateSegments(1, 0);
            }
            counter++;
            if(counter == LAG) {
                counter = 0;
                laggedSegments = new ArrayList<>(segments);
            }
        }
    }

    private boolean allowedMovement(Direction dir) {
        if(dir == Direction.DOWN && this.dir == Direction.UP)
            return false;
        if(dir == Direction.UP && this.dir == Direction.DOWN)
            return false;
        if(dir == Direction.RIGHT && this.dir == Direction.LEFT)
            return false;
        if(dir == Direction.LEFT && this.dir == Direction.RIGHT)
            return false;
        return true;
    }

    private void updateSegments(int x, int y) {
        Position headPos = segments.get(0);
        segments.add(0, new Position(headPos.x+x, headPos.y+y));
        if(!justAte)
            segments.remove(segments.size()-1);
        else
            justAte = false;
    }

    public void draw() {
        for (int i = 0; i < segments.size(); i++) {
            Position pos = segments.get(i);
            Rotation rot = null;
            if(this.dir == Direction.UP) {
                rot = new Rotation(Rotation.Origin.CENTER, 180);
            } else if(this.dir == Direction.DOWN) {
                rot = new Rotation(Rotation.Origin.CENTER, 0);
            } else if(this.dir == Direction.RIGHT) {
                rot = new Rotation(Rotation.Origin.CENTER, 90);
            } else if(this.dir == Direction.LEFT) {
                rot = new Rotation(Rotation.Origin.CENTER, 270);
            }

            if(i == 0) {
                Core.renderer.draw(headTexture, Color.white,
                        pos.x*GameSettings.TILE_SIZE, pos.y*GameSettings.TILE_SIZE,
                        GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, rot);
            } else {
                Core.renderer.draw(bodyTexture, Color.white,
                        pos.x*GameSettings.TILE_SIZE, pos.y*GameSettings.TILE_SIZE,
                        GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, rot);
            }
        }
    }

    public void applyBuffs(FruitBuff buff) {

    }

    public void ateFruit() {
        justAte = true;
    }

    public Position getHeadPos() {
        return segments.get(0);
    }

    public List<Position> getSegments() {
        return segments;
    }

    public List<Position> getLaggedSegments() {
        return laggedSegments;
    }

    public enum Direction {
        UP, LEFT, DOWN, RIGHT;
    }

    public static class Position {
        public int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
