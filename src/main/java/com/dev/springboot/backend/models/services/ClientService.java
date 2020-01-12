package com.dev.springboot.backend.models.services;

import java.util.List;

import com.dev.springboot.backend.models.dao.IClienteDao;
import com.dev.springboot.backend.models.entity.Client;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService implements IClienteService {

	@Autowired // Annotation of injection
	private IClienteDao clientDao;
	
	@Override
	@Transactional(readOnly = true) // Unnecessary
	public List<Client> findAll() {
		return (List<Client>) clientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		return clientDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true) // Unnecessary
	public Client findOne(Long id) {
		return clientDao.findById(id).orElse(null);
	}

	@Override
	@Transactional // Unnecessary
	public Client save(Client client) {
		return clientDao.save(client);
	}

	@Override
	@Transactional // Unnecessary
	public void delete(Long id) {
		clientDao.deleteById(id);
	}
}
