package com.guz.spring.files.upload.db.controller.rest;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guz.spring.files.upload.db.model.NoteModel;
import com.guz.spring.files.upload.db.model.http.RequestNoteModel;
import com.guz.spring.files.upload.db.service.NoteService;

@RestController
@RequestMapping("/api")
public class NoteController {

	@Autowired
	NoteService noteService;

	@PostMapping("/note/new")
	public ResponseEntity<String> saveNote(@ModelAttribute RequestNoteModel requestNoteModel) {
		ResponseEntity<String> responseEntity;
		responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se han proporcionado datos");

		if (requestNoteModel == null) {
			return responseEntity;
		}

		NoteModel noteModel = new NoteModel(requestNoteModel.getTitle(), requestNoteModel.getDescription());
		noteModel = noteService.createNote(noteModel);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		responseEntity = ResponseEntity.status(HttpStatus.CREATED).body("Guardado correcto - "
				+ sdf.format(noteModel.getCreatedAt().getTime()) + " - id: " + noteModel.getId());

		return responseEntity;
	}

	@GetMapping("/note/checkExist")
	public ResponseEntity<String> checkNoteByTitle(@ModelAttribute RequestNoteModel requestNoteModel) {
		ResponseEntity<String> responseEntity;
		responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se han proporcionado datos");

		if (requestNoteModel == null) {
			return responseEntity;
		}
		NoteModel noteModel = new NoteModel(requestNoteModel.getTitle(), "");

		String result = noteService.checkIfExistByTitle(noteModel) ? "true" : "false";
		responseEntity = ResponseEntity.status(HttpStatus.OK).body(result);

		return responseEntity;
	}

}
