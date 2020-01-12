package com.dev.springboot.backend.models.dao;

import com.dev.springboot.backend.models.entity.Client;
// import com.dev.springboot.backend.models.entity.Region;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;
// import org.springframework.data.jpa.repository.Query;

public interface IClienteDao extends JpaRepository<Client, Long> {
	
	// @Query("from Region")
	// public List<Region> findAllRegions();
	
}
