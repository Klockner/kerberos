/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cifrador;

import JavaRMI.ClienteImplem;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author klockner
 */
public class Cifrador {
    /**
     * 
     * @param chave
     * @param mensagem
     * @return 
     */
    public byte[] cifrar(String chave, String mensagem) {
        try {
            Cipher cipher;
            String initVector = "RandomInitVector";
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            byte[] texto = mensagem.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(chave.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[]  textoCriptografado = cipher.doFinal(texto);
            return textoCriptografado;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidKeyException |
                InvalidAlgorithmParameterException | IllegalBlockSizeException |
                BadPaddingException e) {
            System.out.println("Erro ao cifrar: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 
     * @param m2
     * @param key
     * @return 
     */
    public String decifrar(byte[] m2, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            String randomVector = "RandomInitVector";
            IvParameterSpec iv = new IvParameterSpec(randomVector.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] original = cipher.doFinal(m2);
            return new String(original);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
                NoSuchPaddingException | IllegalBlockSizeException |
                BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
