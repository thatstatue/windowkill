package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.model.entities.Vertex;
import org.windowkillproject.view.ImgData;

import java.awt.*;

public class Trigorath extends Enemy{
    private final double RAD3_ON_2 = 0.866;
    public Trigorath(int x, int y) {
        super(x, y);
        setRadius(20);
        setImg(ImgData.getData().getTrigorath());
        setHp(15);
        setAttackHp(10);

        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
        getVertices().add(new Vertex(getXO()- (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
        getVertices().add(new Vertex(getXO()+ (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        for (int i = 0 ; i < 3; i++){
            xpoints[i] = getVertices().get(i).getX();
            ypoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xpoints , ypoints, 3));
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(Color.yellow);
        g2D.setStroke(new BasicStroke(2));
        g2D.drawPolygon(getPolygon());
        g2D.setColor(new Color(0,0,0,0));
        for (Vertex vertex : getVertices()){
            vertex.paint(g);
        }

    }

}
