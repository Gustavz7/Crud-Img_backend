package com.guz.spring.files.upload.db.service.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.guz.spring.files.upload.db.interfaces.repository.FileJpaRepository;
import com.guz.spring.files.upload.db.model.FileModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class FileDAOServiceTest {

	@Autowired
	FileJpaRepository fileJpaRepository;

	@Test
	void saveEntityTest() {
		FileModel fileModel = new FileModel();
		fileModel.setName("testTitle");
		fileModel.setDescription("testDescription");
		fileModel.setType("jpg/jpeg");
		fileModel.setData(null);
		FileModel savedFile = fileJpaRepository.save(fileModel);
		assertNotNull(savedFile);
	}

}
