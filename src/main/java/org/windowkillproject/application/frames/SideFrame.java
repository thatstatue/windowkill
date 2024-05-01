package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SideFrame extends JFrame {
    public <T extends Panel> SideFrame(Class<T> panel) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.APP_TITLE);
        this.setLayout(null);
        try {
            Constructor<T> constructor = panel.getConstructor();
            setContentPane(constructor.newInstance());
        } catch (NoSuchMethodException | InstantiationException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}