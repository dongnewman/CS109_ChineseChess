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
        InGameObjects.gametype = type;
        
        if (SwingUtilities.isEventDispatchThread()) {
            new GameFrame();
        } else {
            SwingUtilities.invokeAndWait(() -> {
                new GameFrame();
            });
        }
        // 等待界面完全初始化
        InGameObjects.uiReadyLatch.await();
        // 如果传入了 initialBoard，重建 GUI 中的棋子布局以匹配历史棋盘
        try {
            com.GUI.GameObjects.Piece.PiecesSession session = InGameObjects.piecesSession;
            if (session != null && initialBoard != null) {
                // 清空现有 session
                for (int r = 1; r <= 10; r++) {
                    for (int c = 1; c <= 9; c++) {
                        session.setPiece(r, c, null);
                    }
                }
                // 根据 Board 的坐标（x=1..10,y=1..9）重建 GUI，注意 GUI row = 11 - x
                for (int x = 1; x <= 10; x++) {
                    for (int y = 1; y <= 9; y++) {
                        char pc = initialBoard.getPiece(x, y);
                        if (pc == '.' || pc == 0)
                            continue;
                        String base = mapCharToPieceBase(pc);
                        if (base == null)
                            continue;
                        int guiRow = 11 - x; // 反转 x
                        int guiCol = y;
                        com.GUI.GameObjects.Piece.Pieces piece = new com.GUI.GameObjects.Piece.Pieces(base, guiCol,
                                guiRow);
                        session.setPiece(guiRow, guiCol, piece);
                    }
                }
                // 请求重绘棋盘以更新显示
                if (InGameObjects.plate != null)
                    InGameObjects.plate.repaint();
            }
        } catch (Exception ex) {
            System.err.println("InitGame: failed to reconstruct GUI pieces from history: " + ex.getMessage());
        }
        GameClick gameclick = new GameClick(InGameObjects.plate);
        new Thread(() -> {
            int count = 0;
            while (count < 5) {
                int[] result = gameclick.waitForClick();
                System.out.println("In InitGame: Clicked at row: " + result[0] + ", col: " + result[1]);
                count++;
            }
        }).start();

        
        // 测试代码
        // System.out.println("Type: " + InGameObjects.gametype);

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

    private static String mapCharToPieceBase(char pc) {
        // map: UPPERCASE chars -> red images, lowercase -> black images
        switch (pc) {
            // uppercase -> red
            case 'R':
                return "pieces/piece-red-che";
            case 'N':
                return "pieces/piece-red-ma";
            case 'B':
                return "pieces/piece-red-xiang";
            case 'A':
                return "pieces/piece-red-shi";
            case 'K':
                return "pieces/piece-red-shuai";
            case 'C':
                return "pieces/piece-red-pao";
            case 'P':
                return "pieces/piece-red-bing";

            // lowercase -> black
            case 'r':
                return "pieces/piece-black-che";
            case 'n':
                return "pieces/piece-black-ma";
            case 'b':
                return "pieces/piece-black-xiang";
            case 'a':
                return "pieces/piece-black-shi";
            case 'k':
                return "pieces/piece-black-jiang";
            case 'c':
                return "pieces/piece-black-pao";
            case 'p':
                return "pieces/piece-black-zu";

            default:
                return null;
        }
    }
}
