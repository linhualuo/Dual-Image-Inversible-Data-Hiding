package com.hualuo.filter;

import com.hualuo.function.AbstractBufferedImageOp;

import java.awt.image.BufferedImage;

/**
 * 双图片解密过滤器
 *
 * @author Joseph
 * @create 2018/3/17 19:48
 */
public class DecodeFilter extends AbstractBufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        return super.filter(src, dest);
    }

    /**
     * 从两张图片中提取隐藏的文字
     * @param dest1
     * @param dest2
     * @return
     */
    public String decode(BufferedImage dest1, BufferedImage dest2) {
        return "";
    }

    /**
     * 核心操作，像素提取
     * @param inPixels1
     * @param inpixels2
     * @return
     */
    private String decodePixels(int[] inPixels1, int[] inpixels2) {
        return null;
    }
}
