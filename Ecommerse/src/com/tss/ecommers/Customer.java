package com.tss.ecommers;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private int id;
	private String name;
	private String address;
	private List<Order> orders;

	public Customer(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.orders = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(Order order) {
		if (order != null) {
			this.orders.add(order);
		}
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", address='" + address + '\''
				+ ", numberOfOrders=" + orders.size() + '}';
	}
}
