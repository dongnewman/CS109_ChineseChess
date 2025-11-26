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
import com.Controller.HistoryGame.HistoryPrompt;

public class AIGameButton {
    final String fsPath = "src\\main\\resources\\Buttons\\AIGameButton.png";
    final String resPath = "/Buttons/AIGameButton.png";

    public AIGameButton(JButton aiGameButton, JFrame parentFrame) {
        BufferedImage image = null;
        try (java.io.InputStream in = getClass().getResourceAsStream(resPath)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            // ignore, try filesystem next
        }
        if (image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    image = ImageIO.read(f);
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
                    try {
                        int defaultType = 1;
                        HistoryPrompt.Result res = HistoryPrompt.promptIfHistory(parentFrame, defaultType);
                        if (res.type == HistoryPrompt.ResultType.CANCEL) {
                            return; // 返回主菜单
                        } else if (res.type == HistoryPrompt.ResultType.LOAD_HISTORY && res.history != null) {
                            new InitGame(res.history.getBoard(), res.history.getType());
                        } else {
                            // START_NEW
                            new InitGame(new Board(), defaultType);
                        }
                    } catch (Exception e2) {
                        System.out.println("新游戏初始化失败: " + e2.getMessage());
                    }
                }
            });
        }
    }

    // history logic delegated to HistoryPrompt

}
