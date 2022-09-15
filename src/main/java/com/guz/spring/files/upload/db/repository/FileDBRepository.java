package com.guz.spring.files.upload.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guz.spring.files.upload.db.model.Img_DB;

@Repository
public interface FileDBRepository extends JpaRepository<Img_DB, Long>{

}
