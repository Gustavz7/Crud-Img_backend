package com.guz.spring.files.upload.db.model.http;

public class RequestNoteModel {
	private String title;
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RequestNoteModel [title=" + title + ", description=" + description + "]";
	}
}
