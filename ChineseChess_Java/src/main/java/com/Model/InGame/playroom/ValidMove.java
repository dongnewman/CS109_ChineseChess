package com.Model.InGame.playroom;

import java.util.ArrayList;

public final class ValidMove {
    public static boolean isValidMove(Board board, Move move) {
        // get information
        boolean side = board.getSide();
        int xi = move.getxi(), yi = move.getyi();
        int xf = move.getxf(), yf = move.getyf();
        char piecei = board.getPiece(xi, yi);
        // universal checks
        if (piecei == '.' || Character.isLowerCase(piecei) != side) {
            return false;
        }
        if (xi == xf && yi == yf) {
            return false;
        }
        if (board.getPiece(xf, yf) != '.' && Character.isLowerCase(board.getPiece(xf, yf)) == side)
            return false;
        {// check the kings cant see each other
            int xK = 0, yK = 0, xk = 0, yk = 0;
            for (int x = 1; x <= 3; x++) {
                for (int y = 4; y <= 6; y++) {
                    if (board.getPiece(x, y) == 'K') {
                        xK = x;
                        yK = y;
                    }
                }
            }
            for (int x = 8; x <= 10; x++) {
                for (int y = 4; y <= 6; y++) {
                    if (board.getPiece(x, y) == 'k') {
                        xk = x;
                        yk = y;
                    }
                }
            }
            if (xi == xK && yi == yK) {
                xK = xf;
                yK = yf;
            }
            if (xi == xk && yi == yk) {
                xk = xf;
                yk = yf;
            }
            if (yK == yk) {
                int cnt = 0;
                for (int x = xK + 1; x < xk; x++) {
                    char piece = board.getPiece(x, yK);
                    if (piece != '.') {
                        if (xi == x && yi == yK) {
                            if (xf > xK && xf < xk && yf == yK) {
                                cnt++;
                            }
                        } else
                            cnt++;
                    }
                }
                if (cnt == 0)
                    return false;
            }
        }
        // piece specific checks
        if (piecei == 'R' || piecei == 'r') {
            if (xi == xf) {
                for (int y = Math.min(yi, yf) + 1; y < Math.max(yi, yf); y++) {
                    if (board.getPiece(xi, y) != '.') {
                        return false;
                    }
                }
                return true;
            } else if (yi == yf) {
                for (int x = Math.min(xi, xf) + 1; x < Math.max(xi, xf); x++) {
                    if (board.getPiece(x, yi) != '.') {
                        return false;
                    }
                }
                return true;
            } else
                return false;
        } else if (piecei == 'N' || piecei == 'n') {
            if (Math.abs(xi - xf) == 2 && Math.abs(yi - yf) == 1) {
                return board.getPiece((xi + xf) / 2, yi) == '.';
            } else if (Math.abs(xi - xf) == 1 && Math.abs(yi - yf) == 2) {
                return board.getPiece(xi, (yi + yf) / 2) == '.';
            } else {
                return false;
            }
        } else if (piecei == 'B' || piecei == 'b') {
            if (Math.abs(xi - xf) == 2 && Math.abs(yi - yf) == 2) {
                if (piecei == 'B' && xf > 5)
                    return false;
                if (piecei == 'b' && xf < 6)
                    return false;
                return board.getPiece((xi + xf) / 2, (yi + yf) / 2) == '.';
            } else {
                return false;
            }
        } else if (piecei == 'A' || piecei == 'a') {
            if (Math.abs(xi - xf) != 1 || Math.abs(yi - yf) != 1)
                return false;
            if (piecei == 'A') {
                return xf <= 3 && yf >= 4 && yf <= 6;
            } else {
                return xf >= 8 && yf >= 4 && yf <= 6;
            }
        } else if (piecei == 'K' || piecei == 'k') {
            if (Math.abs(xi - xf) + Math.abs(yi - yf) != 1)
                return false;
            if (piecei == 'K') {
                return xf <= 3 && yf >= 4 && yf <= 6;
            } else {
                return xf >= 8 && yf >= 4 && yf <= 6;
            }
        } else if (piecei == 'C' || piecei == 'c') {
            if (xi == xf) {
                int cnt = 0;
                for (int y = Math.min(yi, yf) + 1; y < Math.max(yi, yf); y++) {
                    if (board.getPiece(xi, y) != '.') {
                        cnt++;
                    }
                }
                // return (cnt == 0 && board.getPiece(xf, yf) == '.') || (cnt == 1 &&
                // board.getPiece(xf, yf) != '.');
                return cnt == 0 && board.getPiece(xf, yf) == '.' || cnt == 1 && board.getPiece(xf, yf) != '.';
            } else if (yi == yf) {
                int cnt = 0;
                for (int x = Math.min(xi, xf) + 1; x < Math.max(xi, xf); x++) {
                    if (board.getPiece(x, yi) != '.') {
                        cnt++;
                    }
                }
                // return (cnt == 0 && board.getPiece(xf, yf) == '.') || (cnt == 1 &&
                // board.getPiece(xf, yf) != '.');
                return cnt == 0 && board.getPiece(xf, yf) == '.' || cnt == 1 && board.getPiece(xf, yf) != '.';
            } else
                return false;
        } else {// pawn
            if (piecei == 'P') {
                if (xi <= 5) {
                    return yf == yi && xf == xi + 1;
                } else {
                    return Math.abs(xi - xf) + Math.abs(yi - yf) == 1 && xf >= xi;
                }
            } else {
                if (xi >= 6) {
                    return yf == yi && xf == xi - 1;
                } else {
                    return Math.abs(xi - xf) + Math.abs(yi - yf) == 1 && xf <= xi;
                }
            }
        }
    }

