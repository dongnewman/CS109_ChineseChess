    package com.GUI.Box;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RedBox extends Box {
    private static final int CELL_SIZE = 77;
    private static final int OFFSET_X = 39;
    private static final int OFFSET_Y = 55;

    private static final String BASE_NAME = "Box/RedBox";
    private String file_path = "src\\main\\resources\\Box\\RedBox.png";
    private BufferedImage image;
    private int centerX;
    private int centerY;
    private int drawX;
    private int drawY;
    private int width;
    private int height;

    public RedBox(int col, int row) {
        try {
            File f = new File(file_path);
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore, try classpath next
        }
        if (image == null) {
            try (java.io.InputStream in = getClass().getResourceAsStream(file_path)) {
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
            System.err.println("BlueBox: failed to load image from '" + file_path + "'.");
        }
        setBoardPositionInternal(col, row);
        // 构造后自动paint（需有Graphics对象）
        // 如果有全局画布可用，可在此处调用 paint(g)
        // 例如：paint(GlobalGraphicsContext.get());
        // 如无则略过，实际项目建议在外部容器paintComponent中统一调用
    }

    public void paint(Graphics g) {
        if (image != null && g != null) {
            g.drawImage(image, drawX, drawY, null);
        }
    }

    public void setCenterPosition(int x, int y) {
        this.centerX = x;
        this.centerY = y;
        updateDrawCoordinates();
    }

    public void setBoardPosition(int col, int row) {
        setBoardPositionInternal(col, row);
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    private void updateDrawCoordinates() {
        this.drawX = centerX - width / 2;
        this.drawY = centerY - height / 2;
    }

    private void setBoardPositionInternal(int col, int row) {
        this.centerX = columnToX(col);
        this.centerY = rowToY(row);
        updateDrawCoordinates();
    }

    public static int columnToX(int column) {
        return (column - 1) * CELL_SIZE + OFFSET_X;
    }

    public static int rowToY(int row) {
        return (row - 1) * CELL_SIZE + OFFSET_Y;
    }

    // 判断颜色，黑色返回true
    public boolean getColor() {
        // 约定：图片名或路径包含"black"即为黑色
        if (file_path != null && file_path.toLowerCase().contains("black")) {
            return true;
        }
        return false;
    }
}
