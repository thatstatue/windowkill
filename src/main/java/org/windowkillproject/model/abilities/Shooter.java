package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.windowkillproject.application.Application.gameFrame;

public class Shooter extends Ability {
    private EpsilonModel parent;
    public Shooter(int x, int y, EpsilonModel parent) {
        super(x, y);
        this.parent = parent;
    }

    @Override
    public EpsilonModel getParent() {
        return parent;
    }
    public void setParent(EpsilonModel parent) {
        this.parent = parent;
    }

    private MouseInputAdapter mouseListener = initAdapter();
    private MouseInputAdapter initAdapter(){
        return new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x1 = e.getX();
                int y1 = e.getY();
                if (getParent() == null) setParent(gameFrame.getGamePanel().getEpsilon());
                Bullet bullet = new Bullet(getParent().getXO(), getParent().getYO());
                parent.getBullets().add(bullet);
                double theta = Math.atan2(y1 - bullet.getY(), x1 - bullet.getX());
                bullet.setShoot(true);
                bullet.setTheta(theta);
                bullet.move();

            }
        };
    }

    public MouseListener getMouseListener() {
        if (mouseListener == null) mouseListener = initAdapter();
        return mouseListener;
    }
}

