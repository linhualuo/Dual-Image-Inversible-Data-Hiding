package com.hualuo.filter;

import com.hualuo.util.AbstractBufferedImageOp;
import com.hualuo.util.StringUtils;

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
        System.out.println("decode");
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
        System.out.println("像素提取");
        int k = 0;
        int d = 0;
        StringBuilder res = new StringBuilder();
        int leftRange = 0;
        k = retK(inPixels1, inPixels2);
        System.out.println("The value of k is: " + k);
        leftRange = (int) Math.pow(2, k - 2);
        int index = 0;
        int endIndex = HideFilter.END_INDEX;
        int endIndexPadoff = HideFilter.END_INDEX_PADOFF;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                if (inPixels1[index] == inPixels2[index] && isInRange(inPixels1[index], leftRange)) {
                    outPixels[index] = inPixels1[index];
                    continue;
                }
                if (index > endIndex) {
                    outPixels[index] = inPixels1[index];
                    continue;
                }
                else {
                    int ta = 0, tGray1 = 0, tGray2 = 0, targetGray = 0;
                    //若为灰度图片，rgb的值都一样，a值为255
                    ta = (inPixels1[index] >> 24) & 0xff;
                    tGray1 = (inPixels1[index] >> 16) & 0xff;
                    tGray2 = (inPixels2[index] >> 16) & 0xff;
                    targetGray = (int) Math.ceil((tGray1 + tGray2) / 2.0);
                    outPixels[index] = (ta << 24) | (clamp(targetGray) << 16) | clamp(targetGray) << 8 | clamp(targetGray);
                    //恢复每个隐藏像素的隐藏信息
                    d = (int)(2 * Math.abs(tGray1 - tGray2) +
                            0.5 * SgnFunction(tGray1 - tGray2) - 0.5);

                    //最后一个有隐藏数据的像素且有填充的情况
                    if (index == endIndex && endIndexPadoff > 0) {
                        res.append(StringUtils.getBinaryByK(d, k));
                        if (d != Math.pow(2, k) - 1) {
                            int deleteIndex = res.length() - endIndexPadoff;
                            int endDelIndex = res.length();
                            res.delete(deleteIndex, endDelIndex);
                        }
                        continue;
                    }

                    if (d == Math.pow(2, k) - 1) {
                        res.append(StringUtils.getBinaryByK(d, k));
                        res.append("0");//补0
                    } else if (d == Math.pow(2, k)) {
                        d--;
                        res.append(StringUtils.getBinaryByK(d, k));
                        res.append("1");//补1
                    } else {//其余普通情况
                        res.append(StringUtils.getBinaryByK(d, k));
                    }


                }
            }
        }
        return StringUtils.binaryToString(res.toString());
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
                int tr1 = 0, tr2 = 0;
                //各自取出灰度值
                tr1 = (inPixels1[index] >> 16) & 0xff;
                tr2 = (inPixels2[index] >> 16) & 0xff;
                cmp = Math.abs(tr1 - tr2);
                if (cmp > k) {
                    k = cmp;
                }
            }
        }
//        return (int)Math.ceil(k / 2.0) + 1;
        return (int)Math.ceil(Math.log(k) / Math.log(2)) + 1;
    }

    /**
     * 判断某个像素是否在某范围[0, range)或者(255 - range, 255]
     * @param pixel
     * @param range
     * @return
     */
    private boolean isInRange(int pixel, int range) {
        if (pixel >= 0 && pixel < range) {
            return true;
        } else if (pixel > (255 - range) && pixel <= 255) {
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
    private int SgnFunction(int num) {
        if (num >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    private int retK(int[] inPixels1, int[] inPixels2) {
        int len = inPixels1.length;
        int tb1 = 0, tb2 = 0;
        tb1 = inPixels1[len - 1] & 0xff;
        tb2 = inPixels2[len - 1] & 0xff;
        return Math.abs(tb1 - tb2);
    }
}
