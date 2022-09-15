package com.guz.spring.files.upload.db.message;

import org.springframework.web.multipart.MultipartFile;

//esta clase representa la estructura de la respuesta http y todos los campos del body
public class ResponseText {

	private String title;
	private String description;
	private MultipartFile image;

	public ResponseText(String title, String description, MultipartFile image) {

		this.title = title;
		this.description = description;
		this.image = image;
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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

}
