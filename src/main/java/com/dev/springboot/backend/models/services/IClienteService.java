package com.dev.springboot.backend.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.springboot.backend.models.entity.Client;

public interface IClienteService {

	public List<Client> findAll();
	public Page<Client> findAll(Pageable pageable);
	public Client findOne(Long id);
	public Client save(Client client);
	public void delete(Long id);

}
