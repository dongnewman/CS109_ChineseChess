package main.java.com.Model.Account;

import javax.swing.*;
import java.awt.*;

/**
 * 显示当前账号信息（账号、邮箱）的模态对话框。
 *
 * 使用示例:
 *   new DoAccountLook().showDialog(parentFrame);
 */
public class DoAccountLook {

    private JFrame frame;

    public DoAccountLook() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("账号信息");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(360, 160);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8,8));

        String username = AccountSession.getUsername();
        String email = AccountSession.getEmail();

        if (username == null || username.trim().isEmpty()) username = "(未设置)";
        if (email == null || email.trim().isEmpty()) email = "(未设置)";

        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,12,6,12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        center.add(new JLabel("当前账号:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        center.add(new JLabel(username), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        center.add(new JLabel("邮箱:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        center.add(new JLabel(email), gbc);

        frame.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = new JButton("关闭");
        close.addActionListener(e -> frame.dispose());
        bottom.add(close);
        frame.add(bottom, BorderLayout.SOUTH);
    }

    /** 显示独立窗口（无父窗口） */
    public void showDialog() {
        showDialog((Frame) null);
    }

    /** 显示独立窗口，并以 parent 为定位参考（若 parent 为 null 则居中屏幕） */
    public void showDialog(Frame parent) {
        if (parent != null) frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }
}
