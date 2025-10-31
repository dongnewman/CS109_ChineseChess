package main.java.com.Model.Account;

import main.java.com.GUI.Menu;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 注销逻辑：确认后删除本地保存的账号文件并清空会话。
 */
public class DoAccountLogout {

    /**
     * 执行注销流程：确认 -> 删除 home account.json -> 删除 accounts/<username>.json -> 清空会话 -> 更新 UI
     * @param parentFrame 父窗口，用于显示对话
     */
    public static void performLogout(JFrame parentFrame) {
        int opt = JOptionPane.showConfirmDialog(
                parentFrame,
                "确认注销？（不会删除本地保存的账号信息）",
                "确认注销",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opt != JOptionPane.YES_OPTION) return;

        // 仅清空会话，不删除磁盘上的账号文件
        AccountSession.clear();

        // 更新主界面状态（如果 Menu 已创建）
        try {
            Menu.setStatusText("未登录");
        } catch (Exception ignore) {
        }

        JOptionPane.showMessageDialog(parentFrame, "已注销（本地账号文件未被删除）", "注销成功", JOptionPane.INFORMATION_MESSAGE);
    }

    // no disk deletion anymore - helper removed
}
