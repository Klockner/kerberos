/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import database.Dados;
import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
import interfaces.InterfaceTGS;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {
    private final Dados database;
    private byte[] chaveSessao;
    
    public ServidorASImplem() throws RemoteException {
        database = new Dados();
    }

    @Override
    public void autenticar(String idCliente, InterfaceCliente interfaceCliente, 
            InterfaceTGS interfaceTGS, int timeOut, int randomNumber) throws RemoteException {
        if (idCliente.equals(database.getIdCliente())) {
            System.out.println("Usuário autenticado.");
            System.out.println("Identificação do CLIENTE: " + idCliente);
            System.out.println("Tempo de validade em minutos: " + timeOut);
            System.out.println("Numero aleatório: " + randomNumber);
            //Se foi autenticado, responde o cliente
            respostaAS(interfaceCliente, randomNumber, timeOut);
        } else {
            System.out.println("Usuário não autenticado!");
        }
    }

    /**
     * Envia resposta ao cliente, mensagem m2
     * Contém a chave de sessão, o número aleatório enviado anteriormente pelo cliente 
     * (devem ser cifrados com a chave do cliente)
     * Também contém o TGT (deve ser cifrado com a chave do TGS)
     * 
     * @param interfaceCliente
     * @param randomNumber
     * @param timeOut
     * @throws RemoteException 
     */
    public void respostaAS(InterfaceCliente interfaceCliente, int randomNumber, int timeOut) throws RemoteException {
        String keySession;
        keySession = gerarChaveSessao();
        String TGT = gerarTicketTGT(database.getIdCliente(), timeOut, keySession);
        String resposta = (keySession + " - " + randomNumber);
        System.out.println("Chave de sessão: " + keySession);
        byte[] chaveSessaoTGS = cifrar(database.getIdCliente(), resposta);
        byte[] TGTEncriptado = cifrar(database.getIdTGS(), TGT);
        interfaceCliente.esperaResposta(chaveSessaoTGS, TGTEncriptado);
    }
    
    /**
     * Gera chave de sessão baseado no id do cliente com data e hora
     * 
     * @return 
     */
    public String gerarChaveSessao() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date data = new Date();
        String dataHora = sdf.format(data);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String dataHoraCliente = dataHora + database.getIdCliente();
            md.update(dataHoraCliente.getBytes());
            chaveSessao = md.digest();
            //TODO why? 12345
            String keySession = Arrays.toString(chaveSessao) + "12345";
            return keySession;
            
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erro ao gerar chave de sessão: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cifrar chave da sessao e numero aleatório para para utilizar no TGS
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
        1. a chave de sessão a ser usada na comunicação com o TGS (kc−tgs) 
        e o número aleatório n1,
        ambos cifrados com a chave do cliente kc registrada no AS; 
     * @param idCliente
     * @param timeOut
     * @param chaveSessao
     * @return 
    */
    public String gerarTicketTGT(String idCliente, int timeOut, String chaveSessao) {
        String ticketTGT = idCliente + " - " + timeOut + " - " + chaveSessao;
        return ticketTGT;
    }
    
}
