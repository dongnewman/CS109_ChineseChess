package com.GUI.Piece;

public class InitPieces {
    private final int[][] piecesData = {
        // 黑子 第一排 (坐标从1开始)
        {1, 1, 0}, {1, 2, 1}, {1, 3, 2}, {1, 4, 3}, {1, 5, 4}, 
        {1, 6, 3}, {1, 7, 2}, {1, 8, 1}, {1, 9, 0},
        // 黑方其他棋子
        {3, 2, 5}, {3, 8, 5}, 
        {4, 1, 6}, {4, 3, 6}, {4, 5, 6}, {4, 7, 6}, {4, 9, 6},

        // 红子 第一排 (坐标从1开始)
        {10, 1, 7}, {10, 2, 8}, {10, 3, 9}, {10, 4, 10}, {10, 5, 11}, 
        {10, 6, 10}, {10, 7, 9}, {10, 8, 8}, {10, 9, 7},
        // 红方其他棋子
        {8, 2, 12}, {8, 8, 12}, 
        {7, 1, 13}, {7, 3, 13}, {7, 5, 13}, {7, 7, 13}, {7, 9, 13}
    };

    private final String[] piecesBaseNames = {
        "pieces/piece-black-che",
        "pieces/piece-black-ma",
        "pieces/piece-black-xiang",
        "pieces/piece-black-shi",
        "pieces/piece-black-jiang",
        "pieces/piece-black-pao",
        "pieces/piece-black-zu",

        "pieces/piece-red-che",
        "pieces/piece-red-ma",
        "pieces/piece-red-xiang",
        "pieces/piece-red-shi",
        "pieces/piece-red-shuai",
        "pieces/piece-red-pao",
        "pieces/piece-red-bing"
    };

    /**
     * 初始化所有棋子并放入传入的 PiecesSession 中。
     * 返回 true 表示初始化成功，false 表示失败（例如 session 为 null 或发生异常）。
     */
    public boolean initAllPieces(PiecesSession session) {
        if (session == null) return false;
        try {
            for (int[] data : piecesData) {
                int row = data[0];      // 行（1~10）
                int col = data[1];      // 列（1~9）
                int typeIndex = data[2]; // 类型索引（0~6 黑，7~13 红）

                String baseName = piecesBaseNames[typeIndex];
                // Pieces 构造器期望传入 baseName（如 "pieces/piece-black-jiang"），以及 col,row
                Pieces piece = new Pieces(baseName, col, row);
                // piecesSession 使用 0-based 索引
                session.setPiece(row, col, piece);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
