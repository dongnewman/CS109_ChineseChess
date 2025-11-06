package com.GUI.Box;

import com.Controller.InGameObjects;

public class BoxSession {
    private Box[][] blueBoxArray = new Box[11][10];
    private Box[][] redBoxArray = new Box[11][10];

    // public BoxSession() {
    //     InGameObjects.blueBoxSession = this;
    //     InGameObjects.redBoxSession = this;
    // }

    // BlueBox相关操作
    // 示例：保存BlueBox对象
    public void setBlueBox(int row, int col) {
        BlueBox blueBox = new BlueBox(col, row);
        blueBoxArray[row][col] = blueBox;
    }
    // 示例：移除并返回BlueBox对象
    public boolean removeBlueBox(int row, int col) {
        if(blueBoxArray[row][col] == null) return false;
        blueBoxArray[row][col] = null;
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
        redBoxArray[row][col] = redBox;
    }
    // 示例：移除RedBox对象
    public boolean removeRedBox(int row, int col) {
        if(redBoxArray[row][col] == null) return false;
        redBoxArray[row][col] = null;
        return true;
    }
    // 示例：获取RedBox对象
    public Box getRedBoxAt(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 9) {
            return redBoxArray[row][col];
        }
        return null;
    }
    
}