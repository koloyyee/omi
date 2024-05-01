package co.loyyee.Omi.Drafter.controller;


import co.loyyee.Omi.Drafter.service.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drafter/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final TokenService tokenService;
	
	public AuthController(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@PostMapping("token")
	public String token(Authentication authentication){
		log.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		log.debug("Token granted: {}", token);
		return token;
	
	}
}
