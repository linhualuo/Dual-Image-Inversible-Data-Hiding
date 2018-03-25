package com.hualuo.filter;

import com.hualuo.util.AbstractBufferedImageOp;

import java.awt.image.BufferedImage;

/**
 * 双图片解密过滤器
 *
 * @author Joseph
 * @create 2018/3/17 19:48
 */
public class DecodeFilter extends AbstractBufferedImageOp {

    private BufferedImage src;

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
        int width = dest1.getWidth();
        int height = dest1.getHeight();
        BufferedImage tmp = createCompatibleDestImage(dest1, null);

        int[] inPixels1 = new int[width * height];
        int[] inPixels2 = new int[width * height];
        int[] outPixels = new int[width * height];
        getRGB(dest1, 0, 0, width, height, inPixels1);
        getRGB(dest2, 0, 0, width, height, inPixels2);
        String message = decodePixels(outPixels, inPixels1, inPixels2, width, height);
        setRGB(tmp, 0, 0, width, height, outPixels);
        this.src = tmp;
        return message;
    }

    /**
     * 核心操作，像素提取
     * @param inPixels1
     * @param inPixels2
     * @return
     */
    private String decodePixels(int[] outPixels, int[] inPixels1, int[] inPixels2, int width, int height) {
        int k = 0;
        int d = 0;
        int midRange = 0;
        k = restoreK(inPixels1, inPixels2, width, height);
        midRange = (int) Math.pow(2, k - 2);
        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                if (inPixels1[index] == inPixels2[index] && isInRange(inPixels1[index], midRange)) {
                    outPixels[index] = inPixels1[index];
                } else {
                    outPixels[index] = (int) Math.ceil((inPixels1[index] + inPixels2[index]) / 2);
                }
            }
        }

        String res = "";
        //d赋值未解决...
        if (d < (int) Math.pow(2, k) - 1) {
            res =  String.valueOf(d);
        } else if (d == (int) Math.pow(2, k) - 1) {
            res =  String.valueOf(d * 2);
        } else if (d == (int) Math.pow(2, k)) {
            res =  String.valueOf(d * 2 + 1);
        }
        return res;
    }

    public BufferedImage getSrc() {
        return src;
    }

    public void setSrc(BufferedImage src) {
        this.src = src;
    }

    /**
     * 恢复k，算法是Max(|x' - x''|)/2 + 1
     * @param inPixels1
     * @param inPixels2
     * @return
     */
    private int restoreK(int[] inPixels1, int[] inPixels2, int width, int height) {
        int k = 0;
        int cmp = 0;
        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                cmp = Math.abs(inPixels1[index] - inPixels2[index]);
                if (cmp > k) {
                    k = cmp;
                }
            }
        }
        return k / 2 + 1;
    }

    /**
     * 判断某个像素是否在某范围
     * @param pixel
     * @param range
     * @return
     */
    private boolean isInRange(int pixel, int range) {
        if (pixel >= 0 && pixel < range) {
            return true;
        } else if (pixel > range && pixel <= 255) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sgn函数，如果参数大于等于0返回1，其余返回-1
     * @param num
     * @return
     */
    private int Sgn(int num) {
        if (num >= 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
