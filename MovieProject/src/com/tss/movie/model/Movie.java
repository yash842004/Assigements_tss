package com.tss.movie.model;

import java.io.Serializable;

public class Movie implements Serializable {

	private String movieId;
	private String name;
	private String genre;
	private int year;

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Movie(String movieId, String name, String genre, int year) {
		super();
		this.movieId = movieId;
		this.name = name;
		this.genre = genre;
		this.year = year;
	}

	@Override
	public String toString() {
		return "ID: " + movieId + ", Name: " + name + ", Genre: " + genre + ", Year: " + year;
	}

}
