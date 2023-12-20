package com.guz.spring.files.upload.db.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guz.spring.files.upload.db.model.NoteModel;

public interface NoteRepository extends JpaRepository<NoteModel, Long> {
	public NoteModel findByTitle(NoteModel noteModel);
}
