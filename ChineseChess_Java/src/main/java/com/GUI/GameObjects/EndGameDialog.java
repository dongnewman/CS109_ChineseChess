package com.GUI.GameObjects;

import javax.swing.JOptionPane;

import com.Controller.InGameObjects;
import com.GUI.Menu;
import javax.swing.JFrame;
import javax.swing.JButton;

import com.GUI.MenuObjects.MusicButton;

public class EndGameDialog {
    private String winner;

    public EndGameDialog(String winner, JFrame gameFrame) {
        this.winner = winner;
        // 停止计时
        InGameObjects.countdownTimer.pause();
        // 弹出赢家对话框
        String msg = ("red".equalsIgnoreCase(winner)) ? "红方胜利！" : "黑方胜利！";
        JOptionPane.showMessageDialog(null, msg, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        // 关闭游戏界面 — 触发窗口关闭事件以便执行保存/清理逻辑
        if (gameFrame != null) {
            gameFrame.dispatchEvent(
                    new java.awt.event.WindowEvent(gameFrame, java.awt.event.WindowEvent.WINDOW_CLOSING));
        }
        // 显示主菜单
        if (Menu.frame != null) {
            Menu.frame.setVisible(true);
            if (Menu.musicButton.isPlaying == true) {
                Menu.musicButton.doPlayMusic(); // 播放菜单音乐
            }
        }
    }
}
