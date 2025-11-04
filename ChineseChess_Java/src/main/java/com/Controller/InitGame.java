package com.Controller;

import javax.swing.SwingUtilities;

import com.GUI.GameFrame;
import com.GUI.Piece.MovePiece;

public class InitGame {
// 示例：在程序其他地方实现三个棋子的移动动画
    private MovePiece movePiece;

    public InitGame() throws Exception {
    if (SwingUtilities.isEventDispatchThread()) {
        new GameFrame();
    } else {
        SwingUtilities.invokeAndWait(() -> {
            new GameFrame();
        });
    }

    // 等待界面完全初始化
    InGameObjects.uiReadyLatch.await();

    movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
    movePiece.move(7, 5, 6, 5);
    movePiece.move(3, 2, 4, 2);
    movePiece.move(10, 9, 9, 9);
}
}
