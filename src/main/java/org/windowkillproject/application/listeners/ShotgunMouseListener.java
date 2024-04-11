package org.windowkillproject.application.listeners;

import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import static org.windowkillproject.model.abilities.BulletModel.bulletModels;

public class ShotgunMouseListener extends MouseInputAdapter {
    public void mouseClicked(MouseEvent e) {
        int x1 = e.getX();
        int y1 = e.getY();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        BulletModel bulletModel = new BulletModel(epsilonModel.getXO(), epsilonModel.getYO());
        double theta = Math.atan2(y1 - bulletModel.getY(), x1 - bulletModel.getX());
        bulletModel.setShoot(true);
        bulletModel.setTheta(theta);
        bulletModel.move();
    }
}
