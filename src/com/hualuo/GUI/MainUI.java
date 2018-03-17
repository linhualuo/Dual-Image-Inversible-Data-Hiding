package com.hualuo.GUI;

import com.hualuo.filter.DecodeFilter;
import com.hualuo.filter.HideFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing UI 界面实现
 *
 * @author Joseph
 */
public class MainUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final String IMAGE_CMD = "选择图像";

    public static final String HIDE_CMD = "隐藏";

    public static final String EXTRACT_CMD = "提取";

    private JButton imageBtn;

    private JButton hideBtn;

    private JButton extractBtn;

    private ImagePanel imagePanel;

    private ImagePanel destImagePanel1;

    private ImagePanel destImagePanel2;

    private int imageWidth;

    private int imageHeight;

    /**
     * 用户输入用于隐藏的文本
     */
    private JTextField inputText;

    /**
     * 提取出来的隐藏的文本
     */
    private JTextField outputText;

    // image
    private BufferedImage srcImage;

    public MainUI() throws HeadlessException {
        setTitle("双图片信息隐藏1.0");
        imageBtn = new JButton(IMAGE_CMD);
        hideBtn = new JButton(HIDE_CMD);
        extractBtn = new JButton(EXTRACT_CMD);
        inputText = new JTextField("input", 10);
        outputText = new JTextField("output",10);

        //buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.add(imageBtn);
        btnPanel.add(hideBtn);
        btnPanel.add(inputText);
        btnPanel.add(extractBtn);
        btnPanel.add(outputText);

        //filters
        imagePanel = new ImagePanel(new Dimension(400, 400));
        destImagePanel1 = new ImagePanel(new Dimension(400, 400));
        destImagePanel2 = new ImagePanel(new Dimension(400, 400));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(destImagePanel1, BorderLayout.WEST);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        getContentPane().add(destImagePanel2, BorderLayout.EAST);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        //setup listener
        setUpActionListener();
    }

    private void setUpActionListener() {
        imageBtn.addActionListener(this);
        hideBtn.addActionListener(this);
        //提取的暂未设置
        //something here...
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //隐藏信息进入图片
        if (IMAGE_CMD.equals(e.getActionCommand())) {
            if (srcImage == null) {
                JOptionPane.showMessageDialog(this, "请先选择图像源文件");
            }
            try {
                JFileChooser chooser = new JFileChooser();
                setFileTypeFilter(chooser);
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    srcImage = ImageIO.read(file);
                    imagePanel.setSourceImage(srcImage);
                    imagePanel.repaint();
                    String inputMessage = inputText.getText();
                    HideFilter hideFilter = new HideFilter();
                    List<BufferedImage> imageList = hideFilter.hide(imagePanel.getSourceImage(), inputMessage);
                    destImagePanel1.setSourceImage(imageList.get(0));
                    destImagePanel1.repaint();
                    destImagePanel2.setSourceImage(imageList.get(1));
                    destImagePanel2.repaint();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            imagePanel.repaint();
            //tmp
            destImagePanel1.repaint();
            destImagePanel2.repaint();
        }

        //从两张图片中提取信息
        else if (HIDE_CMD.equals(e.getActionCommand())) {
            if (destImagePanel1 == null || destImagePanel2 == null) {
                JOptionPane.showMessageDialog(this, "请先选择图片并进行隐藏操作");
            } else {
                DecodeFilter decodeFilter = new DecodeFilter();
                String message = decodeFilter.decode(destImagePanel1.getSourceImage(), destImagePanel2.getSourceImage());
                outputText.setText(message);
                imagePanel.repaint();
                destImagePanel1.repaint();
                destImagePanel2.repaint();
            }
        }
    }

    public void setFileTypeFilter(JFileChooser chooser) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG Images", "jpg", "png"
        );
        chooser.setFileFilter(filter);
    }

    public void openView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 500);
        setVisible(true);
    }
}
