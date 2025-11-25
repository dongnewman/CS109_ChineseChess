package com.GUI.GameObjects;

import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;

public class Hinatsuru {
    private String file_path = "src/main/resources/Hinatsuru/image.png";
    private JFrame parentFrame;

    public Hinatsuru(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JLabel hinatsuruLabel = new JLabel();
        try {
            File f = new File(file_path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                hinatsuruLabel.setIcon(icon);
                // 使用图标真实尺寸设置标签大小和位置
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                // 放在右下角或指定位置（这里以 720,100 为例）
                hinatsuruLabel.setBounds(720, 100, w, h);
                // 将标签添加到 frame 的 layered pane，避免改变 contentPane 的布局
                JLayeredPane lp = parentFrame.getLayeredPane();
                lp.add(hinatsuruLabel, JLayeredPane.PALETTE_LAYER);
            } else {
                System.out.println("Hinatsuru image file not found: " + f.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Failed to load Hinatsuru image: " + e.getMessage());
        }
    }
}
