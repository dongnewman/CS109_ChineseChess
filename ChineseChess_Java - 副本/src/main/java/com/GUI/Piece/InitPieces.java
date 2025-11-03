package com.GUI.Piece;

import java.util.StringTokenizer;
import javax.swing.JFrame;
import com.GUI.Piece.Pieces;

public class InitPieces {
    private int[][] piecesData = {
    //黑子 第一排
        {0, 0, 0}, 
        {0, 1, 1}, 
        {0, 2, 2}, 
        {0, 3, 3}, 
        {0, 4, 4}, 
        {0, 5, 3}, 
        {0, 6, 2}, 
        {0, 7, 1}, 
        {0, 8, 0},
        //其它
        {2, 1, 5}, 
        {2, 7, 5}, 
        {3, 0, 6}, 
        {3, 2, 6}, 
        {3, 4, 6}, 
        {3, 6, 6}, 
        {3, 8, 6},

    //红子 第一列
        {9, 0, 0}, 
        {9, 1, 1}, 
        {9, 2, 2}, 
        {9, 3, 3}, 
        {9, 4, 4}, 
        {9, 5, 3}, 
        {9, 6, 2}, 
        {9, 7, 1}, 
        {9, 8, 0},
        //其它
        {7, 1, 5}, 
        {7, 7, 5}, 
        {6, 0, 6}, 
        {6, 2, 6}, 
        {6, 4, 6}, 
        {6, 6, 6}, 
        {6, 8, 6}
    };

    private final String[] piecesFilePaths = {
        "src\\main\\resources\\pieces\\piece-black-che.png",
        "src\\main\\resources\\pieces\\piece-black-ma.png",
        "src\\main\\resources\\pieces\\piece-black-xiang.png",
        "src\\main\\resources\\pieces\\piece-black-shi.png",
        "src\\main\\resources\\pieces\\piece-black-jiang.png",
        "src\\main\\resources\\pieces\\piece-black-pao.png",
        "src\\main\\resources\\pieces\\piece-black-bing.png",

        "src\\main\\resources\\pieces\\piece-red-che.png",
        "src\\main\\resources\\pieces\\piece-red-ma.png",
        "src\\main\\resources\\pieces\\piece-red-xiang.png",
        "src\\main\\resources\\pieces\\piece-red-shi.png",
        "src\\main\\resources\\pieces\\piece-red-jiang.png",
        "src\\main\\resources\\pieces\\piece-red-pao.png",
        "src\\main\\resources\\pieces\\piece-red-bing.png"
    };

    public boolean InitAllPieces(JFrame gameframe) {
        try{
            for(int i = 0; i < piecesData.length; i++){
                Pieces piece = new Pieces()
            }
        }
    }

}
