package com.tss.furniture;

public interface IFurnitureFactory {
    IFurniture createTable();
    IFurniture createChair();
    IFurniture createSofa();

}
