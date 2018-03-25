package com.hualuo.filter;

import com.hualuo.util.AbstractBufferedImageOp;
import com.hualuo.util.StringUtils;

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
    /**
     * 最后一个隐藏像素的索引
     */
    public static int END_INDEX;

    /**
     * 最后一个隐藏像素的填充量（填0），默认为0
     */
    public static int END_INDEX_PADOFF;

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
    public List<BufferedImage> hide(BufferedImage src, String message, String kText) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest1 = createCompatibleDestImage(src, null);
        BufferedImage dest2 = createCompatibleDestImage(src, null);

        int[] inpixels = new int[width * height];
        int[] outpixels1 = new int[width * height];
        int[] outpixels2 = new int[width * height];

        getRGB(src, 0, 0, width, height, inpixels);
        hidePixels(inpixels, outpixels1, outpixels2, message, width, height, kText);
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
            int height,
            String kText) {
        int d = 0;
        int k = Integer.valueOf(kText);

        String binaryMessage = StringUtils.getBinaryString(message);
        int lastIndex = binaryMessage.length() - 1;//字符串的最后一个字符的索引
        int bMIndex = 0;//Binary Message Index,读取字符串索引

        int cmp;

        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;

                //分割字符串操作
                cmp = lastIndex - bMIndex;
                if (cmp >= k) {
                    d = Integer.parseInt(binaryMessage.substring(bMIndex, bMIndex + k), 2);
                    if (d == Math.pow(2, k) - 1) {
                        //读取多一位, d = d + m(k + 1)
                        d += (binaryMessage.charAt(bMIndex + k) - '0');
                        bMIndex++;
                    }
                    bMIndex += k;
                    //经过这步bMIndex可能刚好把串的最后一位读取了
                    if (bMIndex > lastIndex) {
                        END_INDEX_PADOFF = 0;
                        END_INDEX = index;
                    }
                } else if (cmp >= 0 && cmp < k){//隐藏信息的最后一个像素
                    StringBuilder tmpStr = new StringBuilder(binaryMessage.substring(bMIndex));
                    if (tmpStr.length() < k) {//最后的字符串长度小于k
                        //填充0
                        int count = k - tmpStr.length();
                        END_INDEX_PADOFF = count;
                        while (count-- > 0) {
                            tmpStr.append("0");
                        }
                    }
                    d = Integer.parseInt(tmpStr.toString(), 2);
                    if (d == Math.pow(2, k) - 1) {
                        //补个0凑数，如果经过填0操作，不会进这个分支
                        d += 0;
                        END_INDEX_PADOFF = 1;
                    }
                    END_INDEX = index;
                    bMIndex += k;
                } else {//信息已经隐藏完的情况
                    outPixels1[index] = inPixels[index];
                    outPixels2[index] = inPixels[index];
                    bMIndex += k;
                    continue;
                }

                int ta = 0, tr = 0, tg = 0, tb = 0;
                int targetGray1, targetGray2;
                //若为灰度图片，rgb的值都一样，a值为255
                ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;

                if (isOdd(d)) {
//                    outPixels1[index] = inPixels[index] - (int)Math.ceil(d / 4);
//                    outPixels2[index] = outPixels1[index] + (int)Math.ceil(d / 2);
                    targetGray1 = tr - (int)Math.ceil(d / 4.0);
                    targetGray2 = targetGray1 + (int)Math.ceil(d / 2.0);
                    outPixels1[index] = (ta << 24) | clamp(targetGray1) << 16 | clamp(targetGray1) << 8 | clamp(targetGray1);
                    outPixels2[index] = (ta << 24) | clamp(targetGray2) << 16 | clamp(targetGray2) << 8 | clamp(targetGray2);
                } else {
//                    outPixels1[index] = inPixels[index] + (int)Math.floor(d / 4);
//                    outPixels2[index] = outPixels1[index] - (d / 2);
                    targetGray1 = tr + (int)Math.floor(d / 4.0);
                    targetGray2 = targetGray1 - (d / 2);
                    outPixels1[index] = (ta << 24) | clamp(targetGray1) << 16 | clamp(targetGray1) << 8 | clamp(targetGray1);
                    outPixels2[index] = (ta << 24) | clamp(targetGray2) << 16 | clamp(targetGray2) << 8 | clamp(targetGray2);
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
