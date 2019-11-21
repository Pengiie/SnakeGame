package com.penguinz22.SnakeGame.snakes;

import com.Penguinz22.Rex.Core;
import com.Penguinz22.Rex.utils.Color;
import com.penguinz22.SnakeGame.GameSettings;
import com.penguinz22.SnakeGame.fruits.Fruit;
import javafx.geometry.Pos;

import javax.swing.text.Segment;
import java.util.*;

public class AISnake extends Snake {

    private Snake other;
    private Fruit fruit;

    public AISnake(Position position, String snakeHead, String snakeBody, Snake other, Fruit fruit) {
        super(position, snakeHead, snakeBody);
        this.other = other;
        this.fruit = fruit;
    }

    private Node getLeastF(List<Node> nodes) {
        Node least = null;
        for (Node node : nodes) {
            if(least == null) {
                least = node;
                continue;
            }
            if(node.f < least.f)
                least = node;
        }
        return least;
    }

    @Override
    public Direction getDirInput() {

        List<Node> openList = new ArrayList<>();
        openList.add(new Node(null,
                new Position(getHeadPos().x, getHeadPos().y),
                0, new Position(fruit.x, fruit.y)));
        openList.get(0).f = 0;
        List<Node> closedList = new ArrayList<>();

        int steps = 0;

        while(!openList.isEmpty()&& steps != 1000) {
            steps++;

            Node openNode = getLeastF(openList);
            openList.remove(openNode);
            closedList.add(openNode);

            for(int i = -1; i <= 1; i ++)
                for(int j = -1; j <= 1; j ++)
                    if(i == 0 || j == 0) {
                        if(i == 0 && j == 0)
                            continue;
                        Node tmp = new Node(
                                openNode,
                                new Position(openNode.position.x + i, openNode.position.y + j),
                                openNode.g + 0.5f, new Position(fruit.x, fruit.y));
                        if(tmp.position.x == fruit.x && tmp.position.y == fruit.y) {
                            Node next = backTrackFromGoal(tmp);
                            if(next == null) {
                                return tmpDir;
                            }
                            int x = next.position.x - getHeadPos().x;
                            int y = next.position.y - getHeadPos().y;
                            if(x <= -1) {
                                return Direction.RIGHT;
                            } else if(x >= 1) {
                                return Direction.LEFT;
                            } else if(y >= 1) {
                                return Direction.UP;
                            } else if(y <= -1){
                                return Direction.DOWN;
                            }
                        }

                        if(tmp.position.x < 0 || tmp.position.x >= GameSettings.SIZE ||
                                tmp.position.y < 0 || tmp.position.y >= GameSettings.SIZE)
                            continue;

                        boolean add = true;
                        for (Node node : openList) {
                            if(node.position.x == tmp.position.x && node.position.y == tmp.position.y) {
                                add = false;
                                break;
                            }
                        }

                        for (Node node : closedList) {
                            if(node.position.x == tmp.position.x && node.position.y == tmp.position.y) {
                                add = false;
                                break;
                            }
                        }

                        for (Position segment : other.getLaggedSegments()) {
                                if(segment.x == tmp.position.x && segment.y == tmp.position.y) {
                                    add = false;
                                    break;
                                }
                        }
                        for (Position segment : this.getSegments()) {
                            if(segment.x == tmp.position.x && segment.y == tmp.position.y) {
                                add = false;
                                break;
                            }
                        }

                        if(this.getHeadPos().x == tmp.position.x && this.getHeadPos().y == tmp.position.y)
                            continue;

                        if(add)
                            openList.add(tmp);
                    }
        }
        Random random = new Random();
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    private Node backTrackFromGoal(Node node) {
        Node tmp = node;
        while(tmp.parent.parent != null) {
            tmp = tmp.parent;

            // Uncomment for debug
            //Core.renderer.draw(new Color(1, 0, 0, 1), tmp.position.x* GameSettings.TILE_SIZE -10, tmp.position.y*GameSettings.TILE_SIZE-10, 20, 20);
        }
        return tmp;
    }

    public void updateFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    private class Node {
        Node parent;
        Position position;
        float g, h, f;

        public Node(Node parent, Position pos, float g, Position fruitPos) {
            this.parent = parent;
            this.position = pos;
            this.g = g;
            this.h = Math.abs(fruitPos.x - pos.x) + Math.abs(fruitPos.y - pos.y);
            this.f = this.g + this.h;
        }
    }
}
