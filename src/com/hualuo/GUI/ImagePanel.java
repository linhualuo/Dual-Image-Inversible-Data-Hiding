package com.hualuo.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 支持BufferedImage对象显示的自定义JPanel类
 *
 * @author Joseph
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private BufferedImage sourceImage;

    private BufferedImage destImage;

    private int imageWidth;

    private int imageHeight;

    public ImagePanel() {
    }

    public ImagePanel(Dimension dimension) {
        setPreferredSize(dimension);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (sourceImage != null) {
            g2d.drawImage(sourceImage, 0, 0,
                    sourceImage.getWidth(),
                    sourceImage.getHeight(), null);
            if (destImage != null) {
                g2d.drawImage(destImage, 0, 0,
                        destImage.getWidth(),
                        destImage.getHeight(), null);
            }
            imageWidth = sourceImage.getWidth();
            imageHeight = sourceImage.getHeight();
        }
    }

    public void process() {
        //
    }

    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public BufferedImage getDestImage() {
        return destImage;
    }

    public void setDestImage(BufferedImage destImage) {
        this.destImage = destImage;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
