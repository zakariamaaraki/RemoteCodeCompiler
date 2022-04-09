package com.cp.compiler.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class IndexController {
	
	@ApiOperation(value = "Redirect to the index page", hidden = true)
	@GetMapping("/")
	public RedirectView indexPage(RedirectAttributes attributes) {
		return new RedirectView("/swagger-ui.html");
	}
}
