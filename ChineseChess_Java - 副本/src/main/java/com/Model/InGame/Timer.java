// package com.Model.InGame;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// /**
//  * 倒计时模块（放在 Model/InGame 中）
//  * - 提供一个 JPanel（包含一个显示剩余时间的 JLabel 和一个控制按钮）
//  * - 内部使用 javax.swing.Timer 每秒更新一次
//  * - 提供 start/pause/reset 行为
//  */
// public class Timer {
// 	private int remainingSeconds;
// 	private final int initialSeconds;

// 	private final javax.swing.Timer swingTimer;
// 	private final JLabel timeLabel;
// 	private final JButton controlButton;
// 	private final JPanel panel;

// 	private boolean running = false;

// 	public Timer(int seconds) {
// 		this.initialSeconds = Math.max(0, seconds);
// 		this.remainingSeconds = this.initialSeconds;

// 		timeLabel = new JLabel(formatSeconds(remainingSeconds));
// 		timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 16f));

// 		controlButton = new JButton("Start");

// 		// 每秒触发一次
// 		swingTimer = new javax.swing.Timer(1000, new ActionListener() {
// 			@Override
// 			public void actionPerformed(ActionEvent e) {
// 				tick();
// 			}
// 		});
// 		swingTimer.setRepeats(true);

// 		controlButton.addActionListener(e -> {
// 			if (!running) {
// 				start();
// 			} else {
// 				pause();
// 			}
// 		});

// 		// panel 布局：垂直排列（时间在上，按钮在下）
// 		panel = new JPanel();
// 		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
// 		// 右对齐内容（便于放在右上角）
// 		JPanel labelWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
// 		labelWrap.setOpaque(false);
// 		labelWrap.add(timeLabel);
// 		panel.add(labelWrap);

// 		JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
// 		btnWrap.setOpaque(false);
// 		btnWrap.add(controlButton);
// 		panel.add(btnWrap);
// 	}

// 	private void tick() {
// 		if (remainingSeconds > 0) {
// 			remainingSeconds--;
// 			timeLabel.setText(formatSeconds(remainingSeconds));
// 			if (remainingSeconds == 0) {
// 				// 倒计时结束
// 				swingTimer.stop();
// 				running = false;
// 				controlButton.setText("Reset");
// 				// 可选：发出提示音
// 				Toolkit.getDefaultToolkit().beep();
// 			}
// 		} else {
// 			swingTimer.stop();
// 			running = false;
// 			controlButton.setText("Reset");
// 		}
// 	}

// 	private String formatSeconds(int s) {
// 		int min = s / 60;
// 		int sec = s % 60;
// 		return String.format("%02d:%02d", min, sec);
// 	}

// 	public JPanel getPanel() {
// 		return panel;
// 	}

// 	public void start() {
// 		if (remainingSeconds <= 0) {
// 			// 如果已为 0，则重置为初始值再启动
// 			remainingSeconds = initialSeconds;
// 			timeLabel.setText(formatSeconds(remainingSeconds));
// 		}
// 		swingTimer.start();
// 		running = true;
// 		controlButton.setText("Pause");
// 	}

// 	public void pause() {
// 		swingTimer.stop();
// 		running = false;
// 		controlButton.setText("Start");
// 	}

// 	public void reset() {
// 		swingTimer.stop();
// 		running = false;
// 		remainingSeconds = initialSeconds;
// 		timeLabel.setText(formatSeconds(remainingSeconds));
// 		controlButton.setText("Start");
// 	}

// 	public void setSeconds(int seconds) {
// 		this.remainingSeconds = Math.max(0, seconds);
// 		timeLabel.setText(formatSeconds(this.remainingSeconds));
// 	}

// 	public int getRemainingSeconds() {
// 		return remainingSeconds;
// 	}

// 	public boolean isRunning() {
// 		return running;
// 	}
// }
