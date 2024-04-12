package org.windowkillproject.view.entities;

import org.windowkillproject.view.ImgData;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.abilities.BulletView;
import org.windowkillproject.view.entities.EntityView;

import java.awt.*;

import static org.windowkillproject.application.Config.EPSILON_RADIUS;
import static org.windowkillproject.view.abilities.AbilityView.abilityViews;

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
        for (AbilityView abilityView : abilityViews){
            abilityView.paint(g);
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
