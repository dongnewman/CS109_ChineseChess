package com.Model.InGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.Controller.InGameObjects;
import com.GUI.GameObjects.EndGameDialog;
import com.Model.InGame.playroom.Board;

/**
 * 倒计时模块（放在 Model/InGame 中）
 * - 提供一个 JPanel（包含一个显示剩余时间的 JLabel 和一个控制按钮）
 * - 内部使用 javax.swing.Timer 每秒更新一次
 * - 提供 start/pause/reset 行为
 */
public class CountdownTimer {
    private int remainingSeconds;
    private final int initialSeconds;

    private final javax.swing.Timer swingTimer;
    private final JLabel timeLabel;
    private final JButton controlButton;
    private final JPanel panel;

    private boolean running = false;

    public CountdownTimer(int seconds) {
        this.initialSeconds = Math.max(0, seconds);
        this.remainingSeconds = this.initialSeconds;
        InGameObjects.countdownTimer = this;

        timeLabel = new JLabel(formatSeconds(remainingSeconds));
        // 更醒目的红色更大号字体用于倒计时数字
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 32f));
        timeLabel.setForeground(Color.RED);

        controlButton = new JButton("Start");
        controlButton.setFont(controlButton.getFont().deriveFont(14f));
        // 给按钮一个合适的首选大小以配合更大的面板
        controlButton.setPreferredSize(new Dimension(80, 28));

        // 每秒触发一次
        swingTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        swingTimer.setRepeats(true);

        controlButton.addActionListener(e -> {
            if (!running) {
                start();
            } else {
                pause();
            }
        });

        // panel 布局：垂直排列（时间在上，按钮在下）
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // 右对齐内容（便于放在右上角）
        JPanel labelWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        labelWrap.setOpaque(false);
        labelWrap.add(timeLabel);
        panel.add(labelWrap);

        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrap.setOpaque(false);
        btnWrap.add(controlButton);
        panel.add(btnWrap);

        // 计算并设置 panel 的 preferredSize，确保在未显示前也能取得合理尺寸
        Dimension dLabel = labelWrap.getPreferredSize();
        Dimension dBtn = btnWrap.getPreferredSize();
        int w = Math.max(dLabel.width, dBtn.width);
        int h = dLabel.height + dBtn.height;
        // 加一点内边距
        int padW = 16;
        int padH = 12;
        // 增大最小宽度与高度以确保面板更明显
        panel.setPreferredSize(new Dimension(Math.max(200, w + padW), Math.max(70, h + padH)));
        // 使面板可见并加边框，便于用户识别（不影响布局）
        panel.setOpaque(true);
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void tick() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
            timeLabel.setText(formatSeconds(remainingSeconds));
            if (remainingSeconds == 0) {
                // 倒计时结束
                // 可选：发出提示音
                Toolkit.getDefaultToolkit().beep();
                swingTimer.stop();
                running = false;
                boolean currentSide = InGameObjects.board.getSide();
                String sideStr = currentSide ? "Red" : "Black";
                System.out.println(sideStr + " time out!");
                EndGameDialog endGameDialog = new EndGameDialog(sideStr, (javax.swing.JFrame) InGameObjects.plate.getTopLevelAncestor());
            }
        } else {
            swingTimer.stop();
            running = false;
            controlButton.setText("Reset");
        }
    }

    private String formatSeconds(int s) {
        int min = s / 60;
        int sec = s % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void start() {
        if (remainingSeconds <= 0) {
            // 如果已为 0，则重置为初始值再启动
            remainingSeconds = initialSeconds;
            timeLabel.setText(formatSeconds(remainingSeconds));
        }
        swingTimer.start();
        running = true;
        controlButton.setText("Pause");
    }

    public void pause() {
        swingTimer.stop();
        running = false;
        controlButton.setText("Start");
    }

    public void changeSide() {
        remainingSeconds = initialSeconds;
        timeLabel.setText(formatSeconds(remainingSeconds));
    }

    public void setSeconds(int seconds) {
        this.remainingSeconds = Math.max(0, seconds);
        timeLabel.setText(formatSeconds(this.remainingSeconds));
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public boolean isRunning() {
        return running;
    }
}
