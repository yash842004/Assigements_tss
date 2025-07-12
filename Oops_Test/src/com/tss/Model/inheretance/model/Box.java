package com.tss.Model.inheretance.model;

public class Box {
    protected int height;
    protected int width;
    protected int length;

    public Box(int height, int width, int length) {
        this.height = height;
        this.width = width;
        this.length = length;
    }
    
    public Box() {
    }

    public void displayBox() {
        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        System.out.println("Length: " + length);
    }
}
