package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.Shooter;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Epsilon;
import org.windowkillproject.model.entities.enemies.Enemy;
import org.windowkillproject.model.entities.enemies.Trigorath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import static org.windowkillproject.application.Application.gameFrame;

public class GamePanel extends JPanel {
    private ArrayList<Entity> entities;
    private Epsilon epsilon;
    boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed;
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    public ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (Entity entity: entities){
            if (entity instanceof Enemy) enemies.add((Enemy) entity);
        }
        return enemies;
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
        initListeners();
        addKeyListener(movesKeyListener);
        addMouseListener(shooter.getMouseListener());
        setFocusable(true);
        requestFocusInWindow();
    }

    private Shooter shooter;
    private KeyListener movesKeyListener;

    private void initListeners() {
        shooter = new Shooter(0 ,0 , null);
        movesKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (shooter == null || shooter.getParent() == null) shooter = new Shooter(10, 30, getEpsilon());
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> isLeftPressed = true;
                    case KeyEvent.VK_RIGHT -> isRightPressed = true;
                    case KeyEvent.VK_UP -> isUpPressed = true;
                    case KeyEvent.VK_DOWN -> isDownPressed = true;
                    default -> System.out.print("");//todo: update, width height rate
                }
                Epsilon epsilon1 = getEpsilon();
                int endX = epsilon1.getWidth() + epsilon1.getX() + epsilon1.getRadius();
                int endY = epsilon1.getHeight() + epsilon1.getY() + 3*epsilon1.getRadius();
                if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
                    if (isUpPressed && epsilon1.getY() - Config.EPSILON_SPEED >= 0)
                        epsilon1.moveY(-Config.EPSILON_SPEED);
                    else if (isDownPressed && endY + Config.EPSILON_SPEED <= gameFrame.getHeight())
                        epsilon1.moveY(Config.EPSILON_SPEED);
                    if (isLeftPressed && epsilon1.getX() - Config.EPSILON_SPEED >= 0)
                        epsilon1.moveX(-Config.EPSILON_SPEED);
                    else if (isRightPressed && endX + Config.EPSILON_SPEED <= gameFrame.getWidth())
                        epsilon1.moveX(Config.EPSILON_SPEED);

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> isLeftPressed = false;
                    case KeyEvent.VK_RIGHT -> isRightPressed = false;
                    case KeyEvent.VK_UP -> isUpPressed = false;
                    case KeyEvent.VK_DOWN -> isDownPressed = false;
                    default -> System.out.print("");//todo: update
                }
            }
        };
    }
    public void checkBounds(){
        Epsilon epsilon1 = getEpsilon();
        int endX = epsilon1.getWidth() + epsilon1.getX() + 5+ epsilon1.getRadius();
        int endY = epsilon1.getHeight() + epsilon1.getY() + 3*epsilon1.getRadius();
        if (endY > gameFrame.getHeight()) {
            int deltaY = gameFrame.getHeight() - endY;
            getEpsilon().moveY(deltaY);
        }
        if (endX > gameFrame.getWidth()) {
            int deltaX = gameFrame.getWidth() - endX;
            getEpsilon().moveX(deltaX);
        }
    }


    private ArrayList<Entity> initEntities() {
        ArrayList<Entity> entities1 = new ArrayList<>();

        epsilon = new Epsilon(Config.GAME_WIDTH / 2, Config.GAME_HEIGHT / 2);
        if(shooter == null) shooter = new Shooter(0, 0, null);
        shooter.setParent(epsilon);
        entities1.add(epsilon);

        return entities1;
    }

    public void createEnemy() {
        Trigorath trigorath = new Trigorath(300, 420);
        entities.add(trigorath);
        entities.add(new Trigorath(30, 100));
        //this.add(trigorath);
    }

    public void addEntitiesToPanel() {
        for (Entity entity : entities) {
            this.add(entity);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Entity entity : entities) {
            entity.paint(g);
        }
    }
}
