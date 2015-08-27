package com.wskj.util;

import java.io.File;

/**
 * Created by zhuangjy on 2015/8/27.
 */
public class ImageRunnable implements Runnable{
    private File file;
    private Integer targetX;
    private Integer targetY;
    private Integer targetW;
    private Integer targetH;

    public ImageRunnable(File file, Integer targetX, Integer targetY, Integer targetW, Integer targetH) {
        this.file = file;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetW = targetW;
        this.targetH = targetH;
    }

    public void run(){
        ImageUtil.abscut(file, targetX, targetY, targetW, targetH);
    }
}

