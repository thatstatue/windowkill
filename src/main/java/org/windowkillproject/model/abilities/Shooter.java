package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Epsilon;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class Shooter extends Ability implements MouseListener {
    private Epsilon parent;
    protected Shooter(int x, int y, Epsilon parent) {
        super(x, y);
        this.parent = parent;
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    public void setParent(Epsilon parent) {
        this.parent = parent;
    }

    private void shoot(){
        Bullet bullet = new Bullet(getParent().getX(), getParent().getY());
        bullet.setShoot(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        shoot();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
