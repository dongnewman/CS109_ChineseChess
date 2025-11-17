package com.chinesechess;

import javax.swing.SwingUtilities;

// import com.Controller.InitAll;
import com.GUI.Menu;
import com.Model.Account.AccountSession;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // 初始化所有必要组件
        AccountSession.clear();

        try {
            SwingUtilities.invokeAndWait(() -> {
                com.GUI.FrameInit f = new com.GUI.FrameInit();
                f.show();
            });
        } catch (Exception e) {
            // 如果弹窗失败，继续以未登录状态进入主界面
            AccountSession.clear();
        } finally {
            System.gc();
            new Menu();
        }

        // 启动主界面
    }
}
