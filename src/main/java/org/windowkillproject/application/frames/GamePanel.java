package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.Enemy;
import org.windowkillproject.model.entities.enemies.Trigorath;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static org.windowkillproject.application.Application.gameFrame;

public class GamePanel extends Panel {
    private ArrayList<Entity> entities;
    private EpsilonModel epsilonModel;
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

    public EpsilonModel getEpsilon() {
        return epsilonModel;
    }

    public void setEpsilon(EpsilonModel epsilonModel) {
        this.epsilonModel = epsilonModel;
    }

    public GamePanel() {
        super();
        setBackground(Color.black);
        setPreferredSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));

        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected ArrayList<Component> initComponents() {
        initListeners();
        addKeyListener(movesKeyListener);
        entities = initEntities();
        return new ArrayList<>(entities);
    }

    private KeyListener movesKeyListener;

    private void initListeners() {
        movesKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
//todo
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> isLeftPressed = true;
                    case KeyEvent.VK_RIGHT -> isRightPressed = true;
                    case KeyEvent.VK_UP -> isUpPressed = true;
                    case KeyEvent.VK_DOWN -> isDownPressed = true;
                    default -> System.out.print("");//todo: update, width height rate
                }
                EpsilonModel epsilonModel1 = getEpsilon();
                int endX = epsilonModel1.getWidth() + epsilonModel1.getX() + epsilonModel1.getRadius();
                int endY = epsilonModel1.getHeight() + epsilonModel1.getY() + 3* epsilonModel1.getRadius();
                if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
                    if (isUpPressed && epsilonModel1.getY() - Config.EPSILON_SPEED >= 0)
                        epsilonModel1.moveY(-Config.EPSILON_SPEED);
                    else if (isDownPressed && endY + Config.EPSILON_SPEED <= gameFrame.getHeight())
                        epsilonModel1.moveY(Config.EPSILON_SPEED);
                    if (isLeftPressed && epsilonModel1.getX() - Config.EPSILON_SPEED >= 0)
                        epsilonModel1.moveX(-Config.EPSILON_SPEED);
                    else if (isRightPressed && endX + Config.EPSILON_SPEED <= gameFrame.getWidth())
                        epsilonModel1.moveX(Config.EPSILON_SPEED);

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
        EpsilonModel epsilonModel1 = getEpsilon();
        int endX = epsilonModel1.getWidth() + epsilonModel1.getX() + 5+ epsilonModel1.getRadius();
        int endY = epsilonModel1.getHeight() + epsilonModel1.getY() + 3* epsilonModel1.getRadius();
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

        epsilonModel = EpsilonModel.getINSTANCE();

        entities1.add(epsilonModel);

        return entities1;
    }

    public void createEnemy() {
        Trigorath trigorath = new Trigorath(300, 420);
        entities.add(trigorath);
        entities.add(new Trigorath(30, 100));
        //todo: fix
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Entity entity : entities) {
            entity.paint(g);
        }
    }
}
