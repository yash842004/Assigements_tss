package com.tss.model;

public class Feedback {
	private String name;
	private String date;
	private int sessionContent;
	private int queryResolution;
	private int interactivity;
	private int impactfulLearning;
	private int contentDelivery;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSessionContent() {
		return sessionContent;
	}

	public void setSessionContent(int sessionContent) {
		this.sessionContent = sessionContent;
	}

	public int getQueryResolution() {
		return queryResolution;
	}

	public void setQueryResolution(int queryResolution) {
		this.queryResolution = queryResolution;
	}

	public int getInteractivity() {
		return interactivity;
	}

	public void setInteractivity(int interactivity) {
		this.interactivity = interactivity;
	}

	public int getImpactfulLearning() {
		return impactfulLearning;
	}

	public void setImpactfulLearning(int impactfulLearning) {
		this.impactfulLearning = impactfulLearning;
	}

	public int getContentDelivery() {
		return contentDelivery;
	}

	public void setContentDelivery(int contentDelivery) {
		this.contentDelivery = contentDelivery;
	}
}
