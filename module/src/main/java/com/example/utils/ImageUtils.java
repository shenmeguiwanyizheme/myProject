package com.example.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    //默认用户头像
    private static String defaultMaleAvatar = "/photo/avatar.png";
    private static String defaultFeMaleAvatar = "/photo/avatar.png";

    public static String getDefaultMaleAvatar() {
        return defaultMaleAvatar;
    }

    public static String getDefaultFeMaleAvatar() {
        return defaultFeMaleAvatar;
    }

    public static int[] getImageWidthAndHeight(String imageUrl) {
        int[] wh = new int[2];
        try {
            URL url = new URL(imageUrl);
            BufferedImage brImage = ImageIO.read(url);
            wh[0] = brImage.getWidth();
            wh[1] = brImage.getHeight();
            return wh;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
