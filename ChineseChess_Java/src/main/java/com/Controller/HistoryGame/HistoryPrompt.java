package com.Controller.HistoryGame;

import com.Model.Account.AccountSession;
import com.Model.InGame.playroom.Board;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import java.awt.Component;

public class HistoryPrompt {

    public enum ResultType {
        LOAD_HISTORY, START_NEW, CANCEL
    }

    public static class Result {
        public ResultType type;
        public HistoryInfo history; // may be null

        public Result(ResultType t, HistoryInfo h) {
            type = t;
            history = h;
        }
    }

    public static Result promptIfHistory(Component parent, int defaultType) {
        try {
            String hist = AccountSession.getRawJson();
            if (hist == null || hist.trim().isEmpty()) {
                String username = AccountSession.getUsername();
                if (username != null && !username.trim().isEmpty()) {
                    String fname = "accounts/" + username.replaceAll("[^a-zA-Z0-9._-]", "_") + ".history";
                    try {
                        if (Files.exists(Paths.get(fname))) {
                            hist = new String(Files.readAllBytes(Paths.get(fname)), StandardCharsets.UTF_8);
                        }
                    } catch (Exception ex) {
                        // ignore
                    }
                }
            }
            if (hist == null || hist.trim().isEmpty()) {
                return new Result(ResultType.START_NEW, null);
            }
            hist = hist.trim();
            HistoryInfo strict = ReadHistory.StringtoBoard(hist);
            // 严格校验失败则按无历史处理
            if (strict == null) {
                return new Result(ResultType.START_NEW, null);
            }
            HistoryInfo usable = strict;
            Board histBoard = usable.getBoard();
            // if finished or equals start position -> treat as no history
            if (histBoard.gameOver() || com.Model.InGame.playroom.Board.isStartingPosition(histBoard)) {
                return new Result(ResultType.START_NEW, null);
            }

            // show three options: Load, Start New, Return
            Object[] options = { "加载历史并继续", "忽略历史并开始新对局", "返回主菜单" };
            String msg = "检测到未完成的历史对局，选择操作：";
            int sel = JOptionPane.showOptionDialog(parent,
                    msg,
                    "检测到历史对局",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (sel == 0) {
                return new Result(ResultType.LOAD_HISTORY, usable);
            } else if (sel == 1) {
                return new Result(ResultType.START_NEW, null);
            } else {
                return new Result(ResultType.CANCEL, null);
            }
        } catch (Exception e) {
            return new Result(ResultType.START_NEW, null);
        }
    }

    private static boolean boardsEqual(Board a, Board b) {
        if (a == null || b == null)
            return false;
        char[][] A = a.getBoard();
        char[][] B = b.getBoard();
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 9; j++) {
                if (A[i][j] != B[i][j])
                    return false;
            }
        }
        return true;
    }
}
