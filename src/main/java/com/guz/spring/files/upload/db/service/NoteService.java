package com.guz.spring.files.upload.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.guz.spring.files.upload.db.interfaces.repository.NoteRepository;
import com.guz.spring.files.upload.db.model.NoteModel;

@Service
public class NoteService {
	private final Logger logger = LoggerFactory.getLogger(NoteService.class);

	@Autowired
	private NoteRepository noteRepository;

	public NoteModel createNote(NoteModel entity) {
		return noteRepository.save(entity);
	}

	public boolean checkIfExistByTitle(NoteModel entity) {
		NoteModel noteModel = noteRepository.findByTitle(entity);
		return noteModel != null;
	}

	public Page<NoteModel> getPaging() {
		// 10 as default paging element size
		return getPaging(0, 10);
	}

	public Page<NoteModel> getPaging(int pageNumber, int elements) {
		Pageable firstPageWithTwoElements = PageRequest.of(pageNumber, elements);
		Page<NoteModel> allProducts = null;
		try {
			allProducts = noteRepository.findAll(firstPageWithTwoElements);
		} catch (Exception e) {
			logger.error("error al intentar paginar las notas", e);
		}
		return allProducts;
	}
}
