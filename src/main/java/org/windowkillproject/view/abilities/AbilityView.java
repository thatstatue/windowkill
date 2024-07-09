package org.windowkillproject.view.abilities;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.view.ObjectView;
import org.windowkillproject.view.Viewable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class AbilityView extends ObjectView {
    public AbilityView(String id, int x, int y) {
        super(id);
        this.x = x;
        this.y = y;
        abilityViews.add(this);
        setEnabled(true);
    }
    public static ArrayList<AbilityView> abilityViews = new ArrayList<>();



    public void set(int x, int y, int width, int height) {
        setX(x);
        setY(y);
    }
}
