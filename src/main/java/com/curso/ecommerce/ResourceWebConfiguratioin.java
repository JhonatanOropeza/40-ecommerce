package com.curso.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//Clase que nos permitira apuntar hacia los recursos de imagenes
//de manera reactivo, con solo apuntar a esa URL
@Configuration
public class ResourceWebConfiguratioin implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//Apuntando al directorio para mostrar las imagenes
		registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
	}
}
