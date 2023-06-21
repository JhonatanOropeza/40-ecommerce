package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Producto;

//Interface con la defición de los ervicios
public interface ProductoService {
	public Producto save(Producto producto);
	public Optional<Producto> get(Integer id);//Para saber si existe el producto (validarlo)
	public void update(Producto producto);
	public void delete(Integer id);
	public List<Producto> findAll();
}
