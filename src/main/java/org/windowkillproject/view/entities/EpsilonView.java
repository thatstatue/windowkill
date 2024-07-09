package org.windowkillproject.view.entities;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.view.ImgData;
import org.windowkillproject.view.abilities.AbilityView;

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
    public  static EpsilonView getInstance(){
        for (EntityView entityView : entityViews){
            if (entityView instanceof EpsilonView){
                return (EpsilonView) entityView;
            }
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX() + 2, getY() + 2, getWidth(), getHeight(), null);
//        for (AbilityView abilityView : abilityViews) {
//            abilityView.paint(g);
//        }
    }
}