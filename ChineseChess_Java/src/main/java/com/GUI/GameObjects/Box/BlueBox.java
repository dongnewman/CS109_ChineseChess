package com.GUI.GameObjects.Box;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BlueBox extends Box {
    public BlueBox(int col, int row) {
        super();
        final String fsPath = "src\\main\\resources\\Box\\BlueBox.png"; // 开发时文件系统路径
        final String resPath = "/Box/BlueBox.png"; // 类路径资源路径（用于 jar）

        try (java.io.InputStream in = getClass().getResourceAsStream(resPath)) {
            if (in != null) {
                this.image = ImageIO.read(in);
            }
        } catch (IOException e) {
            // ignore, try filesystem next
        }
        if (this.image == null) {
            try {
                File f = new File(fsPath);
                if (f.exists()) {
                    this.image = ImageIO.read(f);
                }
            } catch (IOException e) {
                // ignore
            }
        }
        if (this.image != null) {
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
        } else {
            this.width = 0;
            this.height = 0;
            System.err.println("BlueBox: failed to load image from '" + file_path + "'.");
        }
        setBoardPositionInternal(col, row);
    }

    public void paint(Graphics g) {
        if (this.image != null && g != null) {
            g.drawImage(this.image, this.drawX, this.drawY, null);
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
}
