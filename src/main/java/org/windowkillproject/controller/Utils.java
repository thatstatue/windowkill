package org.windowkillproject.controller;

import org.windowkillproject.model.Transferable;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.MAX_ENEMY_SPEED;
import static org.windowkillproject.application.Config.MIN_ENEMY_SPEED;

public abstract class Utils {
    public static Point2D unitVector(Point2D goingPoint, Point2D anchor) {
        double dY = goingPoint.getY() - anchor.getY();
        double dX = goingPoint.getX() - anchor.getX();
        double magnitude = Math.sqrt(dX * dX + dY * dY);
        return new Point2D.Double(dX / magnitude, dY / magnitude);
    }

    public static Point2D unitVector(Point2D point) {
        return unitVector(point, new Point2D.Double(0, 0));
    }

    public static Point2D calculateForce(Point2D point, double theta) {
        int i = 1;
        if (theta < 0) i = -1;
        return new Point2D.Double(point.getX() * -1 * i, point.getY() * i);
    }

    public static int rotateI(Point2D unitPoint, Point2D unitForce) {
        int i = 1;
        unitPoint = unitVector(unitPoint);
        unitForce = unitVector(unitForce);
        double t = Math.atan2(unitForce.getY() + unitPoint.getY(), unitForce.getX() + unitPoint.getX());
        if (t < 0) i = -1;
        return i;
    }

    private static double accelSpeed(Point2D entityModel, Point2D other) {
        double d1 = entityModel.getX() - other.getX();
        double d2 = entityModel.getY() - other.getY();
        double d = Math.sqrt(d1 * d1 + d2 * d2);
        int speed = (int) (d / 25 + 0.75);
        return Math.min(MAX_ENEMY_SPEED, speed);
    }
    public static int getSign(int number){
        if (number>=0) return 1;
        return -1;

    }
    public static Point2D localRoutePoint(Point2D entity, Point2D other, boolean hasAccel) {
        double speed = MIN_ENEMY_SPEED;
        if (hasAccel) speed = accelSpeed(entity, other);
        speed = Math.max(speed, MIN_ENEMY_SPEED);
        Point2D vector = unitVector(other, entity);
        return weighedVector(vector, speed);

    }
    public static Point2D globalRoutePoint(Point2D entity, Point2D other,double speed) {
//        if (hasAccel) speed = accelSpeed(entity, other);
        speed = Math.max(speed, MIN_ENEMY_SPEED);
        Point2D vector = unitVector(other, entity);
        return weighedVector(vector, speed);

    }
    public static boolean isOccupied(int randLoc, int entityLoc, int radius) {
        return randLoc + 2 * radius > entityLoc
                && randLoc < entityLoc + 4 * radius;
    }
    public static Point2D globalRoutePoint(Point2D entity, Point2D other){
        return globalRoutePoint(entity, other,(MAX_ENEMY_SPEED+ MIN_ENEMY_SPEED)/2.0);
    }


    public static Point2D impactPoint(Point2D entity, Point2D other) {
        double speed = deltaSpeedAway(entity, other);
        Point2D vector = unitVector(entity, other);
        return weighedVector(vector, speed);
    }

    public static Point2D inverse(Point2D point2D) {
        return new Point2D.Double(-point2D.getX(), -point2D.getY());
    }

    public static double magnitude(Point2D point2D) {
        return Math.sqrt(point2D.getX() * point2D.getX() + point2D.getY() * point2D.getY());
    }
    public static double magnitude(Point2D point, Point2D anchor) {
        double dY = point.getY() - anchor.getY();
        double dX = point.getX() - anchor.getX();
        return Math.sqrt(dX * dX + dY * dY);
    }


    private static double deltaSpeedAway(Point2D point, Point2D anchor) {
        double d1 = point.getX() - anchor.getX();
        double d2 = point.getY() - anchor.getY();
        double d = Math.sqrt(d1 * d1 + d2 * d2);
        double speed = MAX_ENEMY_SPEED - d / 50;
        return Math.max(0, speed);
    }

    public static Point2D weighedVector(Point2D point, double scalar) {
        return new Point2D.Double(point.getX() * scalar, point.getY() * scalar);
    }

    public static Point2D closestPointOnPolygon(Point2D point, ArrayList<Point2D> vertices) {
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        for (int i = 0; i < vertices.size(); i++) {
            Point2D temp = getClosestPointOnSegment(vertices.get(i), vertices.get((i + 1) % vertices.size()), point);
            double distance = temp.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = temp;
            } else if (distance == minDistance) {
                assert closest != null;
                closest = new Point2D.Double((closest.getX() + temp.getX()) / 2,
                        (closest.getY() + temp.getY()) / 2);
            }
        }
        return closest;
    }

    private static Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u = ((point.getX() - head1.getX()) * (head2.getX() - head1.getX()) + (point.getY() - head1.getY()) * (head2.getY() - head1.getY())) / head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else
            return new Point2D.Double(head2.getX() * u + head1.getX() * (1.0 - u) + 0.5, head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
    }
    public static boolean isTransferableInBounds(Transferable transferable, Point2D center, int radius, boolean partly){
        int left = transferable.getX();
        int right = left + transferable.getWidth();
        int up = transferable.getY();
        int down = up+ transferable.getHeight();
        ArrayList<Point2D> edges = new ArrayList<>();
        edges.add(new Point2D.Double(left,up));
        edges.add(new Point2D.Double(right,up));
        edges.add(new Point2D.Double(left,down));
        edges.add(new Point2D.Double(right,down));

        for (Point2D edge: edges) {
            if (edge.distance(center)<radius){
                if (partly) return true;
            }else if (!partly) return false;
        }
        return !partly;
    }

    public static boolean isTransferableInBounds(Transferable transferable, Rectangle area, boolean partly){
        int left = transferable.getX();
        int right = left + transferable.getWidth();
        int up = transferable.getY();
        int down = up+ transferable.getHeight();
        ArrayList<Point2D> edges = new ArrayList<>();
        edges.add(new Point2D.Double(left,up));
        edges.add(new Point2D.Double(right,up));
        edges.add(new Point2D.Double(left,down));
        edges.add(new Point2D.Double(right,down));

        for (Point2D edge: edges) {
            if (isPointInBounds(edge, area)) {
                if (partly) return true;
            }else if (!partly) return false;
        }
        return !partly;
    }

    public static boolean isTransferableInBounds(Transferable transferable, Area area, boolean partly){
        int left = transferable.getX();
        int right = left + transferable.getWidth();
        int up = transferable.getY();
        int down = up+ transferable.getHeight();
        ArrayList<Point2D> edges = new ArrayList<>();
        edges.add(new Point2D.Double(left,up));
        edges.add(new Point2D.Double(right,up));
        edges.add(new Point2D.Double(left,down));
        edges.add(new Point2D.Double(right,down));

        for (Point2D edge: edges) {
            if (area.contains(edge.getX() , edge.getY())) {
                if (partly) return true;
            }else if (!partly) return false;
        }
        return !partly;
    }

    private static boolean isPointInBounds(Point2D point2D, Rectangle area){
        double left = area.getX();
        double right = left + area.getWidth();
        double up = area.getY();
        double down = up+ area.getHeight();
        boolean isPointXInBounds = point2D.getX() > left &&
                point2D.getX()< right;
        boolean isPointYInBounds = point2D.getY() > up &&
                point2D.getY()< down;
        return isPointXInBounds && isPointYInBounds;
    }

}
