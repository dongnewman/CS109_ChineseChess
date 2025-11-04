package com.GUI.Piece;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pieces {
    private String file_path;
    private BufferedImage image;
    // 中心坐标（以像素为单位）
    private int centerX;
    private int centerY;
    // 用于绘制的左上角坐标（计算自中心点和图片尺寸）
    private int drawX;
    private int drawY;
    private int width;
    private int height;

    /**
     * 构造器：以棋子资源名(不含路径，含扩展名或不含扩展名均可)与棋盘格位置(col,row)初始化。
     * 内部会尝试两种方式加载图片：1) 源码树下的 src/main/resources/{name}.png（便于开发运行）
     * 2) classpath 根下的 /{name}.png（便于打包后运行）。
     * 示例: new Pieces("piece-black-jiang", 5, 1)
     */
    public Pieces(String pieceBaseName, int col, int row) {
        // pieceBaseName 期望形式: "pieces/piece-black-jiang" 或带扩展名
        String baseName = pieceBaseName;
        // 如果带扩展名，移除
        if (baseName.endsWith(".png") || baseName.endsWith(".jpg")) {
            baseName = baseName.substring(0, baseName.lastIndexOf('.'));
        }
        String resourcePath = "/" + baseName + ".png"; // classpath 形式
        this.file_path = "src" + File.separator + "main" + File.separator + "resources" + File.separator + baseName.replace('/', File.separatorChar) + ".png";

        // 先尝试从源码目录读取（开发时）
        try {
            File f = new File(this.file_path);
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore, try classpath next
        }

        // 如果没有从文件系统加载到，再尝试 classpath
        if (image == null) {
            try (java.io.InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in != null) {
                    image = ImageIO.read(in);
                }
            } catch (IOException e) {
                // ignore
            }
        }

        if (image != null) {
            width = image.getWidth();
            height = image.getHeight();
        } else {
            width = 0;
            height = 0;
            System.err.println("Pieces: failed to load image for '" + baseName + "' from '" + this.file_path + "' or classpath " + resourcePath);
        }

        // 计算中心点（与旧逻辑兼容）
        this.centerX = 77 * (col - 1) + 39;
        this.centerY = 77 * (row - 1) + 55;
        // 计算绘制用左上角坐标
        this.drawX = this.centerX - width / 2;
        this.drawY = this.centerY - height / 2;
    }

    // 绘制方法：在棋盘的 paintComponent 中由外部传入 Graphics 调用
    public void paint(Graphics g) {
        if (image != null && g != null) {
            g.drawImage(image, drawX, drawY, null);
        }
    }

}
