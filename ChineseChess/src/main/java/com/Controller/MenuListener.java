package main.java.com.Controller;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.java.com.GUI.LeaveCheck;
import main.java.com.GUI.Menu;
import main.java.com.Model.Account.DoAccountRegister;

/**
 * 为菜单栏中的菜单项注册监听器（占位实现）。
 *
 * 说明：
 * - 只注册占位的 ActionListener（打印或日志提示未实现），不实现具体功能。
 * - 对文本为 “离开” 的菜单项，委托给现有的 LeaveCheck 以保持退出一致性。
 */
public class MenuListener {

	public static void registerListeners(JMenuBar jmb, final JFrame parentFrame) {
		if (jmb == null) return;

		for (int i = 0; i < jmb.getMenuCount(); i++) {
			JMenu menu = jmb.getMenu(i);
			if (menu == null) continue;

			for (int j = 0; j < menu.getItemCount(); j++) {
				JMenuItem item = menu.getItem(j);
				if (item == null) continue; // 可能是分隔符

				// 使用菜单文本作为 action command，便于识别
				final String cmd = item.getText();
				item.setActionCommand(cmd);

				// 注册占位监听器
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// 根据命令分发到具体的处理器（目前为占位实现）
						String command = e.getActionCommand();
						switch (command) {
							case "偏好设置":
								handlePreferences();
								break;
							case "查看资料":
								handleViewProfile();
								break;
							case "登录":
								handleLogin();
								break;
							case "注册":
								handleRegister();
								break;
							case "注销":
								handleLogout();
								break;
							case "删除账号":
								handleDeleteAccount();
								break;
							case "新游戏":
								handleNewGame();
								break;
							case "加载游戏":
								handleLoadGame();
								break;
							case "查看记录":
								handleViewRecord();
								break;
							case "删除记录":
								handleDeleteRecord();
								break;
							case "帮助内容":
								handleHelpContents();
								break;
							case "关于":
								handleAbout();
								break;
							case "离开":
								handleExit(parentFrame);
								break;
							default:
								System.out.println("菜单项 '" + command + "' 被触发 — 未实现");
						}
					}
				});
			}
		}
	}

	// ===== 菜单项处理器（均为占位实现） =====

	private static void handlePreferences() {
		System.out.println("TODO: handlePreferences() - 偏好设置");
        // 使用 Menu 提供的线程安全方法来更新界面文本
        Menu.setStatusText("TODO: handlePreferences() - 偏好设置");
	}

	private static void handleViewProfile() {
		System.out.println("TODO: handleViewProfile() - 查看资料");
	}

	private static void handleLogin() {
		System.out.println("TODO: handleLogin() - 登录");
	}

	private static void handleRegister() {
		// 打开模态注册对话框（当前已在 EDT 上），阻塞直到对话框关闭
		try {
			DoAccountRegister dlg = new DoAccountRegister();
			dlg.showDialog();
			// 注册后可进一步处理 dlg.getRegisteredUsername() 等信息
		} catch (Exception e) {
			System.out.println("打开注册对话框失败: " + e.getMessage());
		}
	}

	private static void handleLogout() {
		System.out.println("TODO: handleLogout() - 注销");
	}

	private static void handleDeleteAccount() {
		System.out.println("TODO: handleDeleteAccount() - 删除账号");
	}

	private static void handleNewGame() {
		System.out.println("TODO: handleNewGame() - 新游戏");
	}

	private static void handleLoadGame() {
		System.out.println("TODO: handleLoadGame() - 加载游戏");
	}

	private static void handleViewRecord() {
		System.out.println("TODO: handleViewRecord() - 查看记录");
	}

	private static void handleDeleteRecord() {
		System.out.println("TODO: handleDeleteRecord() - 删除记录");
	}

	private static void handleHelpContents() {
		System.out.println("TODO: handleHelpContents() - 帮助内容");
	}

	private static void handleAbout() {
		System.out.println("TODO: handleAbout() - 关于");
	}

	private static void handleExit(JFrame parentFrame) {
		// 保持原有退出逻辑的一致性
		LeaveCheck.handleWindowClosing(parentFrame);
	}
}
