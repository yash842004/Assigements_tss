package com.tss.Model;

public class Book {
	private int bookId;
	private String name;
	private String author;
	private String publication;
	private int price;
	
	
	
	public Book(){
		bookId = 01;
		name = "No name";
		author = "naaa";
		publication = "naa";
		price = 00;
		
	}
	
	public Book(int bookId, String name, String author, String publication, int price) {
		
	}
	
	

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getBookId() {
		return bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	
	public  int   discountedPrice() {
		int finalPrice = (price -(price *10/100));
		return finalPrice;
	}
	
	public void display() {
		System.out.println("The Id of Book is: "+getBookId());
		System.out.println("The Name of Book is: "+getName());
		System.out.println("The Author of Book is: "+getAuthor());
		System.out.println("The Publication of Book is: "+getPublication());
		System.out.println("The Price of Book is: "+getPrice());
		System.out.println("The Discounted price of Book is: "+discountedPrice());


		
	}

}
