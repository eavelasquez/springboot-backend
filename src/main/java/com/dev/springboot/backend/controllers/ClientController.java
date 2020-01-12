package com.dev.springboot.backend.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

// Annotation RestController
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// Decorator ResponseStatus
// import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
// import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dev.springboot.backend.models.entity.Client;
import com.dev.springboot.backend.models.services.IClienteService;
import com.dev.springboot.backend.models.services.IUploadService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClientController {
	
	@Autowired
	private IClienteService clientService;
	
	@Autowired
	private IUploadService uploadService;
		
	@GetMapping("/clients")
	public List<Client> index() {
		return clientService.findAll();
	}
	
	@GetMapping("/clients/page/{page}")
	public Page<Client> index(@PathVariable Integer page) {
		// Pageable pageable = PageRequest.of(page, 2);
		return clientService.findAll(PageRequest.of(page, 2));
	}
	
	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Client client = new Client();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			client = clientService.findOne(id);
			if (client == null) {
				response.put("message", "The client ID: ".concat(id.toString()).concat(" not found."));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("message", "An error occurred while trying to search in the database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	@PostMapping("/clients")
	public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
		Map<String, Object> response = new HashMap<String, Object>();
		Client newClient = new Client();
		/** Fields validation - One form */
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			for (FieldError err: result.getFieldErrors()) {
				errors.add("The field '" + err.getField() + "' " + err.getDefaultMessage());
			}
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newClient = clientService.save(client);
		} catch (DataAccessException e) {
			response.put("message", "An error occurred while trying to insert client in the database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Successfully registered new client");
		response.put("client", newClient);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
		Client clientFound = new Client();
		Client clientUpdated = new Client();
		Map<String, Object> response = new HashMap<String, Object>();
		/** Fields validation - Two form */
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "The field '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clientFound = clientService.findOne(id);
			if (clientFound == null) {
				response.put("message", "Error updating, the client ID: ".concat(id.toString().concat(" not found.")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			clientFound.setName(client.getName());
			clientFound.setSurname(client.getSurname());
			clientFound.setEmail(client.getEmail());
			clientFound.setRegion(client.getRegion());
			// Update client
			clientUpdated = clientService.save(clientFound);
		} catch (DataAccessException e) {
			response.put("message", "An error occurred while trying to update client in the database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		}
		response.put("message", "Successfully updated the client information.");
		response.put("client", clientUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/clients/{id}")
	// @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Client client = new Client();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			client = clientService.findOne(id);
			if (client == null) {
				response.put("message", "The client ID: ".concat(id.toString()).concat(" not found."));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			// Delete file in server folder
			String previousFileName = client.getImg();
			uploadService.delete(previousFileName);
			
			clientService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "An error occurred while trying to delete client in the database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Successfully deleted client.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/clients/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		Client client = new Client();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			client = clientService.findOne(id);
			if (client == null) {
				response.put("message", "The client ID: ".concat(id.toString()).concat(" not found."));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			if (!file.isEmpty()) {
				String fileName = uploadService.copy(file);
				
				// Delete file in server folder
				String previousFileName = client.getImg();
				uploadService.delete(previousFileName);
				
				// Update client with image
				client.setImg(fileName);
				clientService.save(client);
				
				// Response
				response.put("client", client);
				response.put("message", "Successfully uploaded image: ".concat(fileName));
			} else {
				response.put("message", "Unable to upload image");
			}
		} catch (DataAccessException e) {
			response.put("message", "An error occurred while trying to search in the database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			response.put("message", "An error occurred while trying to upload image.");
			response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/uploads/img/{fileName:.+}")
	public ResponseEntity<Resource> showImage(@PathVariable String fileName) {
		Resource resource = null;
		try {
			resource = uploadService.load(fileName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
}
