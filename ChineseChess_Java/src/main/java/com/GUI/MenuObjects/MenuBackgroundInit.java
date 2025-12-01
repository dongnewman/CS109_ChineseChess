package com.GUI.MenuObjects;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

public class MenuBackgroundInit {
    public static void initMenuBackground(JFrame frame) {
        final String fsPath = "src\\main\\resources\\Menu.png";
        final String resPath = "/Menu.png";
        BufferedImage image = null;
        // 优先类路径
        try (java.io.InputStream in = MenuBackgroundInit.class.getResourceAsStream(resPath)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            // ignore, try filesystem next
        }
        if (image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    image = ImageIO.read(f);
                }
            } catch (IOException e) {
                // ignore
            }
        }
        if (image != null) {
            JLabel background = new JLabel(new javax.swing.ImageIcon(image));
            background.setBounds(0, 0, image.getWidth(), image.getHeight());
            frame.setContentPane(background);
            frame.setLayout(null); // 让背景图不被布局管理器影响
            frame.setSize(image.getWidth(), image.getHeight());
        }
    }

}
