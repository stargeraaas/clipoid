package dev.sukharev.clipangel.utils;

import android.util.Base64;

import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.junit.Assert.*;

public class AESCryptTest {

    @Test
    public void encrypt() {
        try {
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