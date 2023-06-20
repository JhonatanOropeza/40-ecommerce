package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Producto;

@Repository //Para poder inyectar en el service
public interface ProductoRepository extends JpaRepository<Producto, Integer>{//A que tabla y por medio delID

}
