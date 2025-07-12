package com.tss.furniture;

public class ModernFurnitureFactory implements IFurnitureFactory {

	public IFurniture createTable() {
		return new ModernTable();
	}

	@Override
	public IFurniture createChair() {
		return new ModernChair();
	}

	@Override
	public IFurniture createSofa() {
		return new ModernSofa();
	}

}
