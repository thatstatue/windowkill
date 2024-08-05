package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.windowkillproject.Constants.*;

public class SideFrame extends JFrame {
    public <T extends Panel> SideFrame(Class<T> panel , GameClient client) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(APP_TITLE);
        this.setLayout(null);
        try {
            Constructor<T> constructor = panel.getConstructor(GameClient.class);
            setContentPane(constructor.newInstance(client));
        } catch (NoSuchMethodException | InstantiationException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
