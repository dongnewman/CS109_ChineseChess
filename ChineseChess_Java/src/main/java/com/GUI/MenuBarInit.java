package com.GUI;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.Controller.MenuListener;

public class MenuBarInit {
    /**
     * 初始化菜单栏并将“离开”菜单项委托给统一的 LeaveCheck 处理逻辑。
     *
     * @param jmb         要初始化的 JMenuBar
     * @param parentFrame 用于定位对话框并传递给 LeaveCheck
     */
    public static void initMenuBar(JMenuBar jmb, final JFrame parentFrame){

        // 创建“设置”菜单及其子菜单项
        JMenu jmSettings = new JMenu("设置");
        JMenuItem jmSettingsPreferences = new JMenuItem("偏好设置");
        jmSettings.add(jmSettingsPreferences);

        jmb.add(jmSettings);


        // 创建“账号”菜单及其子菜单项
        JMenu jmAccount = new JMenu("账号");
        JMenuItem jmAccountLook = new JMenuItem("查看资料");
        JMenuItem jmAccountLogin = new JMenuItem("登录");
        JMenuItem jmAccountRegister = new JMenuItem("注册");
        JMenuItem jmAccountLogout = new JMenuItem("注销");
        JMenuItem jmAccountDelete = new JMenuItem("删除账号");
        jmAccount.add(jmAccountLook);
        jmAccount.add(jmAccountLogin);
        jmAccount.add(jmAccountRegister);
        jmAccount.add(jmAccountLogout);
        jmAccount.addSeparator();
        jmAccount.add(jmAccountDelete);


        jmb.add(jmAccount);

        // 创建“游戏”菜单及其子菜单项
        JMenu jmGame = new JMenu("游戏");
        JMenuItem jmGameNew = new JMenuItem("新游戏");
        JMenuItem jmGameLoad = new JMenuItem("加载游戏");
        jmGame.add(jmGameNew);
        jmGame.add(jmGameLoad);

        jmb.add(jmGame);


        // 创建“记录”菜单及其子菜单项
        JMenu jmRecord = new JMenu("记录");
        JMenuItem jmRecordView = new JMenuItem("查看记录");
        JMenuItem jmRecordDelete = new JMenuItem("删除记录");
        jmRecord.add(jmRecordView);
        jmRecord.add(jmRecordDelete);

        jmb.add(jmRecord);


        // 创建“帮助”菜单及其子菜单项
        JMenu jmHelp = new JMenu("帮助");
        JMenuItem jmHelpContents = new JMenuItem("帮助内容");
        jmHelp.add(jmHelpContents);

        jmb.add(jmHelp);


        // 创建“关于”菜单及其子菜单项
        JMenu jmAbout = new JMenu("关于");
        JMenuItem jmAboutInfo = new JMenuItem("关于本软件");
        jmAbout.add(jmAboutInfo);
        
        jmb.add(jmAbout);


        // 创建“退出”菜单及其子菜单项
        JMenu jmExit = new JMenu("退出");
        JMenuItem jmExitLeave = new JMenuItem("离开");
        jmExit.add(jmExitLeave);

        jmb.add(jmExit);

        



        // 统一在 MenuListener 中注册菜单项的监听器（占位实现）
        MenuListener.registerListeners(jmb, parentFrame);
    }
}
