package com.guz.spring.files.upload.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guz.spring.files.upload.db.model.FileModel;

@Repository
public interface FileJpaRepository extends JpaRepository<FileModel, Long>{

}
