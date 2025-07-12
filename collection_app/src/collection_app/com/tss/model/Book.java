package collection_app.com.tss.model;

public class Book {
	
	private String id;
	private String title;
	private String author;
	private boolean isIssued;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isIssued() {
		return isIssued;
	}

	public void setIssued(boolean isIssued) {
		this.isIssued = isIssued;
	}



	public Book(String id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.isIssued = false;
	}

	public String toString() {
		return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Issued: " + (isIssued ? "Yes" : "No");
	}

}
