package com.Controller;

import com.GUI.GameObjects.GameClick;
import com.GUI.GameObjects.Piece.MovePiece;
import com.GUI.GameObjects.Piece.RemovePiece;
import com.GUI.GameObjects.Piece.UndoMove;
import com.GUI.GameObjects.SideIcon;
import com.GUI.GameObjects.EndGameDialog;
import com.Model.InGame.playroom.*;
import com.Model.InGame.playersAI.*;
import com.Controller.InGameObjects;

/**
 * 游戏控制类 DoGame
 * 负责处理游戏的主要逻辑循环，包括玩家交互、移动合法性检查、
 * 以及 P2P 和 AI 对战模式的流程控制。
 */
public class DoGame {
    private Board board;
    private GameClick gameclick;

    /**
     * 构造函数
     * @param initialBoard 初始棋盘状态
     */
    DoGame(Board initialBoard) {
        board = initialBoard;
    }

    /**
     * 在界面上显示当前选中棋子的所有合法走法
     * @param boardRow 棋子在棋盘上的行坐标
     * @param boardCol 棋子在棋盘上的列坐标
     */
    private void showLegalMoves(int boardRow, int boardCol) {
        java.util.ArrayList<Move> moves = board.getAllLegalMoves();
        for (Move m : moves) {
            if (m.getxi() == boardRow && m.getyi() == boardCol) {
                int uiRow = 11 - m.getxf();
                int uiCol = m.getyf();
                InGameObjects.redBoxSession.setRedBox(uiRow, uiCol);
            }
        }
    }

