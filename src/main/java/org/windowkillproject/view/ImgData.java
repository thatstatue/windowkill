package org.windowkillproject.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgData {
    private static ImgData data;

    protected BufferedImage epsilon, trigorath, banish, empower, heal;


    private ImgData() {
        addImages();
    }

    private void addImages() {
        try {
            String pathBackground = "Epsilon.png";
            File fileBackground = new File(pathBackground);
            epsilon = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Trigorath.png";
            File fileBackground = new File(pathBackground);
            trigorath = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Banish.png";
            File fileBackground = new File(pathBackground);
            banish = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Empower.png";
            File fileBackground = new File(pathBackground);
            empower= ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Heal.png";
            File fileBackground = new File(pathBackground);
            heal= ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo add bullet
    }

    //singleton practice
    public static ImgData getData() {
        if (data == null) {
            data = new ImgData();
        }
        return data;
    }

    public BufferedImage getTrigorath() {
        return trigorath;
    }

    public BufferedImage getEpsilon() {
        return epsilon;
    }
    public BufferedImage getBanish() {
        return banish;
    }
    public BufferedImage getHeal() {
        return heal;
    }

    public BufferedImage getEmpower() {
        return empower;
    }



}
