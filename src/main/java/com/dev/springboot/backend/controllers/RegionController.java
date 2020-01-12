package com.dev.springboot.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.springboot.backend.models.entity.Region;
import com.dev.springboot.backend.models.services.RegionService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class RegionController {

	@Autowired
	private RegionService regionService;
	
	@GetMapping("/regions")
	public ResponseEntity<?> index() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Region> regions = regionService.findAllRegions();

		response.put("regions", regions);
		response.put("message", "Successfully show all regions");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
