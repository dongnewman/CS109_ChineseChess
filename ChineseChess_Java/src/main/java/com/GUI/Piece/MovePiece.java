package com.GUI.Piece;

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

        int startDrawX = startX - pieceWidth / 2 + boardOrigin.x;
        int startDrawY = startY - pieceHeight / 2 + boardOrigin.y;
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
            int currentX = (int) Math.round(startX + (endX - startX) * progress);
            int currentY = (int) Math.round(startY + (endY - startY) * progress);

            int drawX = currentX - pieceWidth / 2 + boardOrigin.x;
            int drawY = currentY - pieceHeight / 2 + boardOrigin.y;
            movingLabel.setLocation(drawX, drawY);

            if (progress >= 1.0) {
                timer.stop();
                layeredPane.remove(movingLabel);
                layeredPane.revalidate();
                layeredPane.repaint(drawX, drawY, pieceWidth, pieceHeight);

                piece.setCenterPosition(endX, endY);
                session.setPiece(toRow, toCol, piece);
                boardComponent.repaint();
            }
        });

        timer.setRepeats(true);
        timer.start();

        return true;
    }
}
