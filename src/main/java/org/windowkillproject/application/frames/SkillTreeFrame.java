package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.PrimaryPanel;
import org.windowkillproject.application.panels.SkillTreePanel;

import javax.swing.*;
import java.awt.*;

public class SkillTreeFrame extends JFrame{
    public SkillTreeFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.APP_TITLE);
        this.setLayout(null);
        setContentPane(new SkillTreePanel());
    }
}
