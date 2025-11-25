package com.Controller;

import javax.swing.JComponent;

import com.GUI.GameObjects.Box.BoxSession;
import com.GUI.GameObjects.Piece.PiecesSession;
import com.GUI.GameObjects.Piece.UndoMove;
import com.Model.InGame.CountdownTimer;
import com.Model.InGame.playroom.Board;
import com.GUI.GameObjects.MessageLabel;
import com.GUI.GameObjects.SideIcon;

public class InGameObjects {
	public static PiecesSession piecesSession;
	public static JComponent plate;
	public static BoxSession blueBoxSession;
	public static BoxSession redBoxSession;
	public static int gametype;
	public static CountdownTimer countdownTimer;
	public static Board board;
	public static MessageLabel messageLabel;
    public static UndoMove undoMove;
    public static com.GUI.GameObjects.SideIcon sideIcon;
	// 用于同步等待界面初始化
	public static final java.util.concurrent.CountDownLatch uiReadyLatch = new java.util.concurrent.CountDownLatch(1);
}
