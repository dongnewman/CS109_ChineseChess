package com.Controller;

import com.GUI.Menu;
import com.Model.Account.AccountSession;

import javax.swing.SwingUtilities;

public class InitAll {
    public static void InitAllinit() {
        // 初始化所有必要的组件和设置

        // 为保证每次启动都要求登录或注册：不再自动读取 home 下的 account.json
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
