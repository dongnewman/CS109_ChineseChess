package com.GUI.MenuObjects;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.Controller.InitGame;
import com.Model.InGame.playroom.*;

public class P2PButton {
    final String file_path = "src\\main\\resources\\Buttons\\P2Pbutton.png";

    public P2PButton(JButton startbutton, JFrame parentFrame) {
        BufferedImage image = null;
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
            // 去掉原有的
            startbutton.setBorderPainted(false);
            startbutton.setContentAreaFilled(false);
            // startbutton.setFocusPainted(false);
            // 加上自己的
            startbutton.setIcon(new javax.swing.ImageIcon(image));
            startbutton.setBounds(550, 400, 300, 50);
            parentFrame.add(startbutton);
            parentFrame.repaint();

            startbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle start button click
                    // System.out.println("Start Button Clicked!");
                    try {
                        InitGame initGame = new InitGame(new Board(), 0);
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }
}
