package dev.sukharev.clipangel.utils;

import org.junit.Test;

import java.security.GeneralSecurityException;

import dev.sukharev.clipangel.utils.aes.AESCrypt;

public class AESCryptTest {

    @Test
    public void encrypt() {
        try {
            // "muFUAmobqOgRY1uCr2HM9TW3vfT3ERdkQV/f6x1l3no="
            String result = AESCrypt.decrypt(
                    "d4FWQJ2vKEc9bP9yiAwdqZfTdw4jXaNOa7V6nNg0eYc=",
                   "0aFZrhpfQtsn8x9GAg3MwA==");
            System.out.println();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}