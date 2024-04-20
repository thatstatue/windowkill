package org.windowkillproject.controller;

import org.windowkillproject.application.Config;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.MIN_ENEMY_SPEED;

public abstract class Utils {
//    public static double vectorTheta(Point2D point, Point2D anchor){
//        return Math.atan2(point.getY() - anchor.getY(), point.getX() - anchor.getX());
//    }
    public static Point2D unitVector(Point2D point, Point2D anchor){
        double dY = point.getY() - anchor.getY();
        double dX = point.getX() - anchor.getX();
        double magnitude = Math.sqrt(dX*dX + dY*dY);
        return new Point2D.Double(dX/magnitude,dY/magnitude );
    }
    private static double accelSpeed(Point2D entityModel, Point2D other){
        double d1 = entityModel.getX() - other.getX();
        double d2 = entityModel.getY() - other.getY();
        double d = Math.sqrt(d1* d1 + d2*d2);
        int speed = (int) (d/25 + 0.75);
        return Math.min(Config.MAX_ENEMY_SPEED , speed);
    }
    public static Point2D routePoint(Point2D entity, Point2D other, boolean hasAccel){
        double speed = MIN_ENEMY_SPEED;
        if (hasAccel) speed = accelSpeed(entity, other);
        speed = Math.max(speed, MIN_ENEMY_SPEED);
        Point2D vector = unitVector(other, entity);
        return weighedVector(vector, speed);

    }
    public static Point2D impactPoint(Point2D entity, Point2D other){
        double speed = deltaSpeedAway(entity, other);
        Point2D vector = unitVector(entity, other);
        return weighedVector(vector, speed);
    }
    public static Point2D inverse(Point2D point2D){
        return new Point2D.Double(-point2D.getX(), -point2D.getY());
    }
    public static double magnitude(Point2D point2D){
        return Math.sqrt(point2D.getX()*point2D.getX() + point2D.getY()*point2D.getY());
    }
    private static double deltaSpeedAway(Point2D point, Point2D anchor){
        double d1 = point.getX() - anchor.getX();
        double d2 = point.getY() - anchor.getY();
        double d = Math.sqrt(d1* d1 + d2*d2);
        double speed = Config.MAX_ENEMY_SPEED - d/50;
        return Math.max(0 , speed);
    }
    public static Point2D weighedVector(Point2D point, double scalar){
        return new Point2D.Double(point.getX()*scalar,point.getY()*scalar);
    }
    public static Point2D closestPointOnPolygon(Point2D point, ArrayList<Point2D> vertices){
        double minDistance=Double.MAX_VALUE;
        Point2D closest=null;
        for (int i=0;i<vertices.size();i++){
            Point2D temp=getClosestPointOnSegment(vertices.get(i),vertices.get((i+1)%vertices.size()),point);
            double distance=temp.distance(point);
            if (distance<minDistance) {
                minDistance=distance;
                closest=temp;
            }else if(distance==minDistance){
                assert closest != null;
                closest=new Point2D.Double((closest.getX()+temp.getX())/2 ,
                        (closest.getY()+temp.getY())/2);
            }
        }
        return closest;
    }
    private static Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u =((point.getX()-head1.getX())*(head2.getX()-head1.getX())+(point.getY()-head1.getY())*(head2.getY()-head1.getY()))/head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else return new Point2D.Double(head2.getX()* u + head1.getX() * (1.0 - u) + 0.5,head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
    }
//    public static Point2D addVectors(Point2D point1,Point2D point2){
//        return new Point2D.Double(point1.getX()+point2.getX(),point1.getY()+point2.getY());
//    }

}
