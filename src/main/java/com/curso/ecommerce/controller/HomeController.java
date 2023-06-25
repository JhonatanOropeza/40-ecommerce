package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	//Para mostrar los productos en esta vista
	
	//Para ver en cosola los datos traidos desde a bd, para ver trasabilidad en el código
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")//Comillas vacias porque responde a la raíz
	public String home(Model model) {
		//Enviando los productos a la vista
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("ID producto enviado como parametro: {}",id);
		Producto producto = new Producto();
		//Para obtener de la bd
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		//Enviando los datos a la vista
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
}
