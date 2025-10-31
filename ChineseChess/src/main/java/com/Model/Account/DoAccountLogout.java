package main.java.com.Model.Account;

import main.java.com.GUI.Menu;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                "确认注销并删除本地保存的账号信息吗？",
                "确认注销",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opt != JOptionPane.YES_OPTION) return;

        // 删除用户主目录下的 .chinesechess/account.json
        try {
            Path homeAccount = Paths.get(System.getProperty("user.home"), ".chinesechess", "account.json");
            if (Files.exists(homeAccount)) {
                Files.delete(homeAccount);
            }
        } catch (Exception e) {
            // 非致命：记录并继续
            System.out.println("删除 home account.json 失败: " + e.getMessage());
        }

        // 删除项目内 accounts/<username>.json（若存在）
        try {
            String username = AccountSession.username;
            if (username != null && !username.trim().isEmpty()) {
                String safe = sanitizeFileName(username);
                Path acct = Paths.get("accounts", safe + ".json");
                if (Files.exists(acct)) {
                    Files.delete(acct);
                }
            }
        } catch (Exception e) {
            System.out.println("删除 accounts/<username>.json 失败: " + e.getMessage());
        }

        // 清空会话
        AccountSession.clear();

        // 更新主界面状态（如果 Menu 已创建）
        try {
            Menu.setStatusText("未登录");
        } catch (Exception ignore) {
        }

        JOptionPane.showMessageDialog(parentFrame, "已注销并删除本地账号信息", "注销成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String sanitizeFileName(String name) {
        if (name == null) return "";
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }
}
