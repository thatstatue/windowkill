package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.Epsilon;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.windowkillproject.application.Application.gameFrame;

public class Shooter extends Ability {
    private Epsilon parent;
    public Shooter(int x, int y, Epsilon parent) {
        super(x, y);
        this.parent = parent;
    }

    @Override
    public Epsilon getParent() {
        return parent;
    }
    public void setParent(Epsilon parent) {
        this.parent = parent;
    }

    private final MouseInputAdapter mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (getParent() == null) setParent(gameFrame.getGamePanel().getEpsilon());
            Bullet bullet = new Bullet(getParent().getXO(), getParent().getYO());
            parent.getBullets().add(bullet);
            double theta = Math.atan2(y - bullet.getY(), x - bullet.getX());
            bullet.setShoot(true);
            bullet.setTheta(theta);
            bullet.move();

        }
    };

    public MouseListener getMouseListener() {
        return mouseListener;
    }
}

