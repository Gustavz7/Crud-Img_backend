package com.guz.spring.files.upload.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.guz.spring.files.upload.db.interfaces.repository.FileJpaRepository;
import com.guz.spring.files.upload.db.message.ResponseFile;
import com.guz.spring.files.upload.db.model.FileModel;
import com.guz.spring.files.upload.db.utils.AsyncRequestUtils;

@SpringBootApplication
public class SpringBootCrudImagenesApplication {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCrudImagenesApplication.class, args);
	}

	@Bean
	CommandLineRunner initDemo(FileJpaRepository repository) {
		return args -> {
			// save a few images
			ResponseFile file = AsyncRequestUtils.getResponsedFile("https://s6.imgcdn.dev/fuwug.md.jpg");
			FileModel result = repository.save(new FileModel(null, file.getName(), file.getTitle(), "no-description", file.getType(), file.getFile(), null, null));
			log.info("InitDemo - registros iniciales insertados ==> " + result.toString());
		};
	}

}
