package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Epsilon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private final ArrayList<Entity> entities;
    private Epsilon epsilon;
    public GamePanel() {
        requestFocus();
        setLayout(null);
        setBackground(Color.black);
        setPreferredSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        entities = initEntities();
        addEntitiesToPanel();
    }

    private ArrayList <Entity> initEntities(){
        ArrayList <Entity> entities1 = new ArrayList<>();

        epsilon = new Epsilon(Config.GAME_WIDTH / 2 , Config.GAME_HEIGHT / 2);
        entities1.add(epsilon);

        return entities1;
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
