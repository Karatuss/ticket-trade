package com.ticket.Ticketing.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    void stringEncryptor() {
        String prKey = "";

        System.out.println("plaintext: " + prKey);
        System.out.println("encryptedtext: " + jasyptEncoding(prKey));
    }

    public String jasyptEncoding(String value) {
        String key = "ccs18-hashing-key";
        StandardPBEStringEncryptor pbeStringEncryptor = new StandardPBEStringEncryptor();
        pbeStringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeStringEncryptor.setPassword(key);
        pbeStringEncryptor.setIvGenerator(new RandomIvGenerator());
        return pbeStringEncryptor.encrypt(value);
    }
}