package org.windowkillproject.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgData {
    private static ImgData data;

    protected BufferedImage epsilon;


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
    }

    //singleton practice
    public static ImgData getData() {
        if (data == null) {
            data = new ImgData();
        }
        return data;
    }

    public BufferedImage getEpsilon() {
        return epsilon;
    }

}
