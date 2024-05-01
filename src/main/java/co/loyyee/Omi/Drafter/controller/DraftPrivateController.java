package co.loyyee.Omi.Drafter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/drafter/private")
public class DraftPrivateController {
	
	@GetMapping
	public ResponseEntity<String> privateTest(Principal principal) {
		return ResponseEntity.ok("Welcome back " + principal.getName())	;
	}
	
}
