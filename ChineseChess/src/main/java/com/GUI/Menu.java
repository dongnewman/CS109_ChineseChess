package main.java.com.GUI;

import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Menu GUI 控制的核心和源头！！！

public class Menu {
    JFrame frame;

    public Menu() {
        frame = new JFrame("Chinese Chess");

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

        frame.setSize(800, 600);
        frame.setLayout(new FlowLayout());

        
        

        frame.setVisible(true);
    }
}

