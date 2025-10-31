package main.java.com.Model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 登录对话框：要求输入用户名与密码，验证本地 accounts/<username>.json 中记录的 password 字段。
 */
public class DoAccountLogin {

    private JDialog dialog;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private boolean loggedIn = false;
    private String loggedUsername = null;

    public DoAccountLogin() {
        initUI();
    }

    private void initUI() {
        dialog = new JDialog((Frame) null, "登录", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(380, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(8,8));

        JPanel center = new JPanel(new GridLayout(2,2,6,6));
        center.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        center.add(new JLabel("用户名:"));
        usernameField = new JTextField();
    // 在用户名框按 Enter 将焦点移到密码框
    usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        center.add(usernameField);
        center.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
    // 在密码框按 Enter 触发确认登录
    passwordField.addActionListener(e -> onConfirm());
        center.add(passwordField);

        dialog.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ok = new JButton("确认");
        JButton cancel = new JButton("取消");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { onConfirm(); }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { onCancel(); }
        });

        bottom.add(ok);
        bottom.add(cancel);
        dialog.add(bottom, BorderLayout.SOUTH);
    // 将确认按钮设为默认按钮，按 Enter 时触发登录确认
    dialog.getRootPane().setDefaultButton(ok);
    }

    public boolean showDialog() {
        loggedIn = false;
        loggedUsername = null;
        dialog.setVisible(true);
        return loggedIn;
    }

    private void onCancel() {
        loggedIn = false;
        dialog.dispose();
    }

    private void onConfirm() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        char[] pwd = passwordField.getPassword();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "请输入用户名", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pwd == null || pwd.length == 0) {
            JOptionPane.showMessageDialog(dialog, "请输入密码", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path acct = Paths.get("accounts", sanitizeFileName(username) + ".json");
        String json = null;
        try {
            if (Files.exists(acct)) {
                byte[] b = Files.readAllBytes(acct);
                json = new String(b, StandardCharsets.UTF_8);
            } else {
                // 尝试 home 目录 account.json
                Path home = Paths.get(System.getProperty("user.home"), ".chinesechess", "account.json");
                if (Files.exists(home)) {
                    byte[] b = Files.readAllBytes(home);
                    json = new String(b, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "读取账号文件失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (json == null) {
            JOptionPane.showMessageDialog(dialog, "未找到该用户的本地账号文件", "登录失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String storedPwd = extractString(json, "password");
        String provided = new String(pwd);
        java.util.Arrays.fill(pwd, '\0');

        if (storedPwd == null) {
            JOptionPane.showMessageDialog(dialog, "账号文件中未存储密码，无法登录", "登录失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!storedPwd.equals(provided)) {
            JOptionPane.showMessageDialog(dialog, "用户名或密码错误", "登录失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 成功：设置内存会话（不写入 home account.json，以避免自动登录）
        try {
            AccountSession.setUsername(username);
            AccountSession.setEmail(extractString(json, "email"));
            AccountSession.setLoggedIn(true);
        } catch (Exception ignore) {}

        loggedIn = true;
        loggedUsername = username;
        try { main.java.com.GUI.Menu.setStatusText("已登录: " + username); } catch (Exception ignore) {}
        dialog.dispose();
    }

    private String extractString(String json, String key) {
        try {
            String pattern = "\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) return m.group(1);
        } catch (Exception e) {}
        return null;
    }

    private String sanitizeFileName(String name) {
        if (name == null) return "";
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }

    public String getLoggedUsername() { return loggedUsername; }
}
