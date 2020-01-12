package com.dev.springboot.backend.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.springboot.backend.models.dao.IRegionDao;
import com.dev.springboot.backend.models.entity.Region;

@Service
public class RegionService implements IRegionService {

	@Autowired // Annotation of injection
	private IRegionDao regionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegions() {
		return (List<Region>) regionDao.findAll();
	}
	
}
