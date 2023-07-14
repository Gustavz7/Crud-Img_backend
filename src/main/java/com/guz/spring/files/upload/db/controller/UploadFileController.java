package com.guz.spring.files.upload.db.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guz.spring.files.upload.db.message.ResponseFile;
import com.guz.spring.files.upload.db.message.ResponseFiles;
import com.guz.spring.files.upload.db.message.ResponseMessage;
import com.guz.spring.files.upload.db.message.MultipartResponse;
import com.guz.spring.files.upload.db.model.FileModel;
import com.guz.spring.files.upload.db.service.ImgService;

import org.slf4j.Logger;

/*
 * 
 * 
 * 
 * */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)

public class UploadFileController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	String message = "";
	String fileExtension = "";
	private FileModel fileDetails;
	ResponseEntity<ResponseFile> response;
	@Autowired
	private ImgService localJpaService;
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	/**
	 * return the specified id file with all available details
	 */
	@GetMapping("api/file/{id}")
	public ResponseEntity<ResponseFile> getFile(@PathVariable long id) {
		logger.info("obteniendo archivo con id: {}", id);

		response = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);

		try {
			fileDetails = localJpaService.getFile(id);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(fileDetails.getId().toString()).toUriString();
			Long size = (long) fileDetails.getData().length;
			ResponseFile responseFile = new ResponseFile(fileDetails.getId(), fileDetails.getName(),
					fileDetails.getTitle(), fileDetails.getDescription(), fileDownloadUri, fileDetails.getType(), size,
					fileDetails.getCreatedAt(), fileDetails.getUpdatedAt());
			response = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetails.getName() + "\"")
					.body(responseFile);
		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion al buscar el archivo con id: {}", id);
		}
		return response;
	}

	/**
	 * returns the direct URL source route for direct access file
	 */
	@GetMapping("api/file/src/{id}")
	public ResponseEntity<byte[]> getFileUrlSource(@PathVariable long id) {
		ResponseEntity<byte[]> fileSrc = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		try {
			fileDetails = localJpaService.getFile(id);
			fileSrc = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetails.getName() + "\"")
					.body(fileDetails.getData());
		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion al generar URL para el archivo con id: {}", id);
		}
		return fileSrc;
	}

	@PostMapping("api/file/save")
	public ResponseEntity<String> saveFile(@ModelAttribute MultipartResponse model) {

		logger.info("Guardado archivo: {}", model.getImage().getOriginalFilename());
		ResponseEntity<String> saveResponse;
		MultipartFile file = model.getImage();

		if (validateExtension(file.getContentType()) && validateSize(file.getSize())) {
			try {
				localJpaService.store_data(model.getTitle(), model.getDescription(), model.getImage());
				message = "Archivo subido correctamente: " + model.getImage().getOriginalFilename();
				saveResponse = ResponseEntity.status(HttpStatus.OK).body(message);
				logger.info(message);
			} catch (Exception e) {
				message = "No se pudo subir el archivo :" + file.getOriginalFilename() + "!";
				saveResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
				logger.error(message);
			}
		} else {
			message = "Extension no admitida: " + file.getContentType() + " en el archivo: "
					+ file.getOriginalFilename();
			saveResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
			logger.warn(message);
		}
		return saveResponse;
	}

	@PostMapping("api/file/edit/{fileId}/{multipartResponse}")
	public ResponseEntity<ResponseMessage> updateFile(@PathVariable int fileId,
			@ModelAttribute MultipartResponse multipartResponse) {
		MultipartFile file = multipartResponse.getImage();

		ResponseEntity<ResponseMessage> updateResponse;
		if (validateExtension(file.getContentType()) && validateSize(file.getSize())) {
			try {
				localJpaService.store_edit((long) fileId, multipartResponse.getTitle(),
						multipartResponse.getDescription(), multipartResponse.getImage());
				message = "Archivo modificado correctamente: " + file.getOriginalFilename();
				updateResponse = ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
				logger.info(message);
			} catch (Exception e) {
				message = "No se pudo modificar el archivo :" + file.getOriginalFilename() + "!";
				updateResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
						.body(new ResponseMessage(message));
				logger.error(message);
			}
		} else {
			message = "Extension no admitida: " + file.getContentType() + " en el archivo: "
					+ file.getOriginalFilename();
			updateResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			logger.warn(message);
		}
		return updateResponse;
	}

	@GetMapping("api/file/all")
	public ResponseEntity<List<ResponseFiles>> getPaginatedFiles() {
		logger.info("Obteniendo todos los archivos disponibles...");
		List<ResponseFiles> files = localJpaService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(dbFile.getId().toString()).toUriString();
			return new ResponseFiles(dbFile.getId(), dbFile.getName(), dbFile.getTitle(), dbFile.getDescription(),
					fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());
		logger.info("Archivos encontrados: {}", files.size());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@DeleteMapping(path = "api/file/delete/{id}")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("id") Long id) {
		ResponseEntity<ResponseMessage> deleteResponse;
		boolean ok = this.localJpaService.deleteImage(id);
		if (ok) {
			message = "Se eliminó el archivo con id: " + id;
			deleteResponse = ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			logger.info(message);
		} else {
			message = "No se pudo eliminar el archivo con id: " + id;
			deleteResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			logger.warn(message);
		}
		return deleteResponse;
	}

	private boolean validateExtension(String extension) {
		String pattern = extension == null ? "" : extension;
		return pattern.matches("(?i)image/jpg|image/jpeg|image/png|image/gif");
	}

	private boolean validateSize(long size) {

		// by default 20MB
		Long maxSize = 20000L;

		// the max value is recovered from the application.properties file
		try {
			maxSize = Long.parseLong(maxFileSize);
		} catch (Exception e) {
			logger.error("No se pudo obtener el tamaño definido en properties, usando {} bytes por defecto", maxSize);
		}
		return (size > maxSize);
	}
}
