package com.GUI.GameObjects;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

import com.GUI.MenuObjects.MenuBackgroundInit;

public class SideIcon {
    private final String black_file_path = "src/main/resources/BlackSide.png";
    private final String red_file_path = "src/main/resources/RedSide.png";
    private final javax.swing.ImageIcon blackIcon;
    private final javax.swing.ImageIcon redIcon;
    JFrame parentFrame;
    private final JLabel sidelabel = new JLabel();

    public SideIcon(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        blackIcon = loadIcon(black_file_path);
        redIcon = loadIcon(red_file_path);
        parentFrame.getLayeredPane().add(sidelabel, javax.swing.JLayeredPane.POPUP_LAYER);
        sidelabel.setPreferredSize(new java.awt.Dimension(100, 86));
        sidelabel.setSize(60, 52);
        // 默认初始化为红方
        setRedSideIcon();
        // 默认放在窗口左上角
        sidelabel.setLocation(720, 375);
    }

    private javax.swing.ImageIcon loadIcon(String path) {
        java.awt.Image img = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                img = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore
        }
        if (img != null) {
            return new javax.swing.ImageIcon(img);
        } else {
            return null;
        }
    }

    public void setBlackSideIcon() {
        if (blackIcon != null) {
            sidelabel.setIcon(blackIcon);
        }
    }

    public void setRedSideIcon() {
        if (redIcon != null) {
            sidelabel.setIcon(redIcon);
        }
    }
}
