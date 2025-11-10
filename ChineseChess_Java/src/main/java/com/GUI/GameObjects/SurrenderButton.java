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

public class SurrenderButton {
    private String file_path = "src\\main\\resources\\surrender.png";
    private JFrame parentFrame;
    public SurrenderButton(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        JButton surrenderbutton = new JButton();
        BufferedImage image = null;
        try {
            File f = new File(file_path);
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            System.out.println("Failed to load surrender button image: " + e.getMessage());
        }

        if(image != null) {
            surrenderbutton.setBorderPainted(false);
            surrenderbutton.setContentAreaFilled(false);
            surrenderbutton.setFocusPainted(false);
            surrenderbutton.setOpaque(false);
            surrenderbutton.setIcon(new javax.swing.ImageIcon(image));
            surrenderbutton.setPreferredSize(new java.awt.Dimension(60, 60));
            // 使用JLayeredPane或合适的布局将按钮放在右下角
            // 这里采用绝对定位，需设置布局为null
            parentFrame.setLayout(null);
            surrenderbutton.setBounds(parentFrame.getWidth() - 150, parentFrame.getHeight() - 80, 60, 60);
            parentFrame.add(surrenderbutton);
            parentFrame.setComponentZOrder(surrenderbutton, 0); // 保证按钮在最上层
            parentFrame.repaint();
            // 窗口大小变化时同步按钮位置
            parentFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    surrenderbutton.setBounds(parentFrame.getWidth() - 150, parentFrame.getHeight() - 80, 60, 60);
                }
            });
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
        // 弹出确认对话框
        String[] options = {"再战一会", "我要投降"};
        int choice = JOptionPane.showOptionDialog(null,
                "你确定要投降吗？",
                "投降确认",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) { // "我要投降"
            if (parentFrame != null) {
                parentFrame.dispose();
                // 接下来就是调用DoSurrender的方法了
                // 交给你来写了
                //
                //
                //
                //
                //
                //
                //
                // 记得写完之后在这里调用！
            }
            // 显示菜单界面
            Menu.frame.setVisible(true); 
        }
        // 否则什么都不做，返回游戏
    }
}
