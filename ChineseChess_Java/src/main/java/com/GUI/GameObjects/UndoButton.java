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

public class UndoButton {
    private JFrame parentFrame;

    public UndoButton(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JButton undobutton = new JButton();
        BufferedImage image = null;
        final String resPath = "/InGameIcons/Undo.png";
        final String fsPath = "src\\main\\resources\\InGameIcons\\Undo.png";
        try (java.io.InputStream in = getClass().getResourceAsStream(resPath)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            System.out.println("Failed to load undo button image from classpath: " + e.getMessage());
        }
        if (image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    image = ImageIO.read(f);
                }
            } catch (IOException e) {
                System.out.println("Failed to load undo button image from file system: " + e.getMessage());
            }
        }

        if (image != null) {
            undobutton.setBorderPainted(false);
            undobutton.setContentAreaFilled(false);
            undobutton.setFocusPainted(false);
            undobutton.setOpaque(false);
            undobutton.setIcon(new javax.swing.ImageIcon(image));
            java.awt.Dimension size = new java.awt.Dimension(60, 60);
            undobutton.setSize(size);

            javax.swing.JLayeredPane layeredPane = parentFrame.getLayeredPane();
            layeredPane.add(undobutton, javax.swing.JLayeredPane.POPUP_LAYER);

            Runnable relocate = () -> {
                java.awt.Insets insets = parentFrame.getInsets();
                int horizontalMargin = 250;
                int verticalMargin = 40;
                int x = parentFrame.getWidth() - insets.right - horizontalMargin - size.width;
                int y = parentFrame.getHeight() - insets.bottom - verticalMargin - size.height;
                undobutton.setLocation(Math.max(0, x), Math.max(0, y));
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
            undobutton.addActionListener(new ActionListener() {
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
        // 检查是否可以Undo
        if (com.Controller.InGameObjects.undoMove == null || !com.Controller.InGameObjects.undoMove.isCanUndo()) {
            JOptionPane.showMessageDialog(parentFrame,
                    "无法悔棋：只能悔棋一次或尚未走棋。",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 弹出确认对话框
        String[] options = { "取消", "确认" };
        int choice = JOptionPane.showOptionDialog(null,
                "你确定要Undo吗？\n(注意：每步棋只能悔棋一次)",
                "Undo确认",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) { // "确认"
            if (parentFrame != null) {
                // 接下来就是调用Undo的方法了
                if (com.Controller.InGameObjects.undoMove != null) {
                    com.Controller.InGameObjects.undoMove.undo();
                }
            }
        }
        // 否则什么都不做，返回游戏
    }
}
