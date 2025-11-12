package com.GUI.GameObjects.Piece;

import java.awt.Component;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MovePiece {

    private static final long ANIMATION_DURATION_MS = 500L;
    private static final int FPS = 60;

    private final JComponent boardComponent;
    private PiecesSession session;

    public MovePiece(JComponent boardComponent, PiecesSession session) {
        this.boardComponent = boardComponent;
        this.session = session;
    }

    public MovePiece(JComponent boardComponent) {
        this(boardComponent, null);
    }

    public void setSession(PiecesSession session) {
        this.session = session;
    }

    public boolean move(int fromRow, int fromCol, int toRow, int toCol) {
        if (SwingUtilities.isEventDispatchThread()) {
            return moveInternal(fromRow, fromCol, toRow, toCol);
        }

        final boolean[] resultHolder = new boolean[1];
        try {
            SwingUtilities.invokeAndWait(() -> resultHolder[0] = moveInternal(fromRow, fromCol, toRow, toCol));
            return resultHolder[0];
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 动画：指定起始行列和目标像素坐标移动
    public boolean moveByPixel(int fromRow, int fromCol, int toX, int toY) {
        if (SwingUtilities.isEventDispatchThread()) {
            return moveInternalByPixel(fromRow, fromCol, toX, toY);
        }
        final boolean[] resultHolder = new boolean[1];
        try {
            SwingUtilities.invokeAndWait(() -> resultHolder[0] = moveInternalByPixel(fromRow, fromCol, toX, toY));
            return resultHolder[0];
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // 动画：指定起始行列和目标行列移动
    private boolean moveInternal(int fromRow, int fromCol, int toRow, int toCol) {
        if (boardComponent == null) {
            System.err.println("MovePiece.move: board component is not set.");
            return false;
        }
        if (session == null) {
            System.err.println("MovePiece.move: session is null.");
            return false;
        }

        Pieces piece = session.getPiece(fromRow, fromCol);
        if (piece == null) {
            System.err.println("MovePiece.move: no piece at the source position.");
            return false;
        }

        final int startX = Pieces.columnToX(fromCol);
        final int startY = Pieces.rowToY(fromRow);
        final int endX = Pieces.columnToX(toCol);
        final int endY = Pieces.rowToY(toRow);

        return moveInternalByPixel(startX, startY, endX, endY, piece, fromRow, fromCol, toRow, toCol);
    }

    // 外部：以起始行列和目标像素坐标移动动画，能正确找到棋子对象并操作session
    private boolean moveInternalByPixel(int fromRow, int fromCol, int toX, int toY) {
        if (boardComponent == null) {
            System.err.println("MovePiece.move: board component is not set.");
            return false;
        }
        if (session == null) {
            System.err.println("MovePiece.move: session is null.");
            return false;
        }

        Pieces piece = session.getPiece(fromRow, fromCol);
        if (piece == null) {
            System.err.println("MovePiece.move: no piece at the source position.");
            return false;
        }
        if (piece.getImage() == null || piece.getWidth() == 0 || piece.getHeight() == 0) {
            System.err.println("MovePiece.move: piece image is not available for animation.");
            return false;
        }

        // 起点像素坐标
        final int startX = Pieces.columnToX(fromCol);
        final int startY = Pieces.rowToY(fromRow);

        Component rootComponent = SwingUtilities.getRoot(boardComponent);
        if (!(rootComponent instanceof RootPaneContainer container)) {
            System.err.println("MovePiece.move: unable to resolve root pane container for animation.");
            return false;
        }
        JLayeredPane layeredPane = container.getLayeredPane();
        if (layeredPane == null) {
            System.err.println("MovePiece.move: layered pane is null.");
            return false;
        }
        Point boardOrigin = SwingUtilities.convertPoint(boardComponent, 0, 0, layeredPane);
        final int pieceWidth = piece.getWidth();
        final int pieceHeight = piece.getHeight();
        final JLabel movingLabel = new JLabel(new ImageIcon(piece.getImage()));
        movingLabel.setSize(pieceWidth, pieceHeight);
        movingLabel.setOpaque(false);
        int startDrawX = startX - pieceWidth / 2 + boardOrigin.x;
        int startDrawY = startY - pieceHeight / 2 + boardOrigin.y;
        movingLabel.setLocation(startDrawX, startDrawY);
        layeredPane.add(movingLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
        // 目标行列未知，不做吃子，仅清空起点
        session.setPiece(fromRow, fromCol, null);
        boardComponent.repaint();
        final int delay = Math.max(5, 1000 / FPS);
        final long startTime = System.currentTimeMillis();
        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, elapsed / (double) ANIMATION_DURATION_MS);
            int currentX = (int) Math.round(startX + (toX - startX) * progress);
            int currentY = (int) Math.round(startY + (toY - startY) * progress);
            int drawX = currentX - pieceWidth / 2 + boardOrigin.x;
            int drawY = currentY - pieceHeight / 2 + boardOrigin.y;
            movingLabel.setLocation(drawX, drawY);
            if (progress >= 1.0) {
                timer.stop();
                // 动画结束后，保留该标签作为“观战区”的静态显示
                // 将层级降低，避免一直停留在拖拽层遮挡交互
                JLayeredPane.putLayer(movingLabel, JLayeredPane.PALETTE_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint(drawX, drawY, pieceWidth, pieceHeight);
                // 同步更新棋子中心坐标
                piece.setCenterPosition(toX, toY);
                // 注意：此处未放入session目标格，需外部根据toX/toY自行处理
                boardComponent.repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
        System.gc();
        return true;
    }

    // 重载：带piece和session操作的像素动画（供原moveInternal调用）
    private boolean moveInternalByPixel(int fromX, int fromY, int toX, int toY, Pieces piece, int fromRow, int fromCol,
            int toRow, int toCol) {
        if (boardComponent == null) {
            System.err.println("MovePiece.move: board component is not set.");
            return false;
        }
        if (session == null) {
            System.err.println("MovePiece.move: session is null.");
            return false;
        }
        if (piece == null) {
            System.err.println("MovePiece.move: no piece at the source position.");
            return false;
        }
        if (piece.getImage() == null || piece.getWidth() == 0 || piece.getHeight() == 0) {
            System.err.println("MovePiece.move: piece image is not available for animation.");
            return false;
        }
        Component rootComponent = SwingUtilities.getRoot(boardComponent);
        if (!(rootComponent instanceof RootPaneContainer container)) {
            System.err.println("MovePiece.move: unable to resolve root pane container for animation.");
            return false;
        }
        JLayeredPane layeredPane = container.getLayeredPane();
        if (layeredPane == null) {
            System.err.println("MovePiece.move: layered pane is null.");
            return false;
        }
        Point boardOrigin = SwingUtilities.convertPoint(boardComponent, 0, 0, layeredPane);
        final int pieceWidth = piece.getWidth();
        final int pieceHeight = piece.getHeight();
        final JLabel movingLabel = new JLabel(new ImageIcon(piece.getImage()));
        movingLabel.setSize(pieceWidth, pieceHeight);
        movingLabel.setOpaque(false);
        int startDrawX = fromX - pieceWidth / 2 + boardOrigin.x;
        int startDrawY = fromY - pieceHeight / 2 + boardOrigin.y;
        movingLabel.setLocation(startDrawX, startDrawY);
        layeredPane.add(movingLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
        if (session.getPiece(toRow, toCol) != null) {
            session.setPiece(toRow, toCol, null);
        }
        session.setPiece(fromRow, fromCol, null);
        boardComponent.repaint();
        final int delay = Math.max(5, 1000 / FPS);
        final long startTime = System.currentTimeMillis();
        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, elapsed / (double) ANIMATION_DURATION_MS);
            int currentX = (int) Math.round(fromX + (toX - fromX) * progress);
            int currentY = (int) Math.round(fromY + (toY - fromY) * progress);
            int drawX = currentX - pieceWidth / 2 + boardOrigin.x;
            int drawY = currentY - pieceHeight / 2 + boardOrigin.y;
            movingLabel.setLocation(drawX, drawY);
            if (progress >= 1.0) {
                timer.stop();
                layeredPane.remove(movingLabel);
                layeredPane.revalidate();
                layeredPane.repaint(drawX, drawY, pieceWidth, pieceHeight);
                piece.setCenterPosition(toX, toY);
                session.setPiece(toRow, toCol, piece);
                boardComponent.repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
        System.gc();
        return true;
    }
}
