package com.guz.spring.files.upload.db.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.guz.spring.files.upload.db.utils.UploadFileUtils;

@Entity
@Table(name = "FileModel")
public class FileModel {

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
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;

	public FileModel() {
	}

	public FileModel(Long id, String name, String title, String description, String type, byte[] data, Date createdAt,
			Date updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.description = description;
		this.type = type;
		this.data = data;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getSimpleCreatedAt() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		return formatter.format(createdAt);
	}

	public String getSimpleUpdatedAt() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		return formatter.format(updatedAt);
	}

	public String toString() {
		return String.format(
				"FileModel[id=%d, name='%s', title='%s', description='%s', type='%s', data=%s, createdAt='%s', updatedAt='%s']",
				id, name, title, description, type, UploadFileUtils.formatFileSizeString(data.length),
				getSimpleCreatedAt(), getSimpleUpdatedAt());
	}

}
