package com.shamengxin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 初始化用户头像的工具类
 */
public class ImageUtils {

    private static List<String> photos;

    static {
        photos = new ArrayList<>();
        photos.add("https://s1.ax1x.com/2023/08/26/pPNEbb4.jpg");
        photos.add("https://s1.ax1x.com/2023/08/26/pPNEbb4.jpg");
        photos.add("https://s1.ax1x.com/2023/08/26/pPNEbb4.jpg");
        photos.add("https://s1.ax1x.com/2023/08/26/pPNEbb4.jpg");
    }

    public static String getPhone(){
        int i = new Random().nextInt(photos.size());
        return photos.get(i);
    }
}
