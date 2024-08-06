package org.windowkillproject.server.model.entities.enemies.attackstypes;

import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface LaserOperator {
    ArrayList<Point2D> LASER_ENDS = new ArrayList<>();
    ArrayList<Point2D> LASER_LINES = new ArrayList<>();

    default void setLaserEnds() {
        LASER_ENDS.clear();

        for (int i = 0; i < getGlobeModel().blackOrbModels.size(); i++) {
            BlackOrbModel blackOrbModel = getGlobeModel().blackOrbModels.get(i);
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
    GlobeModel getGlobeModel();
}
