package com.Model.InGame.playroom;

public class Move {
    private int xi, yi, xf, yf;

    // constructor
    public Move(int xi, int yi, int xf, int yf) {
        this.xi = xi;
        this.yi = yi;
        this.xf = xf;
        this.yf = yf;
    }

    // getters
    public int getxi() {
        return xi;
    }

    public int getyi() {
        return yi;
    }

    public int getxf() {
        return xf;
    }

    public int getyf() {
        return yf;
    }

    // setters
    public void setxi(int xi) {
        this.xi = xi;
    }

    public void setyi(int yi) {
        this.yi = yi;
    }

    public void setxf(int xf) {
        this.xf = xf;
    }

    public void setyf(int yf) {
        this.yf = yf;
    }
}
