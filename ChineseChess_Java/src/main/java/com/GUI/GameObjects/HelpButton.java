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

public class HelpButton {
    private String file_path = "src\\main\\resources\\help.png";
    public HelpButton(JFrame parentFrame) {
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
                System.out.println("Failed to load help button image from classpath: " + e.getMessage());
            }
        }
        if (image != null) {
            helpbutton.setBorderPainted(false);
            helpbutton.setContentAreaFilled(false);
            helpbutton.setFocusPainted(false);
            helpbutton.setOpaque(false);
            helpbutton.setIcon(new javax.swing.ImageIcon(image));
            java.awt.Dimension size = new java.awt.Dimension(60, 60);
            helpbutton.setSize(size);

            javax.swing.JLayeredPane layeredPane = parentFrame.getLayeredPane();
            layeredPane.add(helpbutton, javax.swing.JLayeredPane.POPUP_LAYER);

            Runnable relocate = () -> {
                java.awt.Insets insets = parentFrame.getInsets();
                int margin = 40;
                int x = parentFrame.getWidth() - insets.right - margin - size.width;
                int y = parentFrame.getHeight() - insets.bottom - margin - size.height;
                helpbutton.setLocation(Math.max(0, x), Math.max(0, y));
            };

            parentFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    relocate.run();
                }

                @Override
                public void componentMoved(java.awt.event.ComponentEvent e) {
                    relocate.run();
                }
            });

            relocate.run();
            layeredPane.revalidate();
            layeredPane.repaint();
            helpbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new DoHelp();
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }
}
