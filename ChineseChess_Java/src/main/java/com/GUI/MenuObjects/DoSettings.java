package com.GUI.MenuObjects;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.Controller.SettingsSession;
import com.chinesechess.Main;

public class DoSettings {
    private JFrame settingFrame;

    public DoSettings() {
        settingFrame = new JFrame("Settings");
        settingFrame.setSize(400, 170);
        settingFrame.setLayout(null);
        // 初始化已经的选择
        Main.settingsSession.settingsInit();

        // 先声明RadioButton，后设置选中状态
        

        // 游戏开启后自动开始倒计时
        JLabel startTimerJL = new JLabel("游戏开启后自动开始倒计时");
        startTimerJL.setBounds(30, 10, 150, 30);
        JRadioButton startTimeRB1 = new JRadioButton("是");
        JRadioButton startTimeRB2 = new JRadioButton("否");
        if (Main.settingsSession.isStartTimer()) {
            startTimeRB1.setSelected(true);
        } else {
            startTimeRB2.setSelected(true);
        }
        startTimeRB1.setBounds(200, 10, 40, 30);
        startTimeRB2.setBounds(250, 10, 40, 30);
        ActionListener startTimerRL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("是".equals(e.getActionCommand())) {
                    Main.settingsSession.setStartTimer(true);
                } else {
                    Main.settingsSession.setStartTimer(false);
                }
            }
        };
        startTimeRB1.addActionListener(startTimerRL);
        startTimeRB2.addActionListener(startTimerRL);
        ButtonGroup startTimerBG = new ButtonGroup();
        startTimerBG.add(startTimeRB1);
        startTimerBG.add(startTimeRB2);
        settingFrame.add(startTimerJL);
        settingFrame.add(startTimeRB1);
        settingFrame.add(startTimeRB2);

        // 禁止投降
        JLabel disSurrenderJL = new JLabel("禁止投降");
        disSurrenderJL.setBounds(30, 50, 150, 30);
        JRadioButton disSurrenderRB1 = new JRadioButton("是");
        JRadioButton disSurrenderRB2 = new JRadioButton("否");
        if (Main.settingsSession.isDisableSurrender()) {
            disSurrenderRB1.setSelected(true);
        } else {
            disSurrenderRB2.setSelected(true);
        }
        disSurrenderRB1.setBounds(200, 50, 40, 30);
        disSurrenderRB2.setBounds(250, 50, 40, 30);
        ActionListener disSurrenderRL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("是".equals(e.getActionCommand())) {
                    Main.settingsSession.setDisableSurrender(true);
                } else {
                    Main.settingsSession.setDisableSurrender(false);
                }
            }
        };
        disSurrenderRB1.addActionListener(disSurrenderRL);
        disSurrenderRB2.addActionListener(disSurrenderRL);
        ButtonGroup disSurrenderBG = new ButtonGroup();
        disSurrenderBG.add(disSurrenderRB1);
        disSurrenderBG.add(disSurrenderRB2);
        settingFrame.add(disSurrenderJL);
        settingFrame.add(disSurrenderRB1);
        settingFrame.add(disSurrenderRB2);

        // 关闭音乐
        JLabel disMusicJL = new JLabel("关闭音乐");
        disMusicJL.setBounds(30, 90, 150, 30);
        JRadioButton disMusicRB1 = new JRadioButton("是");
        JRadioButton disMusicRB2 = new JRadioButton("否");
        if (Main.settingsSession.isDisableMusic()) {
            disMusicRB1.setSelected(true);
        } else {
            disMusicRB2.setSelected(true);
        }
        disMusicRB1.setBounds(200, 90, 40, 30);
        disMusicRB2.setBounds(250, 90, 40, 30);
        ActionListener disMusicRL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("是".equals(e.getActionCommand())) {
                    Main.settingsSession.setDisableMusic(true);
                } else {
                    Main.settingsSession.setDisableMusic(false);
                }
            }
        };
        disMusicRB1.addActionListener(disMusicRL);
        disMusicRB2.addActionListener(disMusicRL);
        ButtonGroup disMusicBG = new ButtonGroup();
        disMusicBG.add(disMusicRB1);
        disMusicBG.add(disMusicRB2);
        settingFrame.add(disMusicJL);
        settingFrame.add(disMusicRB1);
        settingFrame.add(disMusicRB2);

        // 添加窗口关闭事件，关闭时保存设置
        settingFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveSettingsToJson();
            }
        });
        settingFrame.setVisible(true);
    }

    private void saveSettingsToJson() {
        try {
            java.io.File file = new java.io.File("settings/settings.json");
            java.io.FileWriter writer = new java.io.FileWriter(file);
            writer.write("{\n");
            writer.write("  \"startTimer\": " + com.chinesechess.Main.settingsSession.isStartTimer() + ",\n");
            writer.write(
                    "  \"disableSurrender\": " + com.chinesechess.Main.settingsSession.isDisableSurrender() + ",\n");
            writer.write("  \"disableMusic\": " + com.chinesechess.Main.settingsSession.isDisableMusic() + "\n");
            writer.write("}\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
