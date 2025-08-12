package com.tss.decorater;

public abstract class CarServiceDecorator implements ICarService {
    protected static ICarService carObj; 

    public CarServiceDecorator(ICarService carObj) {
        this.carObj = carObj;
    }

  
    @Override
    public double getCost() {
        return carObj.getCost();
    }
}
