package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.ProductoRepository;

@Service //Para inyectar en el controlador
public class ProductoServiceImpl implements ProductoService {
	//Para inyectar un objeto a esta clase:
	@Autowired
	private ProductoRepository productoRepository;//Objeto creado para el CRUD
	
	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productoRepository.save(producto);		
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}
}