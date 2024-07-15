package org.windowkillproject.model.entities.enemies.attackstypes;

import org.windowkillproject.model.ObjectModel;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel.blackOrbModels;

public interface LaserOperator {
    ArrayList<Point2D> LASER_ENDS = new ArrayList<>();
    ArrayList<Point2D> LASER_LINES = new ArrayList<>();

    default void setLaserEnds() {
        LASER_ENDS.clear();

        for (int i = 0; i < blackOrbModels.size(); i++) {
            BlackOrbModel blackOrbModel = blackOrbModels.get(i);
            Point2D anchor = blackOrbModel.getAnchor();
            LASER_ENDS.add(anchor);
        }
        setLasers();
    }

    private void setLasers() {
        LASER_LINES.clear();
        for (int i = 0; i < LASER_ENDS.size() - 1; i++) {
            for (int j = i + 1; j < LASER_ENDS.size(); j++) {
                double x = LASER_ENDS.get(i).getX();
                double y = LASER_ENDS.get(i).getY();
                double endX = LASER_ENDS.get(j).getX();
                double endY = LASER_ENDS.get(j).getY();

                for (int t = 0; t < 500; t++) {
                    x += (endX - x) / 500;
                    y += (endY - y) / 500;
                    LASER_LINES.add(new Point2D.Double(x, y));
                }
            }
        }
    }

    Point2D getAnchor();

}
