package com.GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LeaveCheck {

    /**
     * 处理窗口关闭事件：弹出中文确认对话框，用户选择“离开”时退出程序。
     *
     * @param frame 触发关闭的父窗口，用于对话框定位
     */
    public static void handleWindowClosing(JFrame frame) {
        Object[] options = {"残忍离开", "我再玩会"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "您确定要离开游戏吗？我再陪您玩会？",
                "确认离开",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );
        if (choice == 0) {
            // 用户确认离开，结束进程
            System.exit(0);
        }
        // 否则不做任何操作，窗口保持打开
    }
}
