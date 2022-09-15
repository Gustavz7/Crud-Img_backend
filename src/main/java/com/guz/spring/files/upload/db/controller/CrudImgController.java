package com.guz.spring.files.upload.db.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.guz.spring.files.upload.db.message.ResponseText;
import com.guz.spring.files.upload.db.model.Img_DB;
import com.guz.spring.files.upload.db.service.ImgService;

/*
 * 
 * 
 * 
 * */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class CrudImgController {
	String message = "";
	String fileExtension = "";
	@Autowired
	private ImgService imgService;

	// Esta ruta permite subir archivos, el backend genera algunos datos
	// adicionales, segun el modelo ResponseText
	// ademas realiza una validacion RegEx para tipos de archivos
	@PostMapping("api/upload")
	public ResponseEntity<?> uploadFile_test(@ModelAttribute ResponseText model) {
		System.out.println("uploadFile_test()");
		MultipartFile file = model.getImage();
		if (file.getContentType().matches("(?i)image/jpg|image/jpeg|image/png|image/gif")) {
			try {
				imgService.store_data(model.getTitle(), model.getDescription(), model.getImage());
				message = "<BackEnd> Archivo subido correctamente: " + model.getImage().getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "<BackEnd> No se pudo subir el archivo :" + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		} else {
			message = "<BackEnd> Extension no admitida: " + file.getContentType() + " en el archivo: "
					+ file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	// Editar un solo registro (crudRepository hace el trabajo en vez de PUT),
	// recibe el id y los datos para la actualizacion
	@PostMapping("api/edit/{id}/{file}")
	public ResponseEntity<ResponseMessage> editFile(@PathVariable int id, @ModelAttribute ResponseText file) {
		MultipartFile file_img = file.getImage();
		System.out.println("editFile()");
		Long id_file = (long) id;
		if (file_img.getContentType().matches("(?i)image/jpg|image/jpeg|image/png|image/gif")) {
			try {
				imgService.store_edit(id_file, file.getTitle(), file.getDescription(), file.getImage());
				message = "<BackEnd> Archivo modificado correctamente: " + file_img.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "<BackEnd> No se pudo modificar el archivo :" + file_img.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		} else {
			message = "<BackEnd> Extension no admitida: " + file_img.getContentType() + " en el archivo: "
					+ file_img.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	// ResponseFiles retorna todos los registros en una lista, con los datos
	// nesesarios para renderizar las cartas
	@GetMapping("api/files")
	public ResponseEntity<List<ResponseFiles>> getListFiles() {
		System.out.println("getListFiles()");
		List<ResponseFiles> files = imgService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(dbFile.getId().toString()).toUriString();
			return new ResponseFiles(dbFile.getId(), dbFile.getName(), dbFile.getTitle(), dbFile.getDescription(),
					fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	// devuelve el unico registro, con todos los datos disponibles (resumen)
	@GetMapping("api/files/{id}")
	public ResponseEntity<?> getFile(@PathVariable long id) {
		try {
			System.out.println("getFile()");
			Img_DB imgDB = imgService.getFile(id);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/src/")
					.path(imgDB.getId().toString()).toUriString();
			Long size = (long) imgDB.getData().length;
			ResponseFile responseFile = new ResponseFile(imgDB.getId(), imgDB.getName(), imgDB.getTitle(),
					imgDB.getDescription(), fileDownloadUri, imgDB.getType(), size, imgDB.getCreated_at(),
					imgDB.getUpdated_at());
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imgDB.getName() + "\"")
					.body(responseFile);
		} catch (Exception e) {
			message = "<BackEnd> No se logró encontrar el archivo con id: " + id + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	// Genera la ruta para que el navegador pueda acceder unicamente al recurso URL
	@GetMapping("api/files/src/{id}")
	public ResponseEntity<?> getFileSrc(@PathVariable long id) {

		try {
			Img_DB imgDB = imgService.getFile(id);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imgDB.getName() + "\"")
					.body(imgDB.getData());
		} catch (Exception e) {
			message = "<BackEnd> No se logró encontrar el archivo con id: " + id + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	// Borra un registro mediante su id
	@DeleteMapping(path = "api/delete-img/{id}")
	public ResponseEntity<ResponseMessage> eliminarPorId(@PathVariable("id") Long id) {
		System.out.println("eliminarPorId()");
		boolean ok = this.imgService.deleteImage(id);
		if (ok) {
			message = "Se eliminó el archivo con id: " + id;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} else {
			message = "No se pudo eliminar el archivo con id: " + id;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
	}
}
