package com.hualuo.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Swing UI 界面实现
 *
 * @author Joseph
 */
public class MainUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final String IMAGE_CMD = "选择图像";

    public static final String PROCESS_CMD = "隐藏";

    private JButton imageBtn;

    private JButton processBtn;

    private ImagePanel imagePanel;

    // image
    private BufferedImage srcImage;

    public MainUI() throws HeadlessException {
        setTitle("双图片信息隐藏1.0");
        imageBtn = new JButton(IMAGE_CMD);
        processBtn = new JButton(PROCESS_CMD);

        //buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(imageBtn);
        btnPanel.add(processBtn);

        //filters
        imagePanel = new ImagePanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        //setup listener
        setUpActionListener();
    }

    private void setUpActionListener() {
        imageBtn.addActionListener(this);
        processBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (SwingUtilities.isEventDispatchThread()) {
            System.out.println("Event Dispatch Thread!!");
        }

        if (srcImage == null) {
            JOptionPane.showMessageDialog(this, "请先选择图像源文件");
            try {
                JFileChooser chooser = new JFileChooser();
                setFileTypeFilter(chooser);
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    srcImage = ImageIO.read(file);
                    imagePanel.setSourceImage(srcImage);
                    imagePanel.repaint();
                }
            } catch (IOException el) {
                el.printStackTrace();
            }
            return;
        }

        if (IMAGE_CMD.equals(e.getActionCommand())) {
            try {
                JFileChooser chooser = new JFileChooser();
                setFileTypeFilter(chooser);
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    srcImage = ImageIO.read(file);
                    imagePanel.setSourceImage(srcImage);
                    imagePanel.repaint();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            imagePanel.repaint();
        }

        else if (PROCESS_CMD.equals(e.getActionCommand())) {
            imagePanel.process();
            imagePanel.repaint();
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
        setPreferredSize(new Dimension(800, 600));
        pack();
        setVisible(true);
    }
}
