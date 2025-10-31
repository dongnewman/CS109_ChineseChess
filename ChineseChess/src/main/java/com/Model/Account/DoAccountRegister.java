package main.java.com.Model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 注册对话框。改为模态 JDialog，并提供 showDialog() 返回注册结果。
 */
public class DoAccountRegister {

    private JDialog dialog;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;

    // 注册结果
    private boolean registered = false;
    private String registeredUsername = null;
    private String registeredEmail = null;

    public DoAccountRegister() {
        initUI();
    }

    private void initUI() {
        dialog = new JDialog((Frame) null, "注册", true); // modal
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("欢迎注册！", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        dialog.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        center.add(new JLabel("username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        usernameField = new JTextField();
    // 在 username 按 Enter 跳到 email
    usernameField.addActionListener(e -> emailField.requestFocusInWindow());
        center.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        center.add(new JLabel("email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        emailField = new JTextField();
    // 在 email 按 Enter 跳到 password
    emailField.addActionListener(e -> passwordField.requestFocusInWindow());
        center.add(emailField, gbc);

    gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
    center.add(new JLabel("password:"), gbc);
    gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
    passwordField = new JPasswordField();
    // 在 password 按 Enter 跳到 confirm password
    passwordField.addActionListener(e -> passwordConfirmField.requestFocusInWindow());
    center.add(passwordField, gbc);

    gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
    center.add(new JLabel("confirm:"), gbc);
    gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
    passwordConfirmField = new JPasswordField();
    // 在 confirm password 按 Enter 触发注册确认
    passwordConfirmField.addActionListener(e -> onConfirm());
    center.add(passwordConfirmField, gbc);

        dialog.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        JButton confirm = new JButton("确认");
        JButton cancel = new JButton("取消");

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        bottom.add(confirm);
        bottom.add(cancel);
        dialog.add(bottom, BorderLayout.SOUTH);
    // 将确认按钮设为默认按钮，按 Enter 时触发注册确认
    dialog.getRootPane().setDefaultButton(confirm);
    }

    /**
     * 显示模态对话框并阻塞直到用户关闭。返回是否成功注册。
     */
    public boolean showDialog() {
        registered = false;
        registeredUsername = null;
        registeredEmail = null;
        dialog.setVisible(true); // modal, blocks until disposed
        return registered;
    }

    private void onCancel() {
        registered = false;
        dialog.dispose();
    }

    private void onConfirm() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        char[] pwd = passwordField.getPassword();
        char[] pwd2 = passwordConfirmField.getPassword();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "username 不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pwd == null || pwd.length == 0) {
            JOptionPane.showMessageDialog(dialog, "password 不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!java.util.Arrays.equals(pwd, pwd2)) {
            JOptionPane.showMessageDialog(dialog, "两次输入的密码不一致", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

    String password = new String(pwd);
    // clear password arrays asap
    java.util.Arrays.fill(pwd, '\0');
    java.util.Arrays.fill(pwd2, '\0');

    String json = toJson(username, email, password);

        try {
            Path dir = Paths.get("accounts");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            String safeFileName = sanitizeFileName(username) + ".json";
            Path out = dir.resolve(safeFileName);
            Files.write(out, json.getBytes(StandardCharsets.UTF_8));

            JOptionPane.showMessageDialog(dialog, "注册信息已保存：" + out.toString(), "成功", JOptionPane.INFORMATION_MESSAGE);

            // 不将 loggedIn 状态写入磁盘（程序启动时不自动登录）
            // 但在当前运行时会话中将用户置为已登录状态
            registered = true;
            registeredUsername = username;
            registeredEmail = email;
            // 设置内存会话（不会写回磁盘）
            AccountSession.setUsername(username);
            AccountSession.setEmail(email);
            AccountSession.setLoggedIn(true);

            dialog.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "保存文件失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 生成用于磁盘存储的 JSON：不将 loggedIn 设置为 true，避免自动登录
     */
    private String toJson(String username, String email, String password) {
        return "{" +
                "\"loggedIn\":false," +
                "\"username\":\"" + escapeJson(username) + "\"," +
                "\"email\":\"" + escapeJson(email) + "\"," +
                "\"password\":\"" + escapeJson(password) + "\"" +
                "}";
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20 || c > 0x7E) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }

    public String getRegisteredEmail() {
        return registeredEmail;
    }

}