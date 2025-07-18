package com.tss.fooddelivery.foodpatner;


public class DeliveryPartner  {


    private int id;
    private String name;

    public DeliveryPartner(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void displayDetails() {
        System.out.println("ID: " + id + ", Name: " + name);
    }
}
