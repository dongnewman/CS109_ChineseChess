package com.GUI.GameObjects.Box;

import com.Controller.InGameObjects;

public class BoxSession {
    private Box[][] blueBoxArray = new Box[11][10];
    private Box[][] redBoxArray = new Box[11][10];

    // BlueBox相关操作
    // 示例：保存BlueBox对象
    public void setBlueBox(int row, int col) {
        BlueBox blueBox = new BlueBox(col, row);
        InGameObjects.plate.repaint();
        blueBoxArray[row][col] = blueBox;
    }
    // 示例：移除并返回BlueBox对象
    public boolean removeBlueBox(int row, int col) {
        if(blueBoxArray[row][col] == null) return false;
        blueBoxArray[row][col] = null;
        InGameObjects.plate.repaint();
        return true;
    }
    // 示例：获取BlueBox对象
    public Box getBoxAt(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 9) {
            return blueBoxArray[row][col];
        }
        return null;
    }

    // RedBox相关操作
    // 示例：保存RedBox对象
    public void setRedBox(int row, int col) {
        Box redBox = new RedBox(col, row);
        InGameObjects.plate.repaint();
        redBoxArray[row][col] = redBox;
    }
    // 示例：移除RedBox对象
    public boolean removeRedBox(int row, int col) {
        if(redBoxArray[row][col] == null) return false;
        redBoxArray[row][col] = null;
        InGameObjects.plate.repaint();
        return true;
    }
    // 示例：获取RedBox对象
    public Box getRedBoxAt(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 9) {
            return redBoxArray[row][col];
        }
        return null;
    }

    public void clearAllRedBoxes() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 9; j++) {
                if (redBoxArray[i][j] != null) {
                    redBoxArray[i][j] = null;
                }
            }
        }
        InGameObjects.plate.repaint();
    }

    public void clearAllBlueBoxes() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 9; j++) {
                if (blueBoxArray[i][j] != null) {
                    blueBoxArray[i][j] = null;
                }
            }
        }
        InGameObjects.plate.repaint();
    }
}