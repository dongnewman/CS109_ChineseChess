package com.GUI.GameObjects.Box;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RedBox extends Box {
    public RedBox(int col, int row) {
        super();
        final String fsPath = "src\\main\\resources\\Box\\RedBox.png"; // 开发时的文件系统路径
        final String resPath = "/Box/RedBox.png"; // 类路径下的资源路径（jar 中应使用此路径）

        // 优先使用类路径（在 jar 内有效），若失败再回退到源码树下的文件系统路径（便于开发时运行）
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
            System.err.println("RedBox: failed to load image from '" + file_path + "'.");
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
