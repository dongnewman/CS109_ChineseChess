package com.GUI.GameObjects;

import javax.swing.*;
import java.awt.*;

public class CountDownTimer {
	private JPanel timerPanel;
	private int seconds;

	public CountDownTimer(int seconds) {
		this.seconds = seconds;
		timerPanel = new JPanel();
		timerPanel.setOpaque(true);
		timerPanel.setBackground(new Color(255, 255, 255, 200));
		timerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		JLabel label = new JLabel("倒计时: " + seconds + " 秒", SwingConstants.CENTER);
		label.setFont(new Font("微软雅黑", Font.BOLD, 18));
		timerPanel.setLayout(new BorderLayout());
		timerPanel.add(label, BorderLayout.CENTER);
		timerPanel.setPreferredSize(new Dimension(120, 50));
		// 你可以在这里添加倒计时逻辑（如Timer定时减少seconds并刷新label）
	}

	public JPanel getPanel() {
		return timerPanel;
	}
}
