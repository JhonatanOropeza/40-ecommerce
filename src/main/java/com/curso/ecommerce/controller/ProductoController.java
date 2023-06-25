package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	//Para realizar pruebas:
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	//Para acceder a la base de datos y otras acciones:
	@Autowired
	private ProductoService productoService;
	@Autowired
	//Para le procesamiento de las imagenes
	private UploadFileService upload;
	@GetMapping
	public String show(Model model) {//El model para llevar 
		model.addAttribute("productos", productoService.findAll());//productos es lo que se envia al ForntEnd
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")				//se trae desde el atributo "img"
	public String save(Producto producto, @RequestParam ("img") MultipartFile file) throws IOException {
		LOGGER.info("Info para guardar producto{}", producto);
		Usuario u = new Usuario(1,"","","","","","","");
		producto.setUsuario(u);
		//Para guardar la imagen
		//Validación cuando el producto es cargado por primera vez y nunca habŕa imagen
		if(producto.getId()==null) {
			String nombreImagen = upload.saveImage(file);
			//Guardar en el campo el nombre de la imagen
			producto.setImagen(nombreImagen);
		}else{//En caso de que se edite un producto
			/* if(file.isEmpty()) {
				Producto p = new Producto();
				p = productoService.get(producto.getId()).get();
				producto.setImagen(p.getImagen());
			}else {			
				String nombreImagen = upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}*/
		}
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);//Donde se guarda el resultado de la búsqueda
		producto = optionalProducto.get();
		LOGGER.info("Producto buscado: {}", producto);
		//Para pasar la información a la vista y mostrarla con elementos HTML
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam ("img") MultipartFile file) throws IOException{
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		if(file.isEmpty()) {//Editamos la imagen pero no cambiamos la imagen
			producto.setImagen(p.getImagen());
		}else {//Cuando se edita también la imagen
			//Eliminar la imagen anterior
			if(!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}			
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos"; 
	}
	
	@GetMapping("/delete/{id}")
	public String edit(@PathVariable Integer id) {
		Producto p = new Producto();
		p= productoService.get(id).get();
		//Eliminar cuando no sea la imagen por defecto
		if(!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";
	}

}
