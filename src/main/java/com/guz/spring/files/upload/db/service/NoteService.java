package com.guz.spring.files.upload.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guz.spring.files.upload.db.interfaces.repository.NoteRepository;
import com.guz.spring.files.upload.db.model.NoteModel;

@Service
public class NoteService {
	private final Logger logger = LoggerFactory.getLogger(NoteService.class);

	@Autowired
	private NoteRepository noteRepository;

	public NoteModel createNote(NoteModel entity) {
		NoteModel savedEntity = noteRepository.save(entity);
		logger.info(savedEntity.toString());
		return savedEntity;
	}

	public boolean checkIfExistByTitle(NoteModel entity) {
		NoteModel noteModel = noteRepository.findByTitle(entity);
		return noteModel != null;
	}
}
