package main.java.com.GUI;

import main.java.com.Model.Account.DoAccountLogin;
import main.java.com.Model.Account.DoAccountRegister;
import main.java.com.Model.Account.AccountSession;

import javax.swing.*;
import java.awt.*;

/**
 * 启动时的初始选择窗口：登录 / 注册 / 游客继续
 */
public class FrameInit {

    private JDialog dialog;

    public FrameInit() {
        dialog = new JDialog((Frame) null, "欢迎", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(360, 160);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(8,8));

        JLabel lbl = new JLabel("请选择登录方式", SwingConstants.CENTER);
        dialog.add(lbl, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        JButton loginBtn = new JButton("登录");
        JButton regBtn = new JButton("注册");
        JButton guestBtn = new JButton("以游客身份继续");

        loginBtn.addActionListener(e -> {
            DoAccountLogin dlg = new DoAccountLogin();
            boolean ok = dlg.showDialog();
            if (ok) {
                // 登录成功，AccountSession 已由 DoAccountLogin 设置
                dialog.dispose();
            }
        });

        regBtn.addActionListener(e -> {
            DoAccountRegister reg = new DoAccountRegister();
            boolean ok = reg.showDialog();
            if (ok) {
                // 注册成功后，注册对话已经写入 account.json，构造 json 并设置会话
                String username = reg.getRegisteredUsername();
                String email = reg.getRegisteredEmail();
                String json = "{" +
                        "\"loggedIn\":true," +
                        "\"username\":\"" + (username==null?"":username.replace("\"","\\\"")) + "\"," +
                        "\"email\":\"" + (email==null?"":email.replace("\"","\\\"")) + "\"" +
                        "}";
                AccountSession.setFromJson(json);
                dialog.dispose();
            }
        });

        guestBtn.addActionListener(e -> {
            AccountSession.clear();
            AccountSession.setUsername("游客");
            AccountSession.setLoggedIn(false);
            dialog.dispose();
        });

        btns.add(loginBtn);
        btns.add(regBtn);
        btns.add(guestBtn);
        
            // 启动对话默认按钮设为登录，按 Enter 将触发登录按钮
            dialog.getRootPane().setDefaultButton(loginBtn);

        dialog.add(btns, BorderLayout.CENTER);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
