package com.dev.springboot.backend.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
	
	public Resource load(String fileName) throws MalformedURLException;
	public String copy(MultipartFile file) throws IOException;
	public boolean delete(String fileName);
	public Path getPath(String fileName);

}
