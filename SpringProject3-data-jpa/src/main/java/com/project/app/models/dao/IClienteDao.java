package com.project.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{

}
