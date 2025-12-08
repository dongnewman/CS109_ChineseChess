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
import com.chinesechess.Main;
import com.Controller.InGameObjects;

public class SurrenderButton {
    private JFrame parentFrame;

    public SurrenderButton(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JButton surrenderbutton = new JButton();
        BufferedImage image = null;
        final String resPath = "/InGameIcons/surrender.png";
        final String fsPath = "src\\main\\resources\\InGameIcons\\surrender.png";
        // 先尝试类路径（jar 内）
        try (java.io.InputStream in = getClass().getResourceAsStream(resPath)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            System.out.println("Failed to load surrender button image from classpath: " + e.getMessage());
        }
        // 回退到源码文件系统路径
        if (image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    image = ImageIO.read(f);
                }
            } catch (IOException e) {
                System.out.println("Failed to load surrender button image from file system: " + e.getMessage());
            }
        }

        if (image != null) {
            surrenderbutton.setBorderPainted(false);
            surrenderbutton.setContentAreaFilled(false);
            surrenderbutton.setFocusPainted(false);
            surrenderbutton.setOpaque(false);
            surrenderbutton.setIcon(new javax.swing.ImageIcon(image));
            java.awt.Dimension size = new java.awt.Dimension(60, 60);
            surrenderbutton.setSize(size);

            javax.swing.JLayeredPane layeredPane = parentFrame.getLayeredPane();
            layeredPane.add(surrenderbutton, javax.swing.JLayeredPane.POPUP_LAYER);

            Runnable relocate = () -> {
                java.awt.Insets insets = parentFrame.getInsets();
                int horizontalMargin = 110;
                int verticalMargin = 40;
                int x = parentFrame.getWidth() - insets.right - horizontalMargin - size.width;
                int y = parentFrame.getHeight() - insets.bottom - verticalMargin - size.height;
                surrenderbutton.setLocation(Math.max(0, x), Math.max(0, y));
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
            surrenderbutton.addActionListener(new ActionListener() {
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
        if (Main.settingsSession.isDisableSurrender()) {
            JOptionPane.showMessageDialog(null,
                    "投降功能已被禁用！",
                    "无法投降",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 弹出确认对话框
        String[] options = { "再战一会", "我要投降" };
        int choice = JOptionPane.showOptionDialog(null,
                "你确定要投降吗？",
                "投降确认",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) { // "我要投降"
            InGameObjects.isSurrendered = true;
            if (parentFrame != null) {
                parentFrame.dispose();
                String winner = InGameObjects.board.getSide() ? "red" : "black";
                if(InGameObjects.gametype == 1) winner = "black";
                new EndGameDialog(winner, parentFrame);
            }
            // 显示菜单界面
            Menu.frame.setVisible(true);
            if (Menu.musicButton.isPlaying == true) {
                Menu.musicButton.doPlayMusic(); // 播放菜单音乐
            }
        }
        // 否则什么都不做，返回游戏
    }
}
