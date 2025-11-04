package com.GUI.Piece;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MovePiece {

    private final JComponent boardComponent;

    public MovePiece(JComponent boardComponent) {
        this.boardComponent = boardComponent;
    }

    public boolean Move(Graphics g, PiecesSession session, int fromRow, int fromCol, int toRow, int toCol) {
        if (boardComponent == null) {
            System.err.println("MovePiece.Move: board component is not set.");
            return false;
        }
        if (session == null) {
            System.err.println("MovePiece.Move: session is null.");
            return false;
        }

        Pieces piece = session.getPiece(fromRow, fromCol);
        if (piece == null) {
            System.err.println("MovePiece.Move: no piece at the source position.");
            return false;
        }

        final int startX = Pieces.columnToX(fromCol);
        final int startY = Pieces.rowToY(fromRow);
        final int endX = Pieces.columnToX(toCol);
        final int endY = Pieces.rowToY(toRow);

        if (piece.getImage() == null || piece.getWidth() == 0 || piece.getHeight() == 0) {
            System.err.println("MovePiece.Move: piece image is not available for animation.");
            return false;
        }

        Component rootComponent = SwingUtilities.getRoot(boardComponent);
        if (!(rootComponent instanceof javax.swing.RootPaneContainer)) {
            System.err.println("MovePiece.Move: unable to resolve root pane container for animation.");
            return false;
        }

        javax.swing.RootPaneContainer container = (javax.swing.RootPaneContainer) rootComponent;
        JLayeredPane layeredPane = container.getLayeredPane();
        if (layeredPane == null) {
            System.err.println("MovePiece.Move: layered pane is null.");
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

        final long animationDuration = 500L;
        final int fps = 60;
        final int delay = Math.max(5, 1000 / fps);
        final long startTime = System.currentTimeMillis();

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, elapsed / (double) animationDuration);
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
