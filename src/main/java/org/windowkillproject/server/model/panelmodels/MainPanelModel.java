package org.windowkillproject.server.model.panelmodels;

import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.abs;
import static org.windowkillproject.Constants.*;

public class MainPanelModel extends PanelModel{
    public MainPanelModel(GlobeModel globeModel, String id) {
        super(globeModel, id,
                new Rectangle(
                        getDefaultToolkit().getScreenSize().width - GAME_WIDTH/2,
                        getDefaultToolkit().getScreenSize().height - GAME_HEIGHT/2,
                        GAME_WIDTH, GAME_HEIGHT
                ), PanelStatus.shrinkable, true, false);
    }
    public boolean isPunched() {
        return punched;
    }

    public void gotPunched(Point2D punchPoint){
        double rightD = punchPoint.getX()- (x + width/2D); // if positive, is hitting the right
        double bottomD = punchPoint.getY()- (y + height/2D); // if positive, is hitting the bottom

        int newX = getX();
        int newY = getY();
        int newWidth = getWidth();
        int newHeight = getHeight();

        if (abs(rightD)< abs(bottomD)){
            newWidth -= 2*FRAME_SHRINKAGE_SPEED;
            if (rightD<0)
                newX += FRAME_SHRINKAGE_SPEED;
        }else{
            newHeight -= 2*FRAME_SHRINKAGE_SPEED;
            if (bottomD<0)
                newY += FRAME_SHRINKAGE_SPEED;
        }
        setBounds(newX, newY, newWidth, newHeight);
        punched = true;
    }
    private boolean punched;
}
