package com.penguinz22.SnakeGame;

import com.Penguinz22.Rex.Application;
import com.Penguinz22.Rex.ApplicationConfig;
import com.Penguinz22.Rex.utils.Strings;
import jdk.nashorn.internal.scripts.JO;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.InputStream;

public class App {

    public static void main(String[] args) {

        InputStream stream = App.class.getResourceAsStream("/apple.png");
        if(stream == null)
            System.out.println("Yep Not Found");

        ApplicationConfig config = new ApplicationConfig();
        config.setTitle("Snake Game");
        config.setWindowSize(32*GameSettings.SIZE, 32*GameSettings.SIZE);

        Application application = new Application(new SnakeGame(), config);
    }

}
