package com.guz.spring.files.upload.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadFileUtils {

	private UploadFileUtils() {
	}

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	public static boolean validateExtension(String extension) {
		String pattern = extension == null ? "" : extension;
		return pattern.matches("(?i)image/jpg|image/jpeg|image/png|image/gif");
	}

	public static boolean validateSize(long size, String maxFileSize) {

		// by default 20MB
		Long maxSize = 20000L;

		// the max value is recovered from the application.properties file
		try {
			maxSize = Long.parseLong(maxFileSize);
			// .maxSize..parseFileSize(maxFileSize);
			// FilenameUtils.get
		} catch (Exception e) {
			logger.error("No se pudo obtener el tamaño definido en properties, usando {} bytes por defecto", maxSize);
		}
		return (size > maxSize);
	}
}
