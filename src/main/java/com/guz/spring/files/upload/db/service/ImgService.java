package com.guz.spring.files.upload.db.service;

import java.io.IOException;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.guz.spring.files.upload.db.model.Img_DB;
import com.guz.spring.files.upload.db.repository.FileDBRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgService {

	String fileName;
	String title_store;
	String description_store;
	Img_DB FileDB;

	@Autowired
	private FileDBRepository fileDBRepository;

	public Img_DB store_data(String title, String description, MultipartFile file) throws IOException {
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		title_store = title;
		description_store = description;
		FileDB = new Img_DB(null, fileName, title_store, description_store, file.getContentType(), file.getBytes(),
				null, null);
		System.out.println(
				"<<Service.store_data>> Filename: " + fileName + " Title_store: " + title_store + " description_store: "
						+ description_store + " Type: " + file.getContentType() + "byte: " + file.getBytes());
		return fileDBRepository.save(FileDB);
	}

	public Img_DB store_edit(Long id, String title, String description, MultipartFile file) throws IOException {
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Long id_file = id;
		String title_update = title;
		String description_update = description;
		Img_DB FileDB = new Img_DB(id_file, fileName, title_update, description_update, file.getContentType(),
				file.getBytes(), null, null);
		System.out.println("<Backend.FileStorageService.store_edit> Filename: " + fileName + " Title: " + title_update
				+ " description: " + description_update + " Type: " + file.getContentType());
		return fileDBRepository.save(FileDB);
	}

	public Img_DB getFile(Long id) {
		return fileDBRepository.findById(id).get();
	}

	public Stream<Img_DB> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}

	public Boolean deleteImage(Long id) {
		try {
			fileDBRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}

}
