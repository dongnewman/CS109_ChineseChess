package main.java.com.Controller;

import main.java.com.GUI.Menu;
import main.java.com.Model.Account.AccountSession;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class InitAll {
    public static void InitAllinit() {
        // 初始化所有必要的组件和设置

        // 约定：在用户主目录下检查 .chinesechess/account.json
        Path accountPath = Paths.get(System.getProperty("user.home"), ".chinesechess", "account.json");

        if (Files.exists(accountPath)) {
            try {
                byte[] bytes = Files.readAllBytes(accountPath);
                String json = new String(bytes, StandardCharsets.UTF_8);
                AccountSession.setFromJson(json);
                System.out.println("Account file found. loggedIn=" + AccountSession.isLoggedIn() + ", username=" + AccountSession.username);
            } catch (Exception e) {
                // 如果读取或解析失败，则清空会话并继续
                AccountSession.clear();
                System.out.println("Failed to read account file: " + e.getMessage());
            }
        } else {
            // 未找到账户文件，清空会话并弹出提示（在 EDT 中）
            AccountSession.clear();
            // 在 EDT 上同步弹出选项，让用户选择创建账户或以游客方式继续
            final int[] choice = new int[1];
            try {
                SwingUtilities.invokeAndWait(() -> {
                    Object[] options = {"创建账户", "以游客身份继续"};
                    int opt = JOptionPane.showOptionDialog(
                            null,
                            "您未登录",
                            "登录状态",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    choice[0] = opt;
                });
            } catch (Exception e) {
                choice[0] = -1;
            }

            // 处理用户选择
            if (choice[0] == 0) {
                // 创建账户：打开注册窗口（在 EDT 上异步打开该对话框）
                SwingUtilities.invokeLater(() -> {
                    new main.java.com.Model.Account.DoAccountRegister();
                });
            } else if (choice[0] == 1) {
                // 以游客身份继续：设置会话为游客
                AccountSession.clear();
                AccountSession.username = "游客";
                AccountSession.setLoggedIn(false);
                System.out.println("Continuing as guest");
            } else {
                // 用户关闭对话或发生错误，保持未登录状态
                AccountSession.clear();
            }
        }

        // 启动主界面
        new Menu();
    }
}
