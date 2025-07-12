package com.tss.ecommers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	private int id;
	private Date date;
	private List<LineItem> items;

	public Order(long l, Date date) {
		this.id = (int) l;
		this.date = date;
		this.items = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public List<LineItem> getItems() {
		return items;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setItems(List<LineItem> items) {
		this.items = items;
	}

	public void addLineItem(LineItem item) {
		if (item != null) {
			this.items.add(item);
		}
	}

	public double calculateOrderPrice() {
		double total = 0;
		for (LineItem item : items) {
			total += item.calculateLineItemCost();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Order{" + "id=" + id + ", date=" + date + ", numberOfItems=" + items.size() + ", totalOrderPrice="
				+ String.format("%.2f", calculateOrderPrice()) + '}';
	}
}
