/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author klockner
 */
public final class Dados {
    private String idCliente = "gabrielcom16byte";
    private String idTGS = "IdentidadeTGS123";
    private String chaveTGS = "ChaveTGS12341234";
    private String chaveCliente = "ChaveCliente1234";

    public Dados() {
        
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
    public String getChaveTGS() {
        return chaveTGS;
    }

    /**
     * @param chaveTGS the chaveTGS to set
     */
    public void setChaveTGS(String chaveTGS) {
        this.chaveTGS = chaveTGS;
    }

    /**
     * @return the chaveCliente
     */
    public String getChaveCliente() {
        return chaveCliente;
    }

    /**
     * @param chaveCliente the chaveCliente to set
     */
    public void setChaveCliente(String chaveCliente) {
        this.chaveCliente = chaveCliente;
    }
}