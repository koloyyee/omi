package co.loyyee.Omi.Drafter.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Qualifier("drafterRsaKey")
@ConfigurationProperties(prefix = "rsa.drafter")
public record DrafterRsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}
