package com.Controller;

import javax.swing.SwingUtilities;

import com.GUI.GameFrame;
import com.GUI.GameObjects.GameClick;
// import com.GUI.GameObjects.Piece.MovePiece;
// import com.GUI.GameObjects.Piece.RemovePiece;
import com.Model.InGame.playroom.*;

public class InitGame {
    // 示例：在程序其他地方实现三个棋子的移动动画

    public InitGame(Board initialBoard, int type) throws Exception {
        if (SwingUtilities.isEventDispatchThread()) {
            new GameFrame();
        } else {
            SwingUtilities.invokeAndWait(() -> {
                new GameFrame();
            });
        }
        // 等待界面完全初始化
        InGameObjects.uiReadyLatch.await();
        GameClick gameclick = new GameClick(InGameObjects.plate);
        new Thread(() -> {
            int count = 0;
            while (count < 5) {
                int[] result = gameclick.waitForClick();
                System.out.println("In InitGame: Clicked at row: " + result[0] + ", col: " + result[1]);
                count++;
            }
        }).start();

        // function:game P2P
        if (type == 0) {
            new Thread(() -> {
                new DoGame(initialBoard).gameP2P();
            }).start();
        }
        // new function: game with lovely hinatsuru AI
        else if (type == 1) {
            new Thread(() -> {
                new DoGame(initialBoard).gameWithAI();
            }).start();
        } else {
            throw new Exception("InitGame: Unknown game type " + type);
        }
        //
        // MovePiece movePiece;
        // movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
        // movePiece.move(7, 5, 6, 5);
        // movePiece.move(3, 2, 4, 2);
        // movePiece.move(10, 9, 9, 9);

        // RemovePiece removePiece;
        // removePiece = new RemovePiece(InGameObjects.piecesSession);
        // removePiece.remove(1, 1, true); // 移除黑色棋子
        // removePiece.remove(1,2, true);
        // removePiece.remove(1, 3, true);
        // removePiece.remove(10, 1, false);
        // removePiece.remove(10,2, false);
        // removePiece.remove(10, 3, false);

        // InGameObjects.blueBoxSession.setBlueBox(4, 5);
        // InGameObjects.redBoxSession.setRedBox(5, 5);

        // InGameObjects.blueBoxSession.removeBlueBox(4, 5);
        // InGameObjects.redBoxSession.removeRedBox(5, 5);
    }
}
