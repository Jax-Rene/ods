package com.wskj.util;

/**
 * Created by johnny on 15/8/8.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;

/**
 * 图片处理工具类
 */
public class ImageUtil {

    /** */
    /**
     * 图像切割（改）
     *
     * @param srcImageFile 源图像
     * @param x            目标切片起点x坐标
     * @param y            目标切片起点y坐标
     * @param destWidth    目标切片宽度
     * @param destHeight   目标切片高度
     */
    public static void abscut(File srcImageFile, int x, int y, int destWidth,
                              int destHeight) {
        try {
            System.out.println("开始执行");
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read(srcImageFile);
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);

               /* ****************************************
                *  判断原图的宽高和DIV宽高的大小
                *  若srcWidth>DIV宽，则【w=destWidth*srcWidth/420】
                *  若srcWidth<DIV宽，则【w=destWidth】
                *  若srcHeight>DIVœ高，则【h=destHeight*srcHeight/420】
                *  若srcHeight<DIV高，则【h=destHeight】
                *************************************** */
                int x1 = x * srcWidth / 400;
                int y1 = y * srcWidth / 400;
                int w = destWidth * srcWidth / 400;
                int h = destHeight * srcWidth / 400;

                // 改进的想法:是否可用多线程加快切割速度
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                cropFilter = new CropImageFilter(x1, y1, w, h);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(w, h,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", srcImageFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void zoomImage(File srcImageFile) throws Exception {

        BufferedImage result = null;
        BufferedImage bi = ImageIO.read(srcImageFile);
        /* 原始图像的宽度和高度 */
        int width = bi.getWidth();
        int height = bi.getHeight();
        int zoomRate = width / 400;
        int destWidth = width / zoomRate;
        int destHeight = height / zoomRate;

        result = new BufferedImage(destWidth, destHeight,
                BufferedImage.TYPE_INT_RGB);

        result.getGraphics().drawImage(
                bi.getScaledInstance(destWidth, destHeight,
                        java.awt.Image.SCALE_SMOOTH), 0, 0, null);

        ImageIO.write(result, "JPEG", srcImageFile);
    }

    private ImageUtil() {
    }
}