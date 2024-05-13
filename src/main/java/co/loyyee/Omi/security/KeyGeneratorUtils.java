package co.loyyee.Omi.security;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 *
 * <a href="https://github.com/danvega/jwt-username-password/blob/master/src/main/java/dev/danvega/jwt/security/KeyGeneratorUtils.java">KeyGenerator Code reference</a>
 * */
@Component
public final class KeyGeneratorUtils {
  private KeyGeneratorUtils() {}

  static KeyPair generateRsaKey() {
    KeyPair keyPair;
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      keyPair = keyPairGenerator.generateKeyPair();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return keyPair;
  }
}
