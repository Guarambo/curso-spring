package com.project.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.app.models.entity.Cliente;
import com.project.app.models.service.IClienteService;
import com.project.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> m, RedirectAttributes flash) {
		Cliente cl = clienteService.findOne(id);

		if (cl == null) {
			flash.addFlashAttribute("Error", "El cliente no existe en la Base de datos");
			return "redirect:/listar";
		}
		
		m.put("cliente", cl);
		m.put("title", "Informacion del cliente " + cl.getNombre());

		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model m) {

		// PageRequest Deprecrated Pageable pr = new PageRequest(page, 5);
		/* SpringBoot 5 PageRequest */
		Pageable pr = PageRequest.of(page, 5);

		Page<Cliente> listadoClientesPaginado = clienteService.findAll(pr);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", listadoClientesPaginado);
		m.addAttribute("title", "Listado de Clientes");
		m.addAttribute("clients", listadoClientesPaginado);
		m.addAttribute("page", pageRender);
		// m.addAttribute("clients", clienteService.findAll()); listar todos los
		// clientes

		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> m) {

		Cliente cl = new Cliente();

		m.put("cliente", cl);
		m.put("title", "Formulario de Cliente");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cl, BindingResult result, Model m, @RequestParam("file") MultipartFile image,
			RedirectAttributes flash, SessionStatus stat) {

		if (result.hasErrors()) {
			m.addAttribute("title", "Formulario de Cliente");
			return "form";
		}

		if (!image.isEmpty()) {
			Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
			String rootPath = directorioRecursos.toFile().getAbsolutePath();

			try {
				byte[] bytes = image.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + image.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Ha subido correctamente '" + image.getOriginalFilename() + "'");

				cl.setFoto(image.getOriginalFilename());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String mensajeFlash = (cl.getId() != null) ? "El Cliente se edito correctamente"
				: "Cliente creado correctamente";

		clienteService.save(cl);
		stat.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> m, RedirectAttributes flash) {

		Cliente cl = null;

		if (id > 0) {
			cl = clienteService.findOne(id);
			if (cl == null) {
				flash.addFlashAttribute("error", "Error - Id no existe en la base de datos");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "Error - Id incorrecto o nulo");
			return "redirect:/listar";
		}

		m.put("cliente", cl);
		m.put("title", "Editar Cliente");

		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado correctamente");
		}

		return "redirect:/listar";
	}
}
