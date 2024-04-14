package org.windowkillproject.application;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Config {
    public static final String APP_TITLE = "Window Kill - Main Menu";
    public static final String GAME_TITLE = "Window Kill - Game";
    public static final String SHOP_TITLE = "Window Kill - Shop";
    public static final int APP_WIDTH = 1000;
    public static final int APP_HEIGHT = 600;

    public static final int GAME_WIDTH = 700;
    public static final int GAME_HEIGHT = 700;
    public static final int FPS = 35;
    public static final int UPS = 50;
    public static final int LOOP = 5 * 1000;
    public static int ENEMY_RADIUS = 20;

    public static final int MAX_ENEMY_SPEED = 5;
    public static final int MIN_ENEMY_SPEED = 2;

    public static final int EPSILON_SPEED = 3;
    public static final int FRAME_SHRINKAGE_SPEED = 2;
    public static final int GAME_MIN_SIZE = 300;
    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 80;
    public static final int EPSILON_RADIUS = 14;
    public static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 25);
    public static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 25);
    public static final Color BUTTON_BG_COLOR = Color.decode("#634c64");
    public static final Color GAME_BG_COLOR = Color.decode("#634c64");
    public static final Color BUTTON_FG_COLOR = Color.white;
    public static final int BULLET_HIT_DOWN = 1;
    public static final int BULLET_HIT_UP = 0;
    public static final int BULLET_HIT_LEFT = 2;
    public static final int BULLET_HIT_RIGHT = 3;
    public static final double FRAME_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / FPS;
    public static final double MODEL_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / UPS;

}
