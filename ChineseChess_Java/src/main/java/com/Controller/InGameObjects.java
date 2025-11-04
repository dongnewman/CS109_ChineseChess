package com.Controller;

import com.GUI.Piece.PiecesSession;
import javax.swing.JComponent;

public class InGameObjects {
	public static PiecesSession piecesSession;
	public static JComponent plate;
	// 用于同步等待界面初始化
	public static final java.util.concurrent.CountDownLatch uiReadyLatch = new java.util.concurrent.CountDownLatch(1);
}
