package org.windowkillproject.controller;

import org.windowkillproject.application.Config;

import java.awt.geom.Point2D;

public abstract class Utils {
    public static double vectorTheta(Point2D point, Point2D anchor){
        return Math.atan2(point.getY() - anchor.getY(), point.getX() - anchor.getX());
    }
    public static Point2D unitVector(Point2D point, Point2D anchor){
        double dY = point.getY() - anchor.getY();
        double dX = point.getX() - anchor.getX();
        double magnitude = Math.sqrt(dX*dX + dY*dY);
        System.out.println(dX + "----" + dY);
        return new Point2D.Double(dX/magnitude,dY/magnitude );
    }
    private static double deltaSpeed(Point2D entityModel, Point2D other){
        double d1 = entityModel.getX() - other.getX();
        double d2 = entityModel.getY() - other.getY();
        double d = Math.sqrt(d1* d1 + d2*d2);
        int speed = (int) (d/25 + 0.75);
        return Math.min(Config.MAX_ENEMY_SPEED , speed);
    }
    public static Point2D routePoint(Point2D entity, Point2D other){
        double speed = deltaSpeed(entity, other);
        Point2D vector = unitVector(other, entity);
        return weighedVector(vector, speed);

    }
    public static Point2D impactPoint(Point2D entity, Point2D other){
        double speed = deltaSpeedAway(entity, other);
        Point2D vector = unitVector(entity, other);
        return weighedVector(vector, speed*8);
    }

    private static double deltaSpeedAway(Point2D point, Point2D anchor){
        double d1 = point.getX() - anchor.getX();
        double d2 = point.getY() - anchor.getY();
        double d = Math.sqrt(d1* d1 + d2*d2);
        double speed = Config.MAX_ENEMY_SPEED*2 - (d/5);
        return Math.max(0 , speed);
    }
    public static Point2D weighedVector(Point2D point, double scalar){
        return new Point2D.Double(point.getX()*scalar,point.getY()*scalar);
    }
    public static Point2D addVectors(Point2D point1,Point2D point2){
        return new Point2D.Double(point1.getX()+point2.getX(),point1.getY()+point2.getY());
    }

}
