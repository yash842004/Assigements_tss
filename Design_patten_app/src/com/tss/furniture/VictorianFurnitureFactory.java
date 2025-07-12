package com.tss.furniture;

public class VictorianFurnitureFactory implements IFurnitureFactory {

	@Override
	public IFurniture createTable() {
		return new VictorianTable();
	}

	@Override
	public IFurniture createChair() {
		return new VictorianChair();
	}

	@Override
	public IFurniture createSofa() {
		return new VictorianSofa();
	}

}
