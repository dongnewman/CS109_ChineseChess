package com.GUI.Piece;

import java.util.ArrayList;
import java.util.List;

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

    private final String[] piecesFilePaths = {
        "src\\main\\resources\\pieces\\piece-black-che.png",
        "src\\main\\resources\\pieces\\piece-black-ma.png",
        "src\\main\\resources\\pieces\\piece-black-xiang.png",
        "src\\main\\resources\\pieces\\piece-black-shi.png",
        "src\\main\\resources\\pieces\\piece-black-jiang.png",
        "src\\main\\resources\\pieces\\piece-black-pao.png",
        "src\\main\\resources\\pieces\\piece-black-zu.png",

        "src\\main\\resources\\pieces\\piece-red-che.png",
        "src\\main\\resources\\pieces\\piece-red-ma.png",
        "src\\main\\resources\\pieces\\piece-red-xiang.png",
        "src\\main\\resources\\pieces\\piece-red-shi.png",
        "src\\main\\resources\\pieces\\piece-red-shuai.png",
        "src\\main\\resources\\pieces\\piece-red-pao.png",
        "src\\main\\resources\\pieces\\piece-red-bing.png"
    };

    public List<Pieces> initAllPieces() {
        List<Pieces> piecesList = new ArrayList<>();
        for (int[] data : piecesData) {
            int row = data[0];      // 行（1~10）
            int col = data[1];      // 列（1~9）
            int typeIndex = data[2]; // 类型索引（0~6 黑，7~13 红）

            String imagePath = piecesFilePaths[typeIndex];
            Pieces piece = new Pieces(imagePath, col, row);
            piecesList.add(piece);
        }
        return piecesList;
    }

}
