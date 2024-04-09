package org.windowkillproject.view;

import java.awt.geom.Point2D;
import static org.windowkillproject.application.Config.EPSILON_RADIUS;

public class EpsilonView {
    Point2D currentLocation=new Point2D.Double(0,0);
    double radius;
    public EpsilonView(String id) {
        this.radius = EPSILON_RADIUS;

    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Point2D currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
