package com.Model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// no AtomicBoolean needed for modal JDialog

/**
 * 删除当前账号：
 * - 弹出确认对话框，用户确认后删除 accounts 目录下的对应 json 文件
 * - 删除后将内存会话（AccountSession）重置为游客状态
 */


public class DoAccountDelete {

	public DoAccountDelete() {
	}

	/**
	 * 显示确认对话框并在确认时删除当前账号的文件。返回是否成功删除并退出登录（true 表示已删除）。
	 */

	public boolean showDialog() {
		// 检查当前会话
		if (!AccountSession.isLoggedIn() || AccountSession.getUsername() == null) {
			JOptionPane.showMessageDialog(null, "当前没有已登录的账号", "提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		String username = AccountSession.getUsername();

		Path dir = Paths.get("accounts");
		String safeFile = sanitizeFileName(username) + ".json";
		Path target = dir.resolve(safeFile);

		if (!Files.exists(target)) {
			JOptionPane.showMessageDialog(null, "账号文件不存在，无法验证密码：" + target.toString(), "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// 读取文件并提取密码字段
		String fileContent;
		try {
			fileContent = new String(Files.readAllBytes(target), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "读取账号文件失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String storedPassword = extractString(fileContent, "password");
		if (storedPassword == null) {
			JOptionPane.showMessageDialog(null, "账号文件中未找到密码字段，无法验证", "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// 使用模态 JDialog 弹出密码输入窗口（setVisible(true) 会阻塞直到关闭）
		final boolean[] verified = new boolean[]{false};

		JDialog dialog = new JDialog((Frame) null, "删除账号 - 输入密码确认", true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(360, 150);
		dialog.setLayout(new BorderLayout(8, 8));
		dialog.setLocationRelativeTo(null);

		JLabel label = new JLabel("请输入账号密码以确认删除：");
		label.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
		dialog.add(label, BorderLayout.NORTH);

	JPasswordField pwdField = new JPasswordField();
	pwdField.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
	dialog.add(pwdField, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
		JButton confirm = new JButton("确认");
		JButton cancelBtn = new JButton("取消");

		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				char[] input = pwdField.getPassword();
				String entered = input == null ? "" : new String(input);
				if (input != null) java.util.Arrays.fill(input, '\0');

				if (entered.equals(storedPassword)) {
					verified[0] = true;
					dialog.dispose();
				} else {
					JOptionPane.showMessageDialog(dialog, "密码不正确，请重试。", "错误", JOptionPane.ERROR_MESSAGE);
					// 保持对话框打开以便重试
				}
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

	bottom.add(confirm);
	bottom.add(cancelBtn);
	// 在密码框按 Enter 等同点击确认（放在 confirm 创建后）
	pwdField.addActionListener(e -> confirm.doClick());
	dialog.add(bottom, BorderLayout.SOUTH);

	// 将确认按钮作为默认按钮（按 Enter 将触发 confirm）
	dialog.getRootPane().setDefaultButton(confirm);

		// 显示模态对话框并阻塞直到用户关闭
		dialog.setVisible(true);

		if (!verified[0]) {
			// 用户取消或未验证通过
			return false;
		}

		// 验证通过，执行删除
		try {
			boolean deleted = Files.deleteIfExists(target);
			if (deleted) {
				AccountSession.clear();
				JOptionPane.showMessageDialog(null, "账号已删除并已退出登录", "成功", JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				JOptionPane.showMessageDialog(null, "删除失败：账号文件不存在或已被移除。", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "删除账号失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private String sanitizeFileName(String name) {
		if (name == null) return "";
		return name.replaceAll("[\\/:*?\"<>|]", "_");
	}

	private String extractString(String json, String key) {
		try {
			String pattern = "\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"";
			java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
			java.util.regex.Matcher m = p.matcher(json);
			if (m.find()) return m.group(1);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

}
