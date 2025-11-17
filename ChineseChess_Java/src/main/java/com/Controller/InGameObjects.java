package com.Controller;

import javax.swing.JComponent;

import com.GUI.GameObjects.Box.BoxSession;
import com.GUI.GameObjects.Piece.PiecesSession;
import com.Model.InGame.CountdownTimer;

public class InGameObjects {
	public static PiecesSession piecesSession;
	public static JComponent plate;
	public static BoxSession blueBoxSession;
	public static BoxSession redBoxSession;
	public static int gametype;
	public static CountdownTimer countdownTimer;
	// 用于同步等待界面初始化
	public static final java.util.concurrent.CountDownLatch uiReadyLatch = new java.util.concurrent.CountDownLatch(1);
}
