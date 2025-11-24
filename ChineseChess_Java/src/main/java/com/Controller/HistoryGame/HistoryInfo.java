package com.Controller.HistoryGame;

import com.Model.InGame.playroom.*;

public class HistoryInfo {
    private Board board;
    private int type;

    public HistoryInfo(Board board, int type) {
        this.board = board;
        this.type = type;
    }

    public Board getBoard() {
        return board;
    }

    public int getType() {
        return type;
    }
}
