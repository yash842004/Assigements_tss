package com.tss.Model.inheretanceTest;

import com.tss.Model.inheretance.model.BoxWeight;

public class BoxTest {

    public static void main(String[] args) {
        BoxWeight boxWeight = new BoxWeight(12,13,43,54);

//        boxWeight.initialize(12,10, 20, 30);       
//        boxWeight.initializeBoxWeight(40);      
        boxWeight.displayBox();                 
    }
}
