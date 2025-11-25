package com.chinesechess;

import javax.swing.SwingUtilities;

import com.Model.InGame.playersAI.Zobrist;
import com.Controller.SettingsSession;
// import com.Controller.InitAll;
import com.GUI.Menu;
import com.Model.Account.AccountSession;
import com.Controller.SettingsSession;

/**
 * 程序的入口类 Main
 * 负责初始化全局设置、账户会话，并启动图形用户界面 (GUI)。
 */
public class Main {
    public static SettingsSession settingsSession;

    /**
     * 程序主入口方法
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // 初始化所有必要组件
        // 清除之前的账户会话信息
        AccountSession.clear();
        // 初始化设置会话
        settingsSession = new SettingsSession();
        if(!settingsSession.settingsInit()) {
            System.out.println("Settings Init has some problem");
        }

        // 固定 Zobrist 的种子，使 Zobrist 哈希在不同运行间可复现
        // 这对于调试和复现特定的棋局状态非常有用
        // 如果你想使用其他种子，请修改这里的常量值
        long FIXED_ZOBRIST_SEED = 123456789L;
        Zobrist.init(FIXED_ZOBRIST_SEED);

        try {
            // 在事件调度线程中启动 GUI，确保线程安全
            SwingUtilities.invokeAndWait(() -> {
                com.GUI.FrameInit f = new com.GUI.FrameInit();
                f.show();
            });
        } catch (Exception e) {
            // 如果弹窗失败，继续以未登录状态进入主界面
            AccountSession.clear();
        } finally {
            // 强制进行一次垃圾回收
            System.gc();
            // 启动主菜单界面
            new Menu();
        }

        // 启动主界面
    }
}
