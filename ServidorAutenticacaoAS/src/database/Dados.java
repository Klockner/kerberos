/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author klockner
 */
public final class Dados {
    private String idCliente = "gabrielcom16byte";
    private String idTGS = "identidadedo_TGS";
    private SecretKeySpec chaveTGS;
    private SecretKeySpec chaveCliente;

    public Dados() {
        chaveCliente = gerarSecretKey(idCliente);
        chaveTGS = gerarSecretKey(idTGS);
    }
    
    /**
     * @return the idCliente
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the chaveCliente
     */
    public SecretKeySpec getChaveCliente() {
        return chaveCliente;
    }

    /**
     * @param chaveCliente the chaveCliente to set
     */
    public void setChaveCliente(String chaveCliente) {
        this.setChaveCliente(chaveCliente);
    }

    /**
     * @return the idTGS
     */
    public String getIdTGS() {
        return idTGS;
    }

    /**
     * @param idTGS the idTGS to set
     */
    public void setIdTGS(String idTGS) {
        this.idTGS = idTGS;
    }

    /**
     * @return the chaveTGS
     */
    public SecretKeySpec getChaveTGS() {
        return chaveTGS;
    }

    /**
     * @param chaveTGS the chaveTGS to set
     */
    public void setChaveTGS(SecretKeySpec chaveTGS) {
        this.chaveTGS = chaveTGS;
    }

    /**
     * @param chaveCliente the chaveCliente to set
     */
    public void setChaveCliente(SecretKeySpec chaveCliente) {
        this.chaveCliente = chaveCliente;
    }
    
    /**
     * 
     * @param seed
     * @return 
     */
    public SecretKeySpec gerarSecretKey(String seed) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(seed.getBytes("UTF-8"), "AES");
            return secretKey;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Dados.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
