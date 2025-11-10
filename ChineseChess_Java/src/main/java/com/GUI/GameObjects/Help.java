package com.GUI.GameObjects;

import javax.swing.JFrame;

import com.Controller.InitGame;
import com.GUI.MenuObjects.MenuBackgroundInit;
import com.Model.Help.DoHelp;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Help {
    private String file_path = "src\\main\\resources\\help.png";
    public Help(JFrame parentFrame) {
        BufferedImage image = null;
        JButton helpbutton = new JButton();
        try {
            File f = new File(file_path);
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore, try classpath next
        }
        if (image == null) {
            try (java.io.InputStream in = MenuBackgroundInit.class.getResourceAsStream("/Menu.png")) {
                if (in != null) {
                    image = ImageIO.read(in);
                }
            } catch (IOException e) {
                // ignore
            }
        }
        if (image != null) {
            helpbutton.setBorderPainted(false);
            helpbutton.setContentAreaFilled(false);
            helpbutton.setFocusPainted(false);
            helpbutton.setOpaque(false);
            helpbutton.setIcon(new javax.swing.ImageIcon(image));
            helpbutton.setPreferredSize(new java.awt.Dimension(60, 60));
            // 使用JLayeredPane或合适的布局将按钮放在右下角
            // 这里采用绝对定位，需设置布局为null
            parentFrame.setLayout(null);
            helpbutton.setBounds(parentFrame.getWidth() - 80, parentFrame.getHeight() - 80, 60, 60);
            parentFrame.add(helpbutton);
            parentFrame.setComponentZOrder(helpbutton, 0); // 保证按钮在最上层
            parentFrame.repaint();
            // 窗口大小变化时同步按钮位置
            parentFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    helpbutton.setBounds(parentFrame.getWidth() - 80, parentFrame.getHeight() - 80, 60, 60);
                }
            });
            helpbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DoHelp doHelp = new DoHelp();
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }
}
