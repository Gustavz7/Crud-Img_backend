package com.guz.spring.files.upload.db.service.dao;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.guz.spring.files.upload.db.model.FileModel;
import com.guz.spring.files.upload.db.repository.FileJpaRepository;

@Service
public class FileDAOService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	String fileName;
	String reqTitle;
	String reqDescription;
	FileModel fileModel;

	@Autowired
	private FileJpaRepository fileDBRepository;

	/**
	 * this function can save and update registers, only must be declared the id if you want to
	 * update an register*/
	public FileModel saveEntity(Long id, String title, String description, MultipartFile file) {
		FileModel result = null;
		fileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "none");
		try {
			fileModel = new FileModel(id, fileName, title, description, file.getContentType(), file.getBytes(),
					null, null);
			result = fileDBRepository.save(fileModel);
		} catch (Exception e) {
			logger.error("error al guardar: ", e);
		}
		
		return result;
		
	}

	public FileModel getFile(Long id) {
		try {
			fileModel = fileDBRepository.findById(id).orElse(null);
		} catch (IllegalArgumentException e) {
			logger.error("Se debe indicar un id valido o no nulo para obtener el archivo", e);
		}
		return fileModel;
	}

	public Stream<FileModel> getAllFiles() {
		return fileDBRepository.findAll().stream();
	}

	public Boolean deleteImage(@NonNull Long id) {
		boolean result = false;
		try {
			fileDBRepository.deleteById(id);
			result = true;
		} catch (IllegalArgumentException e) {
			logger.error("Se debe indicar un id valido o no nulo para eliminar el archivo", e);
		}catch(EmptyResultDataAccessException e1) {
			logger.error("no se puedo borrar el id proporcinado: {}", id);
		}
		return result;
	}

}
