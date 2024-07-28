package org.windowkillproject.client.view;

import org.windowkillproject.server.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgData {
    private static ImgData data;
    private final String icons_dir = "icons/";

    protected BufferedImage epsilon, trigorath, barricados,
            banish, empower, heal,
            ares, aceso, proteus,
            smileyHead, rightHand, leftHand, punchFist,
            bg;


    private ImgData() {
        addImages();
    }

    private void addImages() {
        try {
            String pathBackground = icons_dir+"Epsilon.png";
            File fileBackground = new File(pathBackground);
            epsilon = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"BG.png";
            File fileBackground = new File(pathBackground);
            bg = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Trigorath.png";
            File fileBackground = new File(pathBackground);
            trigorath = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Barricados.png";
            File fileBackground = new File(pathBackground);
            barricados = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Banish.png";
            File fileBackground = new File(pathBackground);
            banish = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Empower.png";
            File fileBackground = new File(pathBackground);
            empower= ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Heal.png";
            File fileBackground = new File(pathBackground);
            heal= ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String pathBackground = icons_dir+"Proteus.png";
            File fileBackground = new File(pathBackground);
            proteus = ImageIO.read(fileBackground);
            proteus.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"Ares.png";
            File fileBackground = new File(pathBackground);
            ares = ImageIO.read(fileBackground);
            ares.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"Aceso.png";
            File fileBackground = new File(pathBackground);
            aceso = ImageIO.read(fileBackground);
            aceso.getScaledInstance(Config.OPTION_WIDTH, Config.OPTION_IMG_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"SmileyHead.png";
            File fileBackground = new File(pathBackground);
            smileyHead = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"RightHand.png";
            File fileBackground = new File(pathBackground);
            rightHand = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"LeftHand.png";
            File fileBackground = new File(pathBackground);
            leftHand = ImageIO.read(fileBackground);

        } catch (IOException e) {
            e.printStackTrace();
        }try {
            String pathBackground = icons_dir+"PunchFist.png";
            File fileBackground = new File(pathBackground);
            punchFist = ImageIO.read(fileBackground);

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
    public BufferedImage getBarricados() {
        return barricados;
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
    public BufferedImage getBg() {
        return bg;
    }


    public BufferedImage getProteus() {
        return proteus;
    }

    public BufferedImage getEmpower() {
        return empower;
    }


    public BufferedImage getSmileyHead() {
        return smileyHead;
    }

    public BufferedImage getRightHand() {
        return rightHand;
    }

    public BufferedImage getLeftHand() {
        return leftHand;
    }

    public BufferedImage getPunchFist() {
        return punchFist;
    }
}
