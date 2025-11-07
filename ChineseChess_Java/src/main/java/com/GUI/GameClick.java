package com.GUI;

import javax.swing.JComponent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * GameClick 类用于监听棋盘组件上的鼠标点击事件，并将点击位置转换为棋盘的行列坐标。
 * 
 * 功能说明：
 * 1. 构造时传入棋盘对应的 JComponent，并自动注册鼠标监听器。
 * 2. 每次点击会根据像素坐标计算棋盘格的行列（不在棋盘范围则返回 [0, 0]）。
 * 3. 所有点击坐标（包括无效点击）都会放入阻塞队列，支持多线程安全获取。
 * 4. 提供 waitForClick() 方法，阻塞直到有新的点击坐标返回。
 * 
 * 用法示例：
 * GameClick gameClick = new GameClick(plateComponent);
 * int[] pos = gameClick.waitForClick(); // pos[0]: 行, pos[1]: 列
 */
public class GameClick {
    private int[] clickposition = new int[2]; // 0: row, 1: col
    private JComponent jcomponent;
    private final BlockingQueue<int[]> clickQueue = new LinkedBlockingQueue<>();

    int x, y;
    int row, col;

    public GameClick(JComponent jcomponent) {
        this.jcomponent = jcomponent;
        setupMouseListener();
        
    }

    private void setupMouseListener() {
        jcomponent.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                x = evt.getX();
                y = evt.getY();
                caculateCoordinates();
            }
        });
    }
    
    private void caculateCoordinates() {
        boolean foundCol = false;
        boolean foundRow = false;

        // 检查列
        for(int i = 1; i < 10; i++) {
            if(Math.abs(x - (i * 77 - 38)) < 35) {
                clickposition[1] = i;
                foundCol = true;
                break;
            }
        }

        // 检查行
        for(int i = 1; i < 11; i++) {
            if(Math.abs(y - (i * 77 - 22)) < 35) {
                clickposition[0] = i;
                foundRow = true;
                break;
            }
        }

        // 如果没找到合法行或列，则返回0,0
        if (!foundCol || !foundRow) {
            clickposition[0] = 0;
            clickposition[1] = 0;
        }

        clickQueue.offer(new int[]{clickposition[0], clickposition[1]});
    }

    public int[] waitForClick() {
        try {
            
            // System.out.println("Clicked at row: " + clickposition[0] + ", col: " + clickposition[1]);
            return clickQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
