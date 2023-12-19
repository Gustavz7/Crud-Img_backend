package com.guz.spring.files.upload.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class UploadFileUtils {
	// the max value is recovered from the application.properties file
	@Value("${spring.servlet.multipart.max-file-size}")
	private static String maxFileSize;

	private UploadFileUtils() {
	}

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	public static boolean validateExtension(String extension) {
		String pattern = extension == null ? "" : extension;
		return pattern.matches("(?i)image/jpg|image/jpeg|image/png|image/gif");
	}

	public static boolean validateSize(long size) {
		// by default 20MB
		long maxSize = 20000L;
		try {
			maxSize = Long.parseLong(maxFileSize);
		} catch (Exception e) {
			logger.error("No se pudo obtener el tamaño definido en properties, usando {} bytes por defecto", maxSize);
		}
		return (size > maxSize);
	}
}
