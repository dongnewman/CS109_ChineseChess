package com.GUI.GameObjects;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import com.GUI.Menu;
import com.Controller.InGameObjects;
import com.Controller.InitGame;
import com.Model.InGame.playroom.Board;

public class RestartButton {
    private JFrame parentFrame;

    public RestartButton(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JButton restartbutton = new JButton();
        BufferedImage image = null;
        final String resPath = "/InGameIcons/Restart.png";
        final String fsPath = "src\\main\\resources\\InGameIcons\\Restart.png";
        try (java.io.InputStream in = getClass().getResourceAsStream(resPath)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            System.out.println("Failed to load restart button image from classpath: " + e.getMessage());
        }
        if (image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    image = ImageIO.read(f);
                }
            } catch (IOException e) {
                System.out.println("Failed to load restart button image from file system: " + e.getMessage());
            }
        }

        if (image != null) {
            restartbutton.setBorderPainted(false);
            restartbutton.setContentAreaFilled(false);
            restartbutton.setFocusPainted(false);
            restartbutton.setOpaque(false);
            restartbutton.setIcon(new javax.swing.ImageIcon(image));
            java.awt.Dimension size = new java.awt.Dimension(60, 60);
            restartbutton.setSize(size);

            javax.swing.JLayeredPane layeredPane = parentFrame.getLayeredPane();
            layeredPane.add(restartbutton, javax.swing.JLayeredPane.POPUP_LAYER);

            Runnable relocate = () -> {
                java.awt.Insets insets = parentFrame.getInsets();
                int horizontalMargin = 180;
                int verticalMargin = 40;
                int x = parentFrame.getWidth() - insets.right - horizontalMargin - size.width;
                int y = parentFrame.getHeight() - insets.bottom - verticalMargin - size.height;
                restartbutton.setLocation(Math.max(0, x), Math.max(0, y));
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
            restartbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Surrender();
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }

    private void Surrender() {
        // 弹出确认对话框
        String[] options = { "再战一会", "重新开始" };
        int choice = JOptionPane.showOptionDialog(null,
                "你确定要重新开始吗？",
                "重新开始确认",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) { // "重新开始"
            if (parentFrame != null) {
                parentFrame.dispose();
                System.gc();
                try {
                    new InitGame(new Board(), InGameObjects.gametype);
                } catch (Exception ex) {
                    System.out.println("新游戏初始化失败: " + ex.getMessage());
                }
            }
        }
        // 否则什么都不做，返回游戏
    }
}
