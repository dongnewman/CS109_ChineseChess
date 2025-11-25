package com.Controller;

import com.GUI.GameObjects.GameClick;
import com.GUI.GameObjects.Piece.MovePiece;
import com.GUI.GameObjects.Piece.RemovePiece;
import com.GUI.GameObjects.SideIcon;
import com.GUI.GameObjects.EndGameDialog;
import com.Model.InGame.playroom.*;
import com.Model.InGame.playersAI.*;
import com.Controller.InGameObjects;

public class DoGame {
    private Board board;
    private GameClick gameclick;

    DoGame(Board initialBoard) {
        board = initialBoard;
    }

    private Move getMove() {
        //
        System.out.println("In DoGame: Waiting for player move...");
        //
        boolean side = board.getSide();
        Move move = new Move(0, 0, 0, 0);
        while (true) {
            int[] posi = gameclick.waitForClick();
            if (posi[0] == 0 && posi[1] == 0) {// 无效点击，继续等待
                continue;
            } else {
                // playroom definition coordination
                char piece = board.getPiece(11 - posi[0], posi[1]);
                if (piece == '.' || Character.isLowerCase(piece) != side) {
                    continue;
                }
                move.setxi(11 - posi[0]);
                move.setyi(posi[1]);
                //
                InGameObjects.blueBoxSession.setBlueBox(posi[0], posi[1]);
                while (true) {
                    int[] posf = gameclick.waitForClick();
                    if (posf[0] == 0 && posf[1] == 0) {
                        InGameObjects.blueBoxSession.removeBlueBox(posi[0], posi[1]);
                        break;
                    } else {
                        // playroom definition coordination
                        move.setxf(11 - posf[0]);
                        move.setyf(posf[1]);
                        if (board.isLegalMove(move)) {
                            return move;
                        } else {
                            InGameObjects.blueBoxSession.removeBlueBox(posi[0], posi[1]);
                            break;
                        }
                    }
                }
            }
        }
    }

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

    public void gameP2P() {
        //
        System.out.println("P2P Game started!");
        //
        MovePiece movetool = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
        RemovePiece removetool = new RemovePiece(InGameObjects.piecesSession);
        gameclick = new GameClick(InGameObjects.plate);
        SideIcon sideIcon = new SideIcon(
                (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(InGameObjects.plate));
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
            animateCapture(removetool, move);
            board.doMove(move);
            // UI animation
            // transform the coordination from board to screen board
            move.setxi(11 - move.getxi());
            move.setxf(11 - move.getxf());
            movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            InGameObjects.redBoxSession.removeRedBox(move.getxi(), move.getyi());
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
        HinatsuruAI AI = new HinatsuruAI();
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
            } else {
                move = getMove();
            }
            InGameObjects.messageLabel.setDefault();
            animateCapture(removetool, move);
            board.doMove(move);
            // UI animation
            // transform the coordination from board to screen board
            move.setxi(11 - move.getxi());
            move.setxf(11 - move.getxf());
            movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            InGameObjects.redBoxSession.removeRedBox(move.getxi(), move.getyi());
            InGameObjects.countdownTimer.changeSide();
            if (Legal.isInCheck(board)) {
                InGameObjects.messageLabel.setCheck();
            }
        }
    }
}
