package org.windowkillproject.server.model.panelmodels;

import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.abs;
import static org.windowkillproject.Constants.FRAME_SHRINKAGE_SPEED;

public class MainPanelModel extends PanelModel{
    public MainPanelModel(GlobeModel globeModel, Rectangle bounds, boolean flexible, boolean background) {
        super(globeModel, bounds,PanelStatus.shrinkable, flexible, background);
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