    private static int[][] dposForN = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 }, { 2, -1 },
            { 2, 1 } };

    public static ArrayList<Move> getAllValidMoves(Board board) {
        // wait for implementation
        ArrayList<Move> validMoves = new ArrayList<>();
        for (int xi = 1; xi <= 10; xi++) {
            for (int yi = 1; yi <= 9; yi++) {
                char piece = board.getPiece(xi, yi);
                if (piece == '.' || Character.isLowerCase(piece) != board.getSide()) {
                    continue;
                }
                if (piece == 'R' || piece == 'r') {
                    for (int xf = 1; xf <= 10; xf++) {
                        if (board.isValidMove(new Move(xi, yi, xf, yi))) {
                            validMoves.add(new Move(xi, yi, xf, yi));
                        }
                    }
                    for (int yf = 1; yf <= 9; yf++) {
                        if (board.isValidMove(new Move(xi, yi, xi, yf))) {
                            validMoves.add(new Move(xi, yi, xi, yf));
                        }
                    }
                } else if (piece == 'N' || piece == 'n') {
                    for (int[] dpos : dposForN) {
                        int xf = xi + dpos[0];
                        int yf = yi + dpos[1];
                        if (xf < 1 || xf > 10 || yf < 1 || yf > 9)
                            continue;
                        if (board.isValidMove(new Move(xi, yi, xf, yf))) {
                            validMoves.add(new Move(xi, yi, xf, yf));
                        }
                    }
                } else if (piece == 'B' || piece == 'b') {
                    for (int dx = -2; dx <= 2; dx += 4) {
                        for (int dy = -2; dy <= 2; dy += 4) {
                            int xf = xi + dx;
                            int yf = yi + dy;
                            if (xf < 1 || xf > 10 || yf < 1 || yf > 9)
                                continue;
                            if (board.isValidMove(new Move(xi, yi, xf, yf))) {
                                validMoves.add(new Move(xi, yi, xf, yf));
                            }
                        }
                    }
                } else if (piece == 'A' || piece == 'a') {
                    for (int dx = -1; dx <= 1; dx += 2) {
                        for (int dy = -1; dy <= 1; dy += 2) {
                            int xf = xi + dx;
                            int yf = yi + dy;
                            if (xf < 1 || xf > 10 || yf < 1 || yf > 9)
                                continue;
                            if (board.isValidMove(new Move(xi, yi, xf, yf))) {
                                validMoves.add(new Move(xi, yi, xf, yf));
                            }
                        }
                    }
                } else if (piece == 'K' || piece == 'k') {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (Math.abs(dx) + Math.abs(dy) != 1)
                                continue;
                            int xf = xi + dx;
                            int yf = yi + dy;
                            if (xf < 1 || xf > 10 || yf < 1 || yf > 9)
                                continue;
                            if (board.isValidMove(new Move(xi, yi, xf, yf))) {
                                validMoves.add(new Move(xi, yi, xf, yf));
                            }
                        }
                    }
                } else if (piece == 'C' || piece == 'c') {
                    for (int xf = 1; xf <= 10; xf++) {
                        if (board.isValidMove(new Move(xi, yi, xf, yi))) {
                            validMoves.add(new Move(xi, yi, xf, yi));
                        }
                    }
                    for (int yf = 1; yf <= 9; yf++) {
                        if (board.isValidMove(new Move(xi, yi, xi, yf))) {
                            validMoves.add(new Move(xi, yi, xi, yf));
                        }
                    }
                } else {// pawn
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (Math.abs(dx) + Math.abs(dy) != 1)
                                continue;
                            int xf = xi + dx;
                            int yf = yi + dy;
                            if (xf < 1 || xf > 10 || yf < 1 || yf > 9)
                                continue;
                            if (board.isValidMove(new Move(xi, yi, xf, yf))) {
                                validMoves.add(new Move(xi, yi, xf, yf));
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}