package com.tss.decorater;

public class WheelAlign extends CarServiceDecorator {

    public WheelAlign(ICarService obj) {
        super(obj); 
    }

    @Override
    public double getCost() {
       
        return 400.00 + super.getCost(); 
    }
}