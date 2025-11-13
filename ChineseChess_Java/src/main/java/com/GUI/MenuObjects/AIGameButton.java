package com.GUI.MenuObjects;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.Controller.InitGame;
import com.Model.InGame.playroom.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class AIGameButton {
    final String file_path = "src\\main\\resources\\Buttons\\AIGamebutton.png";

    public AIGameButton(JButton aiGameButton, JFrame parentFrame) {
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
            aiGameButton.setBorderPainted(false);
            aiGameButton.setContentAreaFilled(false);
            // aiGameButton.setFocusPainted(false);
            // 加上自己的
            aiGameButton.setIcon(new javax.swing.ImageIcon(image));
            aiGameButton.setBounds(550, 470, 300, 50);
            parentFrame.add(aiGameButton);
            parentFrame.repaint();

            aiGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle start button click
                    // System.out.println("Start Button Clicked!");
                    try {
                        try {
                            InitGame initGame = new InitGame(new Board(), 1);
                        } catch (Exception e2) {
                            System.out.println("新游戏初始化失败: " + e2.getMessage());
                        }
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }
}
