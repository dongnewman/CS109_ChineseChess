package com.GUI.GameObjects.Piece;

import com.Controller.InGameObjects;
import com.Model.InGame.playroom.Board;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import java.awt.Point;

public class UndoMove {
    private Pieces[][] savedPiecesArray;
    private Pieces[] savedBlackRemovedPieces;
    private Pieces[] savedRedRemovedPieces;
    private int savedBlackRemovedCount;
    private int savedRedRemovedCount;
    private char[][] savedBoardArray;
    private boolean savedSide;
    private boolean canUndo = false;

    public boolean isCanUndo() {
        return canUndo;
    }

    public void save() {
        PiecesSession session = InGameObjects.piecesSession;
        Board board = InGameObjects.board;
        
        if (session == null || board == null) return;

        // Deep copy piecesArray
        savedPiecesArray = new Pieces[11][10];
        Pieces[][] currentPieces = session.getPiecesArray();
        for (int i = 0; i < 11; i++) {
            System.arraycopy(currentPieces[i], 0, savedPiecesArray[i], 0, 10);
        }

        // Copy removed pieces
        savedBlackRemovedPieces = new Pieces[16];
        System.arraycopy(session.getBlackRemovedPieces(), 0, savedBlackRemovedPieces, 0, 16);
        
        savedRedRemovedPieces = new Pieces[16];
        System.arraycopy(session.getRedRemovedPieces(), 0, savedRedRemovedPieces, 0, 16);

        savedBlackRemovedCount = session.getBlackRemovedCount();
        savedRedRemovedCount = session.getRedRemovedCount();

        // Save board state
        char[][] currentBoard = board.getBoardArray();
        savedBoardArray = new char[11][10];
        for(int i=0; i<11; i++) {
            System.arraycopy(currentBoard[i], 0, savedBoardArray[i], 0, 10);
        }
        savedSide = board.getSide();
        
        canUndo = true;
    }

    public void undo() {
        if (!canUndo) return;
        
        PiecesSession session = InGameObjects.piecesSession;
        Board board = InGameObjects.board;

        // Restore session
        Pieces[][] currentPieces = session.getPiecesArray();
        for(int i=0; i<11; i++) {
             System.arraycopy(savedPiecesArray[i], 0, currentPieces[i], 0, 10);
        }
        
        Pieces[] currentBlackRemoved = session.getBlackRemovedPieces();
        System.arraycopy(savedBlackRemovedPieces, 0, currentBlackRemoved, 0, 16);
        
        Pieces[] currentRedRemoved = session.getRedRemovedPieces();
        System.arraycopy(savedRedRemovedPieces, 0, currentRedRemoved, 0, 16);
        
        session.setBlackRemovedCount(savedBlackRemovedCount);
        session.setRedRemovedCount(savedRedRemovedCount);

        // Restore board
        board.setBoardArray(savedBoardArray);
        board.setSide(savedSide);

        // Update UI positions
        updatePiecesPositions(session);
        
        // Refresh removed pieces visuals (JLabels)
        refreshRemovedPiecesVisuals();

        // Clear selection boxes
        if (InGameObjects.blueBoxSession != null) {
            InGameObjects.blueBoxSession.clearAllBlueBoxes();
        }
        if (InGameObjects.redBoxSession != null) {
            InGameObjects.redBoxSession.clearAllRedBoxes();
        }

        // Reset timer
        if (InGameObjects.countdownTimer != null) {
            InGameObjects.countdownTimer.changeSide();
        }

        // Update SideIcon
        if (InGameObjects.sideIcon != null) {
            if (board.getSide()) {
                InGameObjects.sideIcon.setBlackSideIcon();
            } else {
                InGameObjects.sideIcon.setRedSideIcon();
            }
        }

        InGameObjects.plate.repaint();
        canUndo = false;
    }

    private void updatePiecesPositions(PiecesSession session) {
        // Update pieces on board
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 9; j++) {
                Pieces p = session.getPiece(i, j);
                if (p != null) {
                    p.setBoardPosition(j, i);
                }
            }
        }

        // Update removed pieces
        Pieces[] blackRemoved = session.getBlackRemovedPieces();
        for (int i = 0; i < session.getBlackRemovedCount(); i++) {
            setRemovedPosition(blackRemoved[i], i, true);
        }
        
        Pieces[] redRemoved = session.getRedRemovedPieces();
        for (int i = 0; i < session.getRedRemovedCount(); i++) {
            setRemovedPosition(redRemoved[i], i, false);
        }
    }

    private void setRemovedPosition(Pieces piece, int index, boolean isBlack) {
        int x = 0, y = 0;
        if (isBlack) {
            if (index >= 0 && index <= 6) {
                x = 755 + index * 40;
                y = 290;
            } else if (index >= 8 && index <= 15) {
                x = 755 + (index - 8) * 40;
                y = 220;
            }
        } else {
            if (index >= 0 && index <= 6) {
                x = 755 + index * 40;
                y = 520;
            } else if (index >= 8 && index <= 15) {
                x = 755 + (index - 8) * 40;
                y = 590;
            }
        }
        if (piece != null) piece.setCenterPosition(x, y);
    }

    private void refreshRemovedPiecesVisuals() {
        JComponent boardComponent = InGameObjects.plate;
        Component rootComponent = SwingUtilities.getRoot(boardComponent);
        if (!(rootComponent instanceof javax.swing.RootPaneContainer container)) {
            return;
        }
        JLayeredPane layeredPane = container.getLayeredPane();
        
        // Remove all existing "RemovedPiece" labels
        Component[] components = layeredPane.getComponentsInLayer(JLayeredPane.PALETTE_LAYER);
        for (Component c : components) {
            if ("RemovedPiece".equals(c.getName())) {
                layeredPane.remove(c);
            }
        }
        
        // Re-add labels for current removed pieces
        PiecesSession session = InGameObjects.piecesSession;
        Point boardOrigin = SwingUtilities.convertPoint(boardComponent, 0, 0, layeredPane);

        addRemovedLabels(session.getBlackRemovedPieces(), session.getBlackRemovedCount(), layeredPane, boardOrigin);
        addRemovedLabels(session.getRedRemovedPieces(), session.getRedRemovedCount(), layeredPane, boardOrigin);
        
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void addRemovedLabels(Pieces[] pieces, int count, JLayeredPane layeredPane, Point boardOrigin) {
        for (int i = 0; i < count; i++) {
            Pieces p = pieces[i];
            if (p != null && p.getImage() != null) {
                JLabel label = new JLabel(new ImageIcon(p.getImage()));
                label.setSize(p.getWidth(), p.getHeight());
                label.setOpaque(false);
                label.setName("RemovedPiece");
                
                int drawX = p.getCenterX() - p.getWidth() / 2 + boardOrigin.x;
                int drawY = p.getCenterY() - p.getHeight() / 2 + boardOrigin.y;
                
                label.setLocation(drawX, drawY);
                layeredPane.add(label, JLayeredPane.PALETTE_LAYER);
            }
        }
    }
}
