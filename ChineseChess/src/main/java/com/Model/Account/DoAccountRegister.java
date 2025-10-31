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

public class DoAccountRegister {
    // 账号注册的 Swing 窗口实现

    private JFrame frame;
    private JTextField usernameField;
    private JTextField emailField;

    public DoAccountRegister() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("注册");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8, 8));

        // 顶部标题
        JLabel title = new JLabel("欢迎注册！", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        frame.add(title, BorderLayout.NORTH);

        // 中央输入区域
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        center.add(new JLabel("username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        usernameField = new JTextField();
        center.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        center.add(new JLabel("email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        emailField = new JTextField();
        center.add(emailField, gbc);

        frame.add(center, BorderLayout.CENTER);

        // 底部按钮
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
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void onCancel() {
        frame.dispose();
        System.exit(0);
    }

    private void onConfirm() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "username 不能为空", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 构造简单 JSON 字符串并保存到 accounts/<username>.json
        String json = toJson(username, email);

        try {
            Path dir = Paths.get("accounts");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            String safeFileName = sanitizeFileName(username) + ".json";
            Path out = dir.resolve(safeFileName);
            Files.write(out, json.getBytes(StandardCharsets.UTF_8));
            JOptionPane.showMessageDialog(frame, "注册信息已保存：" + out.toString(), "成功", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "保存文件失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String toJson(String username, String email) {
        // 简单的 JSON 序列化（转义基本字符）
        return "{" +
                "\"username\":\"" + escapeJson(username) + "\"," +
                "\"email\":\"" + escapeJson(email) + "\"" +
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

    // 该类不再包含 main 方法；应由应用程序中的其他组件调用
}