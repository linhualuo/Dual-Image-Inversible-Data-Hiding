package com.hualuo.filter;

import com.hualuo.function.AbstractBufferedImageOp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 将信息隐藏于图片中的过虑器
 *
 * @author Joseph
 * @create 2018/3/17 19:40
 */
public class HideFilter extends AbstractBufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        return super.filter(src, dest);
    }

    /**
     * 隐藏信息进图片，返回两张图片
     * @param src
     * @param message
     * @return
     */
    public ArrayList<BufferedImage> hide(BufferedImage src, String message) {
        return null;
    }

    /**
     * 核心函数，隐藏
     * @param inpixels
     * @param outpixels1
     * @param outpixels2
     * @param message
     */
    private void hidePixels(int[] inpixels, int[] outpixels1, int[] outpixels2, String message) {

    }
}
