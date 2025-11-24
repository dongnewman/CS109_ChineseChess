package com.chinesechess;

import javax.swing.SwingUtilities;

import com.Model.InGame.playersAI.Zobrist;

// import com.Controller.InitAll;
import com.GUI.Menu;
import com.Model.Account.AccountSession;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // 初始化所有必要组件
        AccountSession.clear();

        // 固定 Zobrist 的种子，使 Zobrist 哈希在不同运行间可复现
        // 如果你想使用其他种子，请修改这里的常量值
        long FIXED_ZOBRIST_SEED = 123456789L;
        Zobrist.init(FIXED_ZOBRIST_SEED);

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
