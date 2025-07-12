package com.tss.Model.inheretance.model;


public class BoxWeight extends Box {
    private int weight;

    public BoxWeight(int width, int depth, int height,int weight) {
        super(height, width, weight);
    	this.weight = weight;
    }
    


}
