package com.guz.spring.files.upload.db.message;

//esta clase representa la estructura de la respuesta http y todos los campos del body
public class ResponseFiles {

	private Long id;
	private String name;
	private String title;
	private String description;
	private String url;
	private String type;
	private long size;

	public ResponseFiles(Long id, String name, String title, String description, String url, String type, long size) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.description = description;
		this.url = url;
		this.type = type;
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	/*
	 * public void setId(String id) { this.id = id; }
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
