package com.tss.ecommers; 

public class LineItem {
	private int id;
	private int quantity;
	private Product product; 
	private double unitPrice;

	public LineItem(int id, int quantity, Product product) {
		this.id = id;
		this.quantity = quantity;
		this.product = product; 
		this.unitPrice = product.calculateDiscountedPrice(); 
	}


	public LineItem(int id, String name, int quantity, double unitPrice) {
		this.id = id;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.product = null; 
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public Product getProduct() {
		return product;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product != null) {
			this.unitPrice = product.calculateDiscountedPrice();
		} else {
			this.unitPrice = 0.0; 
		}
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double calculateLineItemCost() {
		return quantity * unitPrice;
	}

	@Override
	public String toString() {
		return "LineItem{" + "id=" + id + ", quantity=" + quantity + ", product="
				+ (product != null ? product.getName() : "N/A") + ", unitPrice=" + String.format("%.2f", unitPrice)
				+ ", totalCost=" + String.format("%.2f", calculateLineItemCost()) + '}';
	}
}