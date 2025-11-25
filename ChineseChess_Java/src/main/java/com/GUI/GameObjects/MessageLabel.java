package com.GUI.GameObjects;

import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import com.Controller.InGameObjects;

import javax.swing.JLabel;

public class MessageLabel {
    private JLabel messageLabel;
    private JLayeredPane layeredPane;

    public MessageLabel(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
        messageLabel = new JLabel();
        layeredPane.add(messageLabel, JLayeredPane.PALETTE_LAYER);
        // 保持宽度一致，方便自动换行
        int labelWidth = 200;
        int labelHeight = 80;
        messageLabel.setBounds(800, 365, labelWidth, labelHeight);

        // 设置字体为宋体、加粗、字号更大、颜色黑色
        messageLabel.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD, 20));
        messageLabel.setForeground(java.awt.Color.BLACK);

        // 设置背景色为#FFEBCD，且不透明
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new java.awt.Color(0xFF, 0xEB, 0xCD));

        // 默认欢迎语，自动换行
        setMessage("欢迎来到中国象棋游戏");
        InGameObjects.messageLabel = this;
        return;
    }

    /**
     * 设置消息文本，自动换行，无需手动插入换行
     */
    public void setMessage(String text) {
        // 使用 table 保证宽度和居中，提升自动换行兼容性
        messageLabel.setText("<html><center><table width='200'><tr><td align='center'>" + text + "</td></tr></table></center></html>");
    }

    public void setCheck() {
        String side;
        if(InGameObjects.board.getSide()) {
            side = "黑方";
        } else {
            side = "红方";
        }
        setMessage(side + "将军！");
    }

    public void setDefault() {
        setMessage("楚河汉界分天下，红黑对垒定乾坤");
    }

    public void setInvalidMove() {
        setMessage("无效走法，请重新走子");
    }
}
