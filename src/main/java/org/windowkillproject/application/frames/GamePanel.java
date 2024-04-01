package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Epsilon;
import org.windowkillproject.model.entities.enemies.Trigorath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private ArrayList<Entity> entities;
    private Epsilon epsilon;
    boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed;


    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public Epsilon getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(Epsilon epsilon) {
        this.epsilon = epsilon;
    }

    public GamePanel() {
        requestFocus();
        setLayout(null);
        setBackground(Color.black);
        setPreferredSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        entities = initEntities();
        createEnemy();
        addEntitiesToPanel();
        initKeyListener();
        addKeyListener(movesKeyListener);
        setFocusable(true);
        requestFocusInWindow();
    }

    private KeyListener movesKeyListener;
    private void initKeyListener(){
        movesKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> isLeftPressed = true;
                    case KeyEvent.VK_RIGHT-> isRightPressed = true;
                    case KeyEvent.VK_UP -> isUpPressed = true;
                    case KeyEvent.VK_DOWN -> isDownPressed = true;
                    default-> System.out.print("");//todo: update
                }

                if (!((isLeftPressed&& isRightPressed) || (isDownPressed && isUpPressed))){
                    if(isUpPressed) getEpsilon().moveY(-Config.EPSILON_SPEED);
                    else if (isDownPressed) getEpsilon().moveY(Config.EPSILON_SPEED);
                    if (isLeftPressed) getEpsilon().moveX(-Config.EPSILON_SPEED);
                    else if (isRightPressed) getEpsilon().moveX(Config.EPSILON_SPEED);

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> isLeftPressed = false;
                    case KeyEvent.VK_RIGHT-> isRightPressed = false;
                    case KeyEvent.VK_UP -> isUpPressed = false;
                    case KeyEvent.VK_DOWN -> isDownPressed = false;
                    default-> System.out.print("");//todo: update
                }
            }
        };
    }

    private ArrayList <Entity> initEntities(){
        ArrayList <Entity> entities1 = new ArrayList<>();

        epsilon = new Epsilon(Config.GAME_WIDTH / 2 , Config.GAME_HEIGHT / 2);
        entities1.add(epsilon);

        return entities1;
    }

    public void createEnemy(){
        Trigorath trigorath = new Trigorath(300, 420);
        entities.add(trigorath);
        entities.add(new Trigorath(30, 100));
        //this.add(trigorath);
    }

    public void addEntitiesToPanel(){
        for (Entity entity : entities) {
            this.add(entity);
        }
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (Entity entity : entities) {
            entity.paint(g);
        }
    }
}
