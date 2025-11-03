// package com.GUI.Piece;

// import java.awt.Graphics;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;
// import javax.swing.JFrame;

// public class Black_Jiang {
//     private final String file_path = "src\\main\\resources\\piece-black-jiang.png";
//     private BufferedImage image;
//     // 中心坐标（以像素为单位）
//     private int centerX;
//     private int centerY;
//     // 用于绘制的左上角坐标（计算自中心点和图片尺寸）
//     private int drawX;
//     private int drawY;
//     private int width;
//     private int height;
//     public Black_Jiang(JFrame gameframe) {
//         // 先尝试从磁盘路径加载图片（开发时）
//         try {
//             image = ImageIO.read(new File(file_path));
//         } catch (IOException e) {
//             // 忽略，稍后尝试从 classpath 加载
//         }

//         // 如果文件加载失败，尝试从 classpath 读取（打包后可用）
//         if (image == null) {
//             try (java.io.InputStream in = getClass().getResourceAsStream("/piece-black-jiang.png")) {
//                 if (in != null) {
//                     image = ImageIO.read(in);
//                 }
//             } catch (IOException e) {
//                 // ignore
//             }
//         }

//         if (image != null) {
//             width = image.getWidth();
//             height = image.getHeight();
//         } else {
//             width = 0;
//             height = 0;
//             System.err.println("Black_Jiang: failed to load image from '" + file_path + "' or classpath /piece-black-jiang.png");
//         }

//         // 默认中心放置在 (348, 56)
//         this.centerX = 348;
//         this.centerY = 56;
//         // 计算绘制用左上角坐标
//         this.drawX = this.centerX - width / 2;
//         this.drawY = this.centerY - height / 2;
//     }
//     /**
//      * 在指定的 Graphics 上绘制该棋子（基于已计算的 drawX/drawY）。
//      */
//     public void paint(Graphics g) {
//         if (image != null && g != null) {
//             g.drawImage(image, drawX, drawY, null);
//         }
//     }

//     // 一些访问方法，供外部布局或调试使用
//     public int getCenterX() { return centerX; }
//     public int getCenterY() { return centerY; }
//     public int getDrawX() { return drawX; }
//     public int getDrawY() { return drawY; }
//     public int getWidth() { return width; }
//     public int getHeight() { return height; }
//     public BufferedImage getImage() { return image; }




// }
