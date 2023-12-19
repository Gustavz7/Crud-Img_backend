package com.guz.spring.files.upload.db.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guz.spring.files.upload.db.message.MultipartResponse;
import com.guz.spring.files.upload.db.message.ResponseFile;
import com.guz.spring.files.upload.db.message.ResponseFiles;
import com.guz.spring.files.upload.db.message.ResponseMessage;
import com.guz.spring.files.upload.db.model.FileModel;
import com.guz.spring.files.upload.db.service.dao.FileDAOService;
import com.guz.spring.files.upload.db.utils.UploadFileUtils;

/*
 * 
 * 
 * 
 * */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)

public class UploadFileController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	String message = "";
	String fileExtension = "";
	private FileModel fileDetails;
	ResponseEntity<ResponseFile> response;
	@Autowired
	private FileDAOService localJpaService;

	@GetMapping("api/file/all")
	public ResponseEntity<List<ResponseFiles>> getPaginatedFiles() {
		logger.info("Obteniendo todos los archivos disponibles...");
		List<ResponseFiles> files = localJpaService.getAllFiles().map(fileElement -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(fileElement.getId().toString()).toUriString();
			return new ResponseFiles(fileElement.getId(), fileElement.getName(), fileElement.getTitle(),
					fileElement.getDescription(), fileDownloadUri, fileElement.getType(), fileElement.getData().length);
		}).collect(Collectors.toList());
		logger.info("Archivos encontrados: {}", files.size());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	/**
	 * return the specified id file with all available details
	 */
	@GetMapping("api/file/{id}")
	public ResponseEntity<ResponseFile> getFile(@PathVariable long id) {
		logger.info("obteniendo archivo con id: {}", id);

		response = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);

		try {
			ResponseFile responseFile = new ResponseFile();
			fileDetails = localJpaService.getFile(id);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(fileDetails.getId().toString()).toUriString();
			Long size = (long) fileDetails.getData().length;

			responseFile.setId(fileDetails.getId());
			responseFile.setName(fileDetails.getName());
			responseFile.setTitle(fileDetails.getTitle());
			responseFile.setDescription(fileDetails.getDescription());
			responseFile.setUrl(fileDownloadUri);
			responseFile.setType(fileDetails.getType());
			responseFile.setSize(size);
			responseFile.setCreatedAt(fileDetails.getCreatedAt());
			responseFile.setUpdatedAt(fileDetails.getUpdatedAt());

			response = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetails.getName() + "\"")
					.body(responseFile);
		} catch (NoSuchElementException e1) {
			logger.error("No se encontro el archivo con id: {}", id);
		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion al buscar el archivo con id: {}", id, e);
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
		ResponseEntity<String> saveResponse;
		MultipartFile file = model.getFile();

		if (file == null) {
			saveResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("archivo no adjuntado en el campo [file]");
			return saveResponse;
		}

		if (UploadFileUtils.validateExtension(file.getContentType()) && UploadFileUtils.validateSize(file.getSize())) {
			try {
				logger.info("Guardando archivo: {}", model.getFile().getOriginalFilename());
				localJpaService.saveEntity(null, model.getTitle(), model.getDescription(), model.getFile());
				message = "Archivo subido correctamente: " + model.getFile().getOriginalFilename();
				saveResponse = ResponseEntity.status(HttpStatus.OK).body(message);
				logger.info(message);
			} catch (Exception e) {
				String fileName = file.getOriginalFilename();
				message = "No se pudo subir el archivo :" + fileName + " !";
				logger.error(message);
				saveResponse = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
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
		MultipartFile file = multipartResponse.getFile();

		ResponseEntity<ResponseMessage> updateResponse;
		if (UploadFileUtils.validateExtension(file.getContentType()) && UploadFileUtils.validateSize(file.getSize())) {
			try {
				localJpaService.saveEntity((long) fileId, multipartResponse.getTitle(),
						multipartResponse.getDescription(), multipartResponse.getFile());
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
}
