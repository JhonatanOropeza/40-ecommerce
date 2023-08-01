package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
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
	
	//Objeto para alamacenar los detalles de la orden ESTA LISTA ES GLOBAL
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//Objeto para almacenar los datos de la orden
	Orden orden = new Orden();
	
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
	
	@PostMapping("/cart")													//Para mostrar en pantalla
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		//Buscando el producto en la bd
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto);
		log.info("Cantidad: {}", cantidad); 
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		//Validar que el producto no se añada más de dos veces
		Integer idProducto= producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()== idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Quitar un producto de la vista donde se muestra el carrito de compras.
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		//Lista actualizada de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden: detalles) {
			if(detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		//Actualizar variable global de lista de productos en carrito
		detalles = ordenesNueva;
		
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	//Para obtener el carrito de compras desde cualquier parte de aplicación
	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
}
