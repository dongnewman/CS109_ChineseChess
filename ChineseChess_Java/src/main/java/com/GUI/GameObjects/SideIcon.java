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
    private final JLabel sidelabelRed = new JLabel();
    private final JLabel sidelabelBlack = new JLabel();

    public SideIcon(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        blackIcon = loadIcon(black_file_path);
        redIcon = loadIcon(red_file_path);

        // 将两个 label 都添加到 layered pane，不同的可见性控制显示哪一方箭头
        parentFrame.getLayeredPane().add(sidelabelRed, javax.swing.JLayeredPane.POPUP_LAYER);
        parentFrame.getLayeredPane().add(sidelabelBlack, javax.swing.JLayeredPane.POPUP_LAYER);

        // 使用相同尺寸和位置
        sidelabelRed.setPreferredSize(new java.awt.Dimension(100, 86));
        sidelabelRed.setSize(60, 52);
        sidelabelBlack.setPreferredSize(new java.awt.Dimension(100, 86));
        sidelabelBlack.setSize(60, 52);

        // 默认初始化为红方 visible，黑方隐藏
        sidelabelRed.setLocation(720, 375);
        sidelabelBlack.setLocation(720, 375);
        setRedSideIcon();
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
            sidelabelBlack.setIcon(blackIcon);
        }
        // 显示黑方箭头，隐藏红方箭头（即使 icon 为 null 也要切换显示状态）
        sidelabelBlack.setVisible(true);
        sidelabelRed.setVisible(false);
    }

    public void setRedSideIcon() {
        if (redIcon != null) {
            sidelabelRed.setIcon(redIcon);
        }
        // 显示红方箭头，隐藏黑方箭头
        sidelabelRed.setVisible(true);
        sidelabelBlack.setVisible(false);
    }
}
