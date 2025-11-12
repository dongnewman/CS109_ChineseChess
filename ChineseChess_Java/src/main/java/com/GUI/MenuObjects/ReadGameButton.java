package com.GUI.MenuObjects;


import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ReadGameButton {
    final String file_path = "src\\main\\resources\\Buttons\\Readgamebutton.png";
    public ReadGameButton(JButton readGameButton, JFrame parentFrame) {
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
            readGameButton.setBorderPainted(false);
            readGameButton.setContentAreaFilled(false);
            // readGameButton.setFocusPainted(false);
            // 加上自己的
            readGameButton.setIcon(new javax.swing.ImageIcon(image));
            readGameButton.setBounds(550,540,300,50);
            parentFrame.add(readGameButton);
            parentFrame.repaint();

            readGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle start button click
                    // System.out.println("Start Button Clicked!");
                    try {
			            // InitGame initGame = new InitGame();
                        // 启动读取游戏的初始化逻辑
                        // 记得写上！！！
                        //
                        //
                        //
                        //
                        //
                        //
		            } catch (Exception e2) {
			            System.out.println("新游戏初始化失败: " + e2.getMessage());
		            }
                }
            });
        }
    }
}
