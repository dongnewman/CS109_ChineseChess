package com.Controller;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GUI 相关处理器
import com.GUI.LeaveCheck;
import com.GUI.Menu;
// Account 相关处理器
import com.Model.Account.DoAccountRegister;
import com.Model.Account.AccountSession;
import com.Model.Account.DoAccountDelete;
import com.Model.Account.DoAccountLook;
// Help 相关处理器
import com.Model.Help.DoHelp;
// About 相关处理器
import com.Model.About.DoAbout;


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

				// 使用菜单文本作为 action command（保留原行为）
				final String cmd = item.getText();
				item.setActionCommand(cmd);

				// 注册占位监听器：先将文本映射为 MenuAction 枚举，再基于枚举分发处理器
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String command = e.getActionCommand();
						MenuAction action = MenuAction.fromText(command);
						if (action == null) {
							System.out.println("菜单项 '" + command + "' 被触发 — 未实现");
							return;
						}

						switch (action) {
							case PREFERENCES:
								handlePreferences();
								break;
							case VIEW_PROFILE:
								handleViewProfile();
								break;
							case LOGIN:
								handleLogin();
								break;
							case REGISTER:
								handleRegister();
								break;
							case LOGOUT:
								handleLogout(parentFrame);
								break;
							case DELETE_ACCOUNT:
								handleDeleteAccount();
								break;
							case NEW_GAME:
								handleNewGame();
								break;
							case LOAD_GAME:
								handleLoadGame();
								break;
							case VIEW_RECORD:
								handleViewRecord();
								break;
							case DELETE_RECORD:
								handleDeleteRecord();
								break;
							case HELP_CONTENTS:
								handleHelpContents();
								break;
							case ABOUT:
								handleAbout();
								break;
							case LEAVE:
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

	// ===== 菜单项处理器 =====


    // 设置栏
	private static void handlePreferences() {
		System.out.println("TODO: handlePreferences() - 偏好设置");
        // 使用 Menu 提供的线程安全方法来更新界面文本
        Menu.setStatusText("TODO: handlePreferences() - 偏好设置");
	}



    // 账户栏
	private static void handleViewProfile() {
		
        DoAccountLook dal = new DoAccountLook();
        dal.showDialog();
        System.gc();

        //System.out.println("TODO: handleViewProfile() - 查看资料");
	}

	private static void handleLogin() {
		try {
			com.Model.Account.DoAccountLogin dlg = new com.Model.Account.DoAccountLogin();
			boolean ok = dlg.showDialog();
			if (ok) {
				String user = dlg.getLoggedUsername();
				if (user != null) Menu.setStatusText("已登录: " + user);
			}
		} catch (Exception e) {
			System.out.println("打开登录对话框失败: " + e.getMessage());
		}
        System.gc();
	}

	private static void handleRegister() {
		// 打开模态注册对话框（当前已在 EDT 上），阻塞直到对话框关闭
		try {
			DoAccountRegister dlg = new DoAccountRegister();
			boolean ok = dlg.showDialog();
			if (ok) {
				String username = dlg.getRegisteredUsername();
				// 尝试读取 accounts/<username>.json（注册器已写入包含 password 的文件）
				try {
					java.nio.file.Path p = java.nio.file.Paths.get("accounts", username.replaceAll("[\\/:*?\"<>|]", "_") + ".json");
					if (java.nio.file.Files.exists(p)) {
						String json = new String(java.nio.file.Files.readAllBytes(p), java.nio.charset.StandardCharsets.UTF_8);
						AccountSession.setFromJson(json);
					} else {
						AccountSession.setUsername(username);
						AccountSession.setLoggedIn(true);
					}
					Menu.setStatusText("已登录: " + username);
				} catch (Exception ex) {
					AccountSession.setUsername(username);
					AccountSession.setLoggedIn(true);
					Menu.setStatusText("已登录: " + username);
				}
			}
		} catch (Exception e) {
			System.out.println("打开注册对话框失败: " + e.getMessage());
		}
        System.gc();
	}

	private static void handleLogout(javax.swing.JFrame parentFrame) {
		// 统一注销逻辑委托给 AccountLogout
			try {
			com.Model.Account.DoAccountLogout.performLogout(parentFrame);
		} catch (Exception e) {
			System.out.println("注销失败: " + e.getMessage());
		}
        System.gc();
	}

	private static void handleDeleteAccount() {
        DoAccountDelete dad = new DoAccountDelete();
        dad.showDialog();
        System.gc();
		// System.out.println("TODO: handleDeleteAccount() - 删除账号");
	}


    // 游戏栏
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
		new DoHelp();
		System.gc();
		//System.out.println("TODO: handleHelpContents() - 帮助内容");
	}

	private static void handleAbout() {
        new DoAbout();
        System.gc();
		// System.out.println("TODO: handleAbout() - 关于");
	}

	private static void handleExit(JFrame parentFrame) {
		LeaveCheck.handleWindowClosing(parentFrame);
	}
}
