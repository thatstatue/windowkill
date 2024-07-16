package org.windowkillproject.view.entities;

import org.windowkillproject.view.ImgData;

import java.awt.*;

import static org.windowkillproject.application.Config.EPSILON_RADIUS;

public class EpsilonView extends EntityView {
    double radius;

    public EpsilonView(String id) {
        super(id);
        this.radius = EPSILON_RADIUS;
        setImg(ImgData.getData().getEpsilon());
        entityViews.add(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX() + 2, getY() + 2, getWidth(), getHeight(), null);
    }
}