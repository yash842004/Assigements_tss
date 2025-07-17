package com.tss.fooddelivery.customer;

import java.util.List;
import com.tss.fooddelivery.foodbill.FoodItem;

public class OrderResult {

    private List<FoodItem> orderList;
    private Address address;

    public OrderResult(List<FoodItem> orderList, Address address) {
        this.orderList = orderList;
        this.address = address;
    }

    public List<FoodItem> getOrderList() {
        return orderList;
    }

    public Address getAddress() {
        return address;
    }
}
