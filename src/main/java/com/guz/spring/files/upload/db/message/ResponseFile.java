package com.guz.spring.files.upload.db.message;

import java.util.Date;

public class ResponseFile {
	
	private Long id;
	private String name;
	private String title;
	private String description;
	private String url;
	private String type;
	private long size;
	private Date created_at;
	private Date updated_at;
	
	public ResponseFile(Long id, String name, String title, String description, String url, String type, long size,
			Date created_at, Date updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.description = description;
		this.url = url;
		this.type = type;
		this.size = size;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	
	
}
