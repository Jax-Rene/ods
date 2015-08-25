package com.wskj.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by youjm on 2015/7/16.
 * 创建验证码工具类
 */
public class VerifyCodeUtil {

    //该方法主要作用是获得随机生成的颜色
    public static Color getRandColor(int s, int e) {
        Random random = new Random();
        if (s > 255) s = 255;
        if (e > 255) e = 255;
        int r, g, b;
        r = s + random.nextInt(e - s);
        g = s + random.nextInt(e - s);
        b = s + random.nextInt(e - s);
        return new Color(r, g, b);
    }

    public static void VerifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        //指定生成的响应图片,一定不能缺少这句话,否则错误.
        response.setContentType("image/jpeg");
        //指定生成验证码的宽度和高度
        int width = 86, height = 25;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //创建BufferedImage对象,其作用相当于一图片
        //创建Graphics对象,其作用相当于画笔
        Graphics g = image.getGraphics();
        //创建Grapchics2D对象
        Graphics2D g2d = (Graphics2D) g;
        Random random = new Random();
        //定义字体样式
        Font mfont = new Font("楷体", Font.BOLD, 16);
        g.setColor(getRandColor(200, 250));
        //绘制背景
        g.fillRect(0, 0, width, height);
        g.setFont(mfont);
        //设置字体
        g.setColor(getRandColor(180, 200));

        //绘制100条颜色和位置全部为随机产生的线条,该线条为2f
        for (int i = 0; i < 60; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int x1 = random.nextInt(6) + 1;
            int y1 = random.nextInt(12) + 1;
            BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL); //定制线条样式
            Line2D line = new Line2D.Double(x, y, x + x1, y + y1);
            g2d.setStroke(bs);
            g2d.draw(line);     //绘制直线
        }
        //输出由英文，数字，和中文随机组成的验证文字，具体的组合方式根据生成随机数确定。
        String sRand = "";
        String ctmp = "";
        int itmp = 0;
        //制定输出的验证码为四位
        for (int i = 0; i < 4; i++) {
            switch (random.nextInt(3)) {
                //生成A-Z的字母
                case 1:
                    itmp = random.nextInt(26) + 65;
                    ctmp = String.valueOf((char) itmp);
                    break;
                //数字
                case 2:
                    itmp = random.nextInt(10);
                    ctmp = String.valueOf(itmp);
                    break;
                default:
                    itmp = random.nextInt(10) + 48;
                    ctmp = String.valueOf((char) itmp);
                    break;
            }
            sRand += ctmp;
            Color color = new Color(20 + random.nextInt(110), 20 + random.nextInt(110), random.nextInt(110));
            g.setColor(color);
            //将生成的随机数进行随机缩放并旋转制定角度 PS.建议不要对文字进行缩放与旋转,因为这样图片可能不正常显示
            //将文字旋转制定角度
            Graphics2D g2d_word = (Graphics2D) g;
            AffineTransform trans = new AffineTransform();
            trans.rotate((45) * 3.14 / 180, 15 * i + 8, 7);
            //缩放文字
            float scaleSize = random.nextFloat() + 0.8f;
            if (scaleSize > 1f) scaleSize = 1f;
            trans.scale(scaleSize, scaleSize);
            g2d_word.setTransform(trans);
            g.drawString(ctmp, 15 * i + 18, 14);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("randCheckCode", sRand);
        //释放g所占用的系统资源
        g.dispose();
        //输出图片
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    private VerifyCodeUtil() {
    }
}

