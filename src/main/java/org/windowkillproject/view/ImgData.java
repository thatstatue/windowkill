package org.windowkillproject.view;

import org.windowkillproject.application.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgData {
    private static ImgData data;

    protected BufferedImage epsilon, trigorath, banish, empower, heal, ares, aceso, proteus;


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
            banish.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Empower.png";
            File fileBackground = new File(pathBackground);
            empower = ImageIO.read(fileBackground);
            empower.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Heal.png";
            File fileBackground = new File(pathBackground);
            heal = ImageIO.read(fileBackground);
            heal.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Ares.png";
            File fileBackground = new File(pathBackground);
            ares = ImageIO.read(fileBackground);
            ares.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Aceso.png";
            File fileBackground = new File(pathBackground);
            aceso = ImageIO.read(fileBackground);
            aceso.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = "Proteus.png";
            File fileBackground = new File(pathBackground);
            proteus = ImageIO.read(fileBackground);
            proteus.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //singleton
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

    public BufferedImage getAres() {
        return ares;
    }

    public BufferedImage getAceso() {
        return aceso;
    }

    public BufferedImage getProteus() {
        return proteus;
    }

    public BufferedImage getEmpower() {
        return empower;
    }


}
