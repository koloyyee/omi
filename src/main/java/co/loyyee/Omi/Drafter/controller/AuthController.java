package co.loyyee.Omi.Drafter.controller;


import co.loyyee.Omi.Drafter.service.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drafter/private/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager; // Username & Password approach
	
	public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
	}
	
	/**
	 * This is the Username and Password approach;
	 * */
	@PostMapping("/token")
	public String token() {
//		log.debug("Token requested for user: '{}'", authentication.getName());
//		String token = tokenService.generateToken(authentication);
//		log.debug("Token granted: {}", token);
//		return token;
		return null;
	}
	

	/**
	 * This is the Auth Basic approach 
	 * */
//	@PostMapping("/token")
	public String token(Authentication authentication){
		log.debug("Token requested for user: '{}'", authentication.getName());
		String token = tokenService.generateToken(authentication);
		log.debug("Token granted: {}", token);
		return token;
	
	}
}
