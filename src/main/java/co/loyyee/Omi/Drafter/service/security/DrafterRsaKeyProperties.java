package co.loyyee.Omi.Drafter.service.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa.drafter")
public record DrafterRsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}
