package it.uniroma3.siw_techstore.Util;

import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptPassword {
    public static void main(String[] args) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword("TechStoreEncKey123!"); // La tua chiave segreta
        String encryptedPassword = encryptor.encrypt("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");
        System.out.println("Password cifrata: ENC(" + encryptedPassword + ")");
    }
}
