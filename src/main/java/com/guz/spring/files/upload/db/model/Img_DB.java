package com.guz.spring.files.upload.db.model;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "imagenes")
public class Img_DB {

	@Id
	// @GeneratedValue(generator = "uuid")
	// @GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	@Column
	private String name;
	@Column
	private String title;
	@Column
	private String description;
	@Column
	private String type;
	@Lob
	private byte[] data;
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Date created_at;
	@UpdateTimestamp
	private Date updated_at;

	public Img_DB() {}

	public Img_DB(Long id, String name, String title, String description, String type, byte[] data, Date created_at,
			Date updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.description = description;
		this.type = type;
		this.data = data;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
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
