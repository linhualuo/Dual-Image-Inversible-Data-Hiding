package com.hualuo.filter;

import com.hualuo.function.AbstractBufferedImageOp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public List<BufferedImage> hide(BufferedImage src, String message) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest1 = createCompatibleDestImage(src, null);
        BufferedImage dest2 = createCompatibleDestImage(src, null);

        int[] inpixels = new int[width * height];
        int[] outpixels1 = new int[width * height];
        int[] outpixels2 = new int[width * height];

        getRGB(src, 0, 0, width, height, inpixels);
        hidePixels(inpixels, outpixels1, outpixels2, message, width, height);
        setRGB(dest1, 0, 0, width, height, outpixels1);
        setRGB(dest2, 0, 0, width, height, outpixels2);

        List<BufferedImage> imageList = new ArrayList<>(2);
        imageList.add(dest1);
        imageList.add(dest2);

        return imageList;
    }

    /**
     * 核心函数，隐藏
     * @param inPixels
     * @param outPixels1
     * @param outPixels2
     * @param message
     * @param width
     * @param height
     */
    private void hidePixels(
            int[] inPixels,
            int[] outPixels1,
            int[] outPixels2,
            String message,
            int width,
            int height ) {
        int d = 0, k = 0;
        d = Integer.parseInt(message);
        k = getK(message);

        //如果k == 2^(k - 1)，加一个m(k + 1)，范围是0-1的随机数
        if (d == Math.pow(2, k - 1)) {
            Random random = new Random();
            d = d + random.nextInt(2);
        }

        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                if (isOdd(d)) {
                    outPixels1[index] = inPixels[index] - (int)Math.ceil(d / 4);
                    outPixels2[index] = outPixels1[index] + (int)Math.ceil(d / 2);
                } else {
                    outPixels1[index] = inPixels[index] + (int)Math.floor(d / 4);
                    outPixels2[index] = outPixels1[index] - (d / 2);
                }
            }
        }
    }

    /**
     * 通过给定数据返回数据的二进制位数
     * @param message
     * @return
     */
    private int getK(String message) {
        int d = Integer.parseInt(message);
        int k = 0;
        while (d != 0) {
            k++;
            d >>= 1;
        }
        return k;
    }

    /**
     * 判断奇偶
     * @param d
     * @return
     */
    private boolean isOdd(int d) {
        if(d % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }
}
