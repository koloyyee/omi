package co.loyyee.Omi.Drafter.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drafter/private")
public class DraftPrivateController {

    @GetMapping
    public ResponseEntity<String> privateTest(Principal principal) {
        return ResponseEntity.ok("Welcome back " + principal.getName());
    }

    @GetMapping(path = "/test")
    public String test(Principal principal) {
        return principal.getName();
    }

}
