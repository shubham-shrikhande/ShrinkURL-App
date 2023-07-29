package com.tm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.tm.binding.URLBinding;
import com.tm.service.IShrinkURLService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ShrinkURLRestController {

	@Autowired
	private IShrinkURLService service;

	@PostMapping("/originalUrl")
	public ResponseEntity<String> getOriginalUrl(@RequestBody URLBinding binding) {
		String duplicateUrl = service.getTempUrl(binding);
		return new ResponseEntity<String>(duplicateUrl, HttpStatus.OK);
	}

	@GetMapping("/request/{temp}")
	public RedirectView redirectToOriginalUrl(@PathVariable String temp) {
		String output = service.getOriginalUrl(temp);
		if (output == null) {
			return new RedirectView("http://localhost:8009/ShrinkURL/expired");
		}
		return new RedirectView(output);
	}

	@GetMapping("/expired")
	private ResponseEntity<String> redirectedExpiredRequestMethod() {
		return new ResponseEntity<String>("Sorry!! Link expired. Please create a new one.", HttpStatus.NOT_FOUND);
	}
}
