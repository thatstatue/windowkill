package org.windowkillproject.view;

import org.windowkillproject.model.abilities.BulletModel;

import java.awt.*;
import java.awt.geom.Point2D;
import static org.windowkillproject.application.Config.EPSILON_RADIUS;
import static org.windowkillproject.view.BulletView.bulletViews;

public class EpsilonView extends EntityView {
    double radius;
    public EpsilonView(String id) {
        super(id);
        this.radius = EPSILON_RADIUS;
        setImg(ImgData.getData().getEpsilon());
        entityViews.add(this);
    }


    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        for (BulletView bulletView : bulletViews){
            bulletView.paint(g);
        }
    }
}/* epsilonVertexView

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.fillOval(getX(), getY(), 5,5);
    }
 */
