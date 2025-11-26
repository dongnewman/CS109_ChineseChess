package com.GUI.GameObjects;

import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;

public class Hinatsuru {
    private JFrame parentFrame;

    public Hinatsuru(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JLabel hinatsuruLabel = new JLabel();

        final String fsPath = "src\\main\\resources\\Hinatsuru\\image.png"; // 开发时文件系统路径
        final String resPath = "/Hinatsuru/image.png"; // 类路径资源路径（jar 内）

        ImageIcon icon = null;
        // 1) 优先尝试类路径（适用于打包到 jar 后）
        java.net.URL url = getClass().getResource(resPath);
        if (url != null) {
            icon = new ImageIcon(url);
        } else {
            // 2) 回退到源码树下的文件系统路径（便于开发时在 IDE 直接运行）
            File f = new File(fsPath);
            if (f.exists()) {
                icon = new ImageIcon(f.getAbsolutePath());
            }
        }

        if (icon != null) {
            hinatsuruLabel.setIcon(icon);
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            hinatsuruLabel.setBounds(720, 100, w, h);
            JLayeredPane lp = parentFrame.getLayeredPane();
            lp.add(hinatsuruLabel, JLayeredPane.PALETTE_LAYER);
        } else {
            System.out.println("Hinatsuru image not found on classpath or at: " + fsPath);
        }
    }
}
