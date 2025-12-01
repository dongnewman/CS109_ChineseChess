package com.Model.Help;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 显示帮助的图片。
 *
 * 使用示例:
 * new DoHelp();
 */

public class DoHelp {
    public DoHelp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showWindow();
            }
        });

    }

    private void showWindow() {
        JFrame helpFrame = new JFrame("帮助");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(1000, 600);
        helpFrame.setLocationRelativeTo(null);

        // 尝试从 classpath 加载名为 tutorial.png 的图片（常见放置位置：src/main/resources/tutorial.png）
        BufferedImage img = null;
        try {
            // 尝试几种常见的资源名（大小写不敏感的备选）
            String[] candidates = { "/tutorial.png", "/Tutorial.png", "/Tutorial1.png" };
            URL res = null;
            for (String c : candidates) {
                res = DoHelp.class.getResource(c);
                if (res != null)
                    break;
            }
            if (res != null) {
                img = ImageIO.read(res);
            } else {
                // 尝试若干常见文件位置（项目根、src/main/resources），同时尝试大小写变体
                String[] fsCandidates = { "tutorial.png", "Tutorial.png", "src/main/resources/tutorial.png",
                        "src/main/resources/Tutorial.png" };
                for (String p : fsCandidates) {
                    File f = new File(p);
                    if (f.exists()) {
                        img = ImageIO.read(f);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            img = null;
        }

        if (img != null) {
            // 如果图片宽度比窗口略宽，按窗口宽度缩放，这样只需要纵向滚动
            int frameContentWidth = helpFrame.getWidth() - 40; // 留白
            Image displayImg = img;
            if (img.getWidth() > frameContentWidth) {
                double scale = (double) frameContentWidth / img.getWidth();
                int newW = (int) (img.getWidth() * scale);
                int newH = (int) (img.getHeight() * scale);

                // 使用高质量缩放
                BufferedImage scaledImg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaledImg.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(img, 0, 0, newW, newH, null);
                g2d.dispose();
                displayImg = scaledImg;
            }

            JLabel picLabel = new JLabel(new ImageIcon(displayImg));
            picLabel.setBorder(new EmptyBorder(8, 8, 8, 8));

            // 放入 JScrollPane，使之可上下滚动；水平一般不需要
            JScrollPane scroll = new JScrollPane(picLabel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            helpFrame.getContentPane().add(scroll, BorderLayout.CENTER);
        } else {
            // 图片未找到，显示说明文本（可本地化）
            JTextArea ta = new JTextArea();
            ta.setEditable(false);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setText("未能找到 tutorial.png。请把教程图片放在项目根目录或 src/main/resources 下，文件名为 tutorial.png。\n\n" +
                    "如果你希望显示多张图片，可以把它们命名为 tutorial1.png, tutorial2.png 等，然后扩展此类以创建多张标签。\n");
            ta.setBorder(new EmptyBorder(12, 12, 12, 12));
            JScrollPane scroll = new JScrollPane(ta,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            helpFrame.getContentPane().add(scroll, BorderLayout.CENTER);
        }

        helpFrame.setVisible(true);
    }
}
