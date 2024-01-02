package com.guz.spring.files.upload.db.interfaces.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.guz.spring.files.upload.db.model.NoteModel;
import com.guz.spring.files.upload.db.model.http.RequestNoteModel;

public interface NoteRepository extends JpaRepository<NoteModel, Long> {
	public NoteModel findByTitle(NoteModel noteModel);

	public List<RequestNoteModel> findAllByTitle(String title, Pageable page);
}
