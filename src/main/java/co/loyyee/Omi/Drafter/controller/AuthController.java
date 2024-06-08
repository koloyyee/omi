package co.loyyee.Omi.Drafter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drafter/private/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /**
     * CRUD https://firebase.google.com/docs/auth/admin/manage-users
     */

    // Swapping to Firebase Auth, commenting out for now. 
		// private final TokenService tokenService;
    // private final AuthenticationManager authenticationManager; // Username & Password approach public

    // AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
    //     this.tokenService = tokenService;
    //     this.authenticationManager = authenticationManager;
    // }
    /**
     * This is the Username and Password approach; reference:
     * <a href="https://www.youtube.com/watch?v=UaB-0e76LdQ"> Walk through</a>
     *
     */
    // @PostMapping("/token")
    // public String token(@RequestBody @NonNull LoginRequest userLogin ) {
    //  Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));
    // 	String token = tokenService.generateToken(authentication);
    // 	log.debug("Token granted: {}", token);
    // 	return token;
    // }
    /**
     * This is the Auth Basic approach
     */
//	@PostMapping("/token")
    // public String token(Authentication authentication){
    // 	log.debug("Token requested for user: '{}'", authentication.getName());
    // 	String token = tokenService.generateToken(authentication);
    // 	log.debug("Token granted: {}", token);
    // 	return token;
    // }
}
