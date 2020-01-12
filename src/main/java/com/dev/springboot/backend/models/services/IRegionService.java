package com.dev.springboot.backend.models.services;

import java.util.List;

import com.dev.springboot.backend.models.entity.Region;

public interface IRegionService {

	public List<Region> findAllRegions();
	
}
