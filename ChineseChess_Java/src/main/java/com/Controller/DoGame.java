package com.Controller;

import com.GUI.Piece.MovePiece;
import com.GUI.Piece.RemovePiece;
import com.GUI.GameClick;

import com.Controller.playroom.*;

public class DoGame {
    private Board board;
    private GameClick gameclick;

    DoGame() {
        board = new Board();
    }

    private Move getMove() {
        //
        System.out.println("In DoGame: Waiting for player move...");
        //
        boolean side = board.getSide();
        Move move = new Move(0, 0, 0, 0);
        while (true) {
            //
            System.out.println("getin geti\n");
            //
            int[] posi = gameclick.waitForClick();
            if (posi[0] == 0 && posi[1] == 0) {// 无效点击，继续等待
                continue;
            } else {
                // playroom definition coordination
                char piece = board.getpiece(11 - posi[0], posi[1]);
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
                        if (board.isPossibleMove(move)) {
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

    public void gameStart() {
        //
        System.out.println("Game started!");
        //
        MovePiece movetool = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
        RemovePiece removetool = new RemovePiece(InGameObjects.piecesSession);
        gameclick = new GameClick(InGameObjects.plate);
        while (true) {
            if (board.gameOver()) {
                // the UI is waiting for implementation
                System.out.println("Game Over!");
                System.out.println(board.getSide() ? "Red wins!" : "Black wins!");
                return;
            }
            Move move = getMove();
            board.doMove(move);
            // UI animation
            // transform the coordination from board to screen board
            move.setxi(11 - move.getxi());
            move.setxf(11 - move.getxf());
            boolean ret = movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            ret = !ret;
            removetool.remove(move.getxf(), move.getyf(),
                    board.getpiece(move.getxf(), move.getyf()) == '.' ? false : true);
            InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            InGameObjects.redBoxSession.removeRedBox(move.getxi(), move.getyi());
            //
            // movetool.move(move.getxi(), move.getyi(), move.getxf(), move.getyf());
            // removetool.remove(move.getxf(), move.getyf(),
            // board.getpiece(move.getxf(), move.getyf()) == '.' ? false : true);
            // InGameObjects.blueBoxSession.removeBlueBox(move.getxi(), move.getyi());
            // InGameObjects.redBoxSession.removeRedBox(move.getxi(), move.getyi());
        }
    }
}
