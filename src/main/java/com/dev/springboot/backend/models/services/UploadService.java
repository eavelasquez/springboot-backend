package com.dev.springboot.backend.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService implements IUploadService {

	private final Logger logger = LoggerFactory.getLogger(UploadService.class);
	private final static String UPLOAD_FOLDER = "uploads";

	@Override
	public Resource load(String fileName) throws MalformedURLException {
		Path filePath = getPath(fileName);
		logger.info("File path: " + filePath.toString());

		Resource resource = new UrlResource(filePath.toUri());
		if (!resource.exists() && !resource.isReadable()) {
			filePath = Paths.get("images").resolve(fileName).toAbsolutePath();
			resource = new UrlResource(filePath.toUri());
			logger.error("Unable to get image: ".concat(fileName));
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID().toString().concat("_").concat(file.getOriginalFilename()).replace(" ", "");

		Path filePath = getPath(fileName);
		logger.info(filePath.toString());

		// Save file in server folder
		Files.copy(file.getInputStream(), filePath);
		return fileName;
	}

	@Override
	public boolean delete(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			Path previousFilePath = getPath(fileName);
			File previousImageFile = previousFilePath.toFile();
			if (previousImageFile.exists() && previousImageFile.canRead()) {
				previousImageFile.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String fileName) {
		return Paths.get(UPLOAD_FOLDER).resolve(fileName).toAbsolutePath();
	}

}
