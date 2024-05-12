package co.loyyee.Omi.security;

import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 *
 * <a href="https://github.com/danvega/jwt-username-password/blob/master/src/main/java/dev/danvega/jwt/security/Jwks.java"> Jwks Code Reference</a>
 * */
public class Jwks {
	private Jwks() {}
	
	public static RSAKey generateRsa() {
		KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
	}
}
