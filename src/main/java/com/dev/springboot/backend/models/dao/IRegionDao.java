package com.dev.springboot.backend.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.dev.springboot.backend.models.entity.Region;

public interface IRegionDao extends CrudRepository<Region, Long> {

}
