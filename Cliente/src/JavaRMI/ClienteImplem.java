/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
import interfaces.InterfaceSS;
import interfaces.InterfaceTGS;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
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
 * @author Klockner
 */

/*Implementa a interface InterfaceCliente*/
public class ClienteImplem extends UnicastRemoteObject implements InterfaceCliente {
    
    private final Registry refSN;
    private final InterfaceAS refAS;
    private InterfaceTGS refTGS;
    private InterfaceSS refSS;
    private String clientId;
    private String chaveSessao;
    

    public ClienteImplem(Registry refSN) throws RemoteException, NotBoundException {
        this.refSN = refSN;
        //Retorna a referência do servidor AS
        this.refAS = (InterfaceAS) refSN.lookup("ServerAS");
    }
     
    /**
     * Envia uma solicitação m1 de autenticação para o AS
     * 
     * @param clientId
     * @param timeOut
     * @throws RemoteException 
     */
    public void solicitarAutenticacao(String clientId, int timeOut) throws RemoteException {
        this.clientId = clientId;
        Random geradorRandom = new Random();
        int n1 = geradorRandom.nextInt();
        
        // Manda solicitação m1 pro AS
        //identidade cliente, identidade do serviço desejado, 
        //prazo de validade, número aleatório
        refAS.autenticar(clientId, this, refTGS, timeOut, n1);
    }
    
    /**
     * Recebe do AS a resposta da solicitação m1
     * 
     * @param respostaM2
     * @param TGT 
     */
    @Override
    public void esperaResposta(byte[] respostaM2, byte[] TGT) {
        //TODO não ta decifrando
        String respostaDecifrada = decifrar(respostaM2, clientId);
        String[] itensRespostaM2 = respostaDecifrada.split(" - ");
        System.out.println("Estou autenticado no AS");
        System.out.println("Chave de sessão: " + itensRespostaM2[0]);
        chaveSessao = itensRespostaM2[0];
        System.out.println("Numero aleatório: " + itensRespostaM2[1]);
        
        //TODO what?
        String aux = "Identidadedo_TGS";
        try {
            SecretKeySpec keySessao = new SecretKeySpec(aux.getBytes("UTF-8"), "AES");
            respostaDecifrada = decifrar(TGT, aux);
            System.out.println("TESTE: " + respostaDecifrada);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
            return Arrays.toString(original);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
                NoSuchPaddingException | IllegalBlockSizeException |
                BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
