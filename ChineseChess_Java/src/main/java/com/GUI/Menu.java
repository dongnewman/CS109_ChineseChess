package com.GUI;

import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.GUI.MenuObjects.MenuBackgroundInit;
import com.GUI.MenuObjects.MenuBarInit;
import com.GUI.MenuObjects.StartButton;

import javax.swing.JButton;

// Menu GUI 控制的核心和源头！！！

public class Menu {
    // 公开的静态标签引用，供其它类（例如 MenuListener）以受控方式更新显示文本
    public static JLabel label;
    JFrame frame;

    public Menu() {
        // 将实例化的 JLabel 赋给静态字段（避免在外部使用局部变量引用）
        label = new JLabel("Welcome to Chinese Chess");
        frame = new JFrame("Chinese Chess");
        frame.setSize(800, 600);
        frame.setLayout(new FlowLayout());

        // 处理关闭窗口行为
        // 拦截默认关闭行为，委托给 LeaveCheck 处理
        // 查看：LeaveCheck.java
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 委托给同包下的 LeaveCheck 类处理退出确认与退出动作
                LeaveCheck.handleWindowClosing(frame);
            }
        });


        // 初始化菜单栏（传入 frame 以便菜单中的“离开”项能委托统一的退出逻辑）
        // 查看：MenuBarInit.java
        frame.setJMenuBar(new javax.swing.JMenuBar());
        MenuBarInit.initMenuBar(frame.getJMenuBar(), frame);

        

        // 菜单栏生成完毕
        // 初始化菜单背景（传入 frame 以设置内容面板）
        // 查看：MenuFrameInit.java
        MenuBackgroundInit.initMenuBackground(frame);

        // Test
        // 添加鼠标监听器以调试点击位置
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Plate clicked at: x=" + x + ", y=" + y);
            }
        });

        // 开始生成正文部分
        // 开始游戏键
        // 旧的
        // JButton startGameButton = new JButton("开始游戏");
        // startGameButton.setBounds(550,400,300,50);
        // frame.add(startGameButton);
        // 新的
        JButton startGameButton = new JButton();
        StartButton startButtonObj = new StartButton(startGameButton, frame);

        // 第二个键（功能暂时未知）
        JButton secondButton = new JButton("~");
        secondButton.setBounds(550,470,300,50);
        frame.add(secondButton);

        // 第三个键（功能暂时未知）
        JButton thirdButton = new JButton("~");
        thirdButton.setBounds(550,540,300,50);
        frame.add(thirdButton);






        frame.add(label);
        frame.setVisible(true);
    }

    /**
     * 线程安全地设置菜单界面上的状态文本（在 EDT 上执行）。
     * 如果标签尚未初始化，则此调用会被忽略。
     */
    public static void setStatusText(final String text) {
        if (SwingUtilities.isEventDispatchThread()) {
            if (label != null) label.setText(text);
        } else {
            SwingUtilities.invokeLater(() -> {
                if (label != null) label.setText(text);
            });
        }
    }
}

