package com.guz.spring.files.upload.db.message;

import org.springframework.web.multipart.MultipartFile;

//esta clase representa la estructura de la respuesta http y todos los campos del body
public class MultipartResponse {

	private String title;
	private String description;
	private MultipartFile file;

	public MultipartResponse(String title, String description, MultipartFile file) {
		super();
		this.title = title;
		this.description = description;
		this.file = file;
	}

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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