    /**
     * 获取玩家的走棋操作
     * 该方法会阻塞等待玩家在界面上的点击操作，直到完成一次合法的走棋。
     * 包含两次点击逻辑：第一次选中棋子，第二次选择目标位置。
     * @return 玩家选择的合法走法 Move 对象
     */
    private Move getMove() {
        //
        System.out.println("In DoGame: Waiting for player move...");
        //
        Move move = new Move(0, 0, 0, 0);
        while (true) {
            // 等待第一次点击（选择棋子）
            int[] posi = gameclick.waitForClick();
            if (posi[0] == 0 && posi[1] == 0) {// 无效点击，继续等待
                continue;
            } else {
                // playroom definition coordination
                char piece = board.getPiece(11 - posi[0], posi[1]);
                // 检查点击位置是否有棋子，且是否为己方棋子
                if (piece == '.' || Character.isLowerCase(piece) != board.getSide()) {
                    continue;
                }
                move.setxi(11 - posi[0]);
                move.setyi(posi[1]);
                //
                // 高亮选中的棋子并显示合法走法
                InGameObjects.blueBoxSession.setBlueBox(posi[0], posi[1]);
                showLegalMoves(11 - posi[0], posi[1]);
                while (true) {
                    // 等待第二次点击（选择目标位置）
                    int[] posf = gameclick.waitForClick();
                    if (posf[0] == 0 && posf[1] == 0) {
                        // 点击无效区域，取消选中
                        InGameObjects.blueBoxSession.removeBlueBox(posi[0], posi[1]);
                        InGameObjects.redBoxSession.clearAllRedBoxes();
                        break;
                    } else {
                        // playroom definition coordination
                        move.setxf(11 - posf[0]);
                        move.setyf(posf[1]);
                        // 检查走法是否合法
                        if (board.isLegalMove(move)) {
                            InGameObjects.redBoxSession.clearAllRedBoxes();
                            InGameObjects.redBoxSession.setRedBox(11 - move.getxf(), move.getyf());
                            return move;
                        } else {
                            // 走法不合法，取消选中，重新开始选择
                            InGameObjects.blueBoxSession.removeBlueBox(posi[0], posi[1]);
                            InGameObjects.redBoxSession.clearAllRedBoxes();
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 播放吃子动画（如果发生了吃子）
     * @param removetool 移除棋子的工具对象
     * @param move 发生的走法
     */
    private void animateCapture(RemovePiece removetool, Move move) {
        char capturedChar = board.getPiece(move.getxf(), move.getyf());
        if (capturedChar == '.') {
            return;
        }
        int targetUiRow = 11 - move.getxf();
        boolean capturedIsBlack = Character.isLowerCase(capturedChar);
        // Run removal before board state mutates so the captured piece reference is
        // still in session.
        removetool.remove(targetUiRow, move.getyf(), capturedIsBlack);
        InGameObjects.plate.repaint();
    }

    /**
     * 启动人人对战 (P2P) 模式
     * 循环交替让双方玩家走棋，直到游戏结束。
     */
    public void gameP2P() {
        //
        System.out.println("P2P Game started!");
        //
        MovePiece movetool = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
        RemovePiece removetool = new RemovePiece(InGameObjects.piecesSession);
        gameclick = new GameClick(InGameObjects.plate);
        SideIcon sideIcon = new SideIcon(
                (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(InGameObjects.plate));
        InGameObjects.sideIcon = sideIcon;
        
        InGameObjects.undoMove = new UndoMove();

        while (true) {
            if (board.getSide()) {
                sideIcon.setBlackSideIcon();
            } else {
                sideIcon.setRedSideIcon();
            }
            if (board.gameOver()) {
                // the UI is waiting for implementation
                System.out.println("Game Over!");
                System.out.println(board.getSide() ? "Red wins!" : "Black wins!");
                // 调用对话框显示结果
                new EndGameDialog(board.getSide() ? "red" : "black",
                        (javax.swing.JFrame) javax.swing.SwingUtilities
                                .getWindowAncestor(InGameObjects.plate));
                break;
            }
            Move move = getMove();
            InGameObjects.messageLabel.setDefault();
            
            InGameObjects.undoMove.save();

            animateCapture(removetool, move);
            board.doMove(move);
            // UI animation
            // transform the coordination from board to screen board
            move.setxi(11 - move.getxi());
            move.setxf(11 - move.getxf());
            movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            try {
                Thread.sleep(550);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            InGameObjects.redBoxSession.clearAllRedBoxes();
            InGameObjects.countdownTimer.changeSide();
            if (Legal.isInCheck(board)) {
                InGameObjects.messageLabel.setCheck();
            }
        }
    }

    public void gameWithAI() {
        //
        System.out.println("Single Game started!");
        InGameObjects.messageLabel.setDefault();
        //
        MovePiece movetool = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
        RemovePiece removetool = new RemovePiece(InGameObjects.piecesSession);
        gameclick = new GameClick(InGameObjects.plate);
        SideIcon sideIcon = new SideIcon(
                (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(InGameObjects.plate));
        InGameObjects.sideIcon = sideIcon;

        HinatsuruAI AI = new HinatsuruAI();
        
        InGameObjects.undoMove = new UndoMove();

        while (true) {
            if (board.gameOver()) {
                // the UI is waiting for implementation
                System.out.println("Game Over!");
                System.out.println(board.getSide() ? "Red wins!" : "Black wins!");
                break;
            }
            if (board.getSide()) {
                sideIcon.setBlackSideIcon();
            } else {
                sideIcon.setRedSideIcon();
            }
            Move move;
            if (board.getSide()) {
                move = AI.makeMove(board);
                InGameObjects.blueBoxSession.setBlueBox(11 - move.getxi(), move.getyi());
                InGameObjects.redBoxSession.setRedBox(11 - move.getxf(), move.getyf());
            } else {
                move = getMove();
            }
            InGameObjects.messageLabel.setDefault();
            
            InGameObjects.undoMove.save();

            animateCapture(removetool, move);
            board.doMove(move);
            // UI animation
            // transform the coordination from board to screen board
            move.setxi(11 - move.getxi());
            move.setxf(11 - move.getxf());
            movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            try {
                Thread.sleep(550);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            InGameObjects.redBoxSession.clearAllRedBoxes();
            InGameObjects.countdownTimer.changeSide();
            if (Legal.isInCheck(board)) {
                InGameObjects.messageLabel.setCheck();
            }
        }
    }
}
