package com.guz.spring.files.upload.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guz.spring.files.upload.db.model.FileModel;

public interface FileJpaRepository extends JpaRepository<FileModel, Long> {

}
