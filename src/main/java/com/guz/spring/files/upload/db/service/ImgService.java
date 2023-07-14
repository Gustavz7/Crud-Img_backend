package com.guz.spring.files.upload.db.service;

import java.io.IOException;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.guz.spring.files.upload.db.model.FileModel;
import com.guz.spring.files.upload.db.repository.FileDBRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgService {

	String fileName;
	String reqTitle;
	String reqDescription;
	FileModel fileModel;

	@Autowired
	private FileDBRepository fileDBRepository;

	public FileModel store_data(String title, String description, MultipartFile file) throws IOException {
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		reqTitle = title;
		reqDescription = description;
		fileModel = new FileModel(null, fileName, reqTitle, reqDescription, file.getContentType(), file.getBytes(),
				null, null);
		System.out.println(
				"<<Service.store_data>> Filename: " + fileName + " Title_store: " + reqTitle + " description_store: "
						+ reqDescription + " Type: " + file.getContentType() + "byte: " + file.getBytes());
		return fileDBRepository.save(fileModel);
	}

	public FileModel store_edit(Long id, String title, String description, MultipartFile file) throws IOException {
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Long id_file = id;
		String title_update = title;
		String description_update = description;
		FileModel FileDB = new FileModel(id_file, fileName, title_update, description_update, file.getContentType(),
				file.getBytes(), null, null);
		System.out.println("<Backend.FileStorageService.store_edit> Filename: " + fileName + " Title: " + title_update
				+ " description: " + description_update + " Type: " + file.getContentType());
		return fileDBRepository.save(FileDB);
	}

	public FileModel getFile(Long id) {
		return fileDBRepository.findById(id).get();
	}

	public Stream<FileModel> getAllFiles() {
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
