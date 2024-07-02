package org.windowkillproject.application;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Config {
    public static final String APP_TITLE = "Window Kill - Main Menu";
    public static final String GAME_TITLE = "Window Kill - Game";
    public static final String SCORE_TITLE = "Window Kill - Score";

    public static final int APP_WIDTH = 1000;
    public static final int APP_HEIGHT = 600;

    public static final int GAME_WIDTH = 700;
    public static final int GAME_HEIGHT = 700;
    public static final int OPTION_WIDTH = 300;
    public static final int OPTION_IMG_HEIGHT = 250;
    public static final int OPTION_HEIGHT = 700;
    public static final int FPS = 35;
    public static final int UPS = 60;
    public static final int WAVE_LOOP = 5 * 1000;
    public static int ENEMY_RADIUS = 20;
    public static final int IMPACT_DURATION = 7;
    public static final int BANISH_DURATION = 13;


    public static int MAX_ENEMY_SPEED = 5;
    public static final int MAX_ENEMIES = 4;
    public static final int PROJECTILE_TIMEOUT = 1;
    public static final int MIN_ENEMY_SPEED = 2;
    public static int SENSITIVITY_RATE = 0;

    public static int EPSILON_SPEED = 4;
    public static final int BULLET_SPEED = 7;

    public static final int FRAME_SHRINKAGE_SPEED = 2;
    public static int BOUND = 5;

    public static final int FRAME_STRETCH_SPEED = 3;
    public static int GAME_MIN_SIZE = 300;
    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 60;
    public static final int EPSILON_RADIUS = 14;
    public static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 25);
    public static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Color BUTTON_BG_COLOR = Color.decode("#634c64");
    public static final Color GAME_BG_COLOR = Color.decode("#634c64");
    public static final Color BUTTON_FG_COLOR = Color.white;
    public static final int WRIT_DURATION = 15;
    public static final int WRIT_COOL_DOWN_SECONDS = 5*60;


    public static final int BULLET_ATTACK_HP = 5;
    public static final int DOWN_CODE = 1;
    public static final int UP_CODE = 0;
    public static final int LEFT_CODE = 2;
    public static final int RIGHT_CODE = 3;
    public static final double FRAME_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / FPS;
    public static final double MODEL_UPDATE_TIME = (double) TimeUnit.SECONDS.toMillis(1) / UPS;

}
