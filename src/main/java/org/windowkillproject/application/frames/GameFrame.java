package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.GamePanel;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getEnemiesKilled;


public class GameFrame extends JFrame {
    private boolean isStretching = false;
    private final JLabel clock = new JLabel("0:00");
    private final JLabel xp = new JLabel("✦0");
    private final JLabel hp = new JLabel("100 ♡");
    private final JLabel wave = new JLabel("~1");
    public GameFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setAlwaysOnTop(true);
        setTitle(Config.GAME_TITLE);
        this.setLayout(null);
        setContentPane(new GamePanel());

    }
    public GamePanel getGamePanel(){
        return (GamePanel) getContentPane();
    }
    public JLabel[] getLabels(){
        return new JLabel[]{new JLabel(""),clock,xp
                ,new JLabel(String.valueOf(getEnemiesKilled())),wave};
    }

    public void shrink() {

        int newX = getX() + Config.FRAME_SHRINKAGE_SPEED / 2;
        int newY = getY() + Config.FRAME_SHRINKAGE_SPEED / 2;
        int newWidth = getWidth() - Config.FRAME_SHRINKAGE_SPEED;
        int newHeight = getHeight() - Config.FRAME_SHRINKAGE_SPEED;
        if (getWidth() <= Config.GAME_MIN_SIZE) {
            newWidth = Config.GAME_MIN_SIZE;
            newX = getX();
        }
        if (getHeight() <= Config.GAME_MIN_SIZE) {
            newHeight = Config.GAME_MIN_SIZE;
            newY = getY();
        }
        if (!isStretching) {
            this.setBounds(newX, newY, newWidth, newHeight);
        }
    }
    public void stretch(int code){
        AtomicInteger count = new AtomicInteger();
        Timer stretchTimer = new Timer(Config.FPS, null);
        stretchTimer.addActionListener(e -> {
            int newX = getX();
            int newY = getY();
            int newWidth = getWidth();
            int newHeight = getHeight();
            switch (code){
                case BULLET_HIT_DOWN -> {
                    newY += FRAME_STRETCH_SPEED / 2;
                    newHeight += FRAME_STRETCH_SPEED;
                }
                case BULLET_HIT_LEFT -> {
                    newX -= FRAME_STRETCH_SPEED ;
                    newWidth += FRAME_STRETCH_SPEED;
                }
                case BULLET_HIT_RIGHT -> {
                    newX += FRAME_STRETCH_SPEED / 2;
                    newWidth += FRAME_STRETCH_SPEED;
                }
                case BULLET_HIT_UP -> {
                    newY -= FRAME_STRETCH_SPEED;
                    newHeight += FRAME_STRETCH_SPEED;
                }
                default -> throw new IllegalStateException("Unexpected value: " + code);
            }
            if (count.get() <7) {
                isStretching = true;
                fixEntityPositionsInFrame(code);
                setBounds(newX, newY, newWidth, newHeight);
                count.getAndIncrement();
            }else {
                isStretching = false;
                stretchTimer.stop();
            }
        });
        stretchTimer.start();
    }

    private static void fixEntityPositionsInFrame(int code) {
        if (code == BULLET_HIT_LEFT || code == BULLET_HIT_UP){
            for (EntityModel entityModel : entityModels){
                if (code == BULLET_HIT_LEFT) {
                    entityModel.move(FRAME_STRETCH_SPEED,0);
                }
                if (code == BULLET_HIT_UP) {
                    entityModel.move(0, FRAME_STRETCH_SPEED);
                }
            }
        }
    }

    public void setClockTime(String time){
        clock.setText(time);
    }
    public void setWaveLevel(int level){
        wave.setText("~"+level);
    }
    public void setXpAmount(int xp ){
        this.xp.setText("✦"+xp);
    }
    public void setHpAmount(int xp ){
        this.hp.setText(xp+ " ♡");
    }
    private void initLabels(){
        clock.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        clock.setForeground(Color.white);
        clock.setBounds(3, 1, 300, 20);
        this.add(clock);

        xp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        xp.setForeground(Color.cyan);
        xp.setBounds(60, 1, 300, 20);
        this.add(xp);

        hp.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        hp.setForeground(Color.green);
        hp.setBounds(180, 1, 300, 20);
        this.add(hp);

        wave.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        wave.setForeground(Color.red);
        wave.setBounds(140, 1, 300, 20);
        this.add(wave);

    }
    public void shrinkFast(){
        ElapsedTime.run();
        initLabels();

        Timer shrinkFastTimer = new Timer(1, null);
        shrinkFastTimer.addActionListener(e -> {
            int newX = getX() + Config.FRAME_SHRINKAGE_SPEED*3 / 2;
            int newY = getY() + Config.FRAME_SHRINKAGE_SPEED*3 / 2;
            int newWidth = getWidth() - Config.FRAME_SHRINKAGE_SPEED*3;
            int newHeight = getHeight() - Config.FRAME_SHRINKAGE_SPEED*3;
            boolean stoppedX = false, stoppedY = false;
            if (getWidth() <= Config.GAME_MIN_SIZE) {
                newWidth = Config.GAME_MIN_SIZE;
                newX = getX();
                stoppedX = true;
            }
            if (getHeight() <= Config.GAME_MIN_SIZE) {
                newHeight = Config.GAME_MIN_SIZE;
                newY = getY();
                stoppedY = true;
            }
            if (!(stoppedX && stoppedY)) {
                setBounds(newX, newY, newWidth, newHeight);
                EpsilonModel epsilonModel =  EpsilonModel.getINSTANCE();
                int deltaX = newWidth/2 - epsilonModel.getXO() - epsilonModel.getRadius();
                int deltaY = newHeight/2 - epsilonModel.getYO() - epsilonModel.getRadius();
                epsilonModel.move(deltaX, deltaY);
            }else {
                shrinkFastTimer.stop();
            }
        });
        shrinkFastTimer.start();
    }
}
