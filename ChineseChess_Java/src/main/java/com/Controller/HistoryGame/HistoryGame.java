package com.Controller.HistoryGame;

import com.Controller.InitGame;
import com.Model.Account.*;
import com.Model.InGame.playroom.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;

public class HistoryGame {
    public HistoryGame() {
        if (!AccountSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "读取历史游戏需要登录账号，请先登录。");
            System.out.println("User not logged in. Cannot read history game.");
            return;
        }
        String hString = AccountSession.getRawJson();
        // 如果内存中没有 rawJson，尝试从 accounts/<username>.history 读取
        if (hString == null || hString.trim().isEmpty()) {
            String username = AccountSession.getUsername();
            if (username != null && !username.isEmpty()) {
                try {
                    Path hist = Paths.get("accounts", sanitizeFileName(username) + ".history");
                    if (Files.exists(hist)) {
                        hString = new String(Files.readAllBytes(hist), StandardCharsets.UTF_8);
                    }
                } catch (Exception ex) {
                    System.err.println("HistoryGame: failed to read history file: " + ex.getMessage());
                }
            }
        }

        HistoryInfo hInfo = ReadHistory.StringtoBoard(hString);
        if (hInfo == null) {
            // GUI prompt
            String message = "无法读取历史游戏记录，可能是因为没有保存过游戏或存档已损坏。";
            JOptionPane.showMessageDialog(null, message);
            System.out.println("Failed to read history game from account data. not exist or broken.");
            return;
        }

        // GUI prompt
        String gt = (hInfo.getType() == 0) ? "双人对战" : "人机对战" ;
        String message = "历史游戏记录已成功加载。\n游戏类型：" + gt +"\n这是您想读取的记录吗？";
        int choice = JOptionPane.showConfirmDialog(null, message, "Read Game" , JOptionPane.YES_NO_OPTION);
        if(choice == 1) {
            return;
        }

        // 正常开始
        try {
            new InitGame(hInfo.getBoard(), hInfo.getType());
        } catch (Exception e) {
            System.err.println("HistoryGame: failed to start InitGame: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String sanitizeFileName(String name) {
        if (name == null)
            return "";
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }
}
