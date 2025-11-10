    package com.GUI.Box;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RedBox extends Box {
    public RedBox(int col, int row) {
        super();
        this.file_path = "src\\main\\resources\\Box\\RedBox.png";
        try {
            File f = new File(file_path);
            if (f.exists()) {
                this.image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore, try classpath next
        }
        if (this.image == null) {
            try (java.io.InputStream in = getClass().getResourceAsStream(file_path)) {
                if (in != null) {
                    this.image = ImageIO.read(in);
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

    // 判断颜色，黑色返回true
    public boolean getColor() {
        if (file_path != null && file_path.toLowerCase().contains("black")) {
            return true;
        }
        return false;
    }
}
