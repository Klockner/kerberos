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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {
    private final Dados database;
    private String chaveSessao;
    private int numeroAleatorio;
    
    public ServidorASImplem() throws RemoteException {
        database = new Dados();
    }

    @Override
    public void chamar(String texto, InterfaceCliente interfaceCliente) throws RemoteException {
        System.out.println(texto);
    }

    @Override
    public void autenticar(String idCliente, InterfaceCliente interfaceCliente, 
            InterfaceTGS interfaceTGS, int tempoValidade, int numeroAleatorio) throws RemoteException {
        if (idCliente.equals(database.getIdCliente())) {
            System.out.println("Usuário autenticado.");
            System.out.println("Identificação do CLIENTE: " + idCliente);
            System.out.println("Tempo de validade em minutos: " + tempoValidade);
            System.out.println("Numero aleatório: " + numeroAleatorio);
            this.numeroAleatorio = numeroAleatorio;
        } else {
            System.out.println("Usuário não autenticado!");
        }
    }

    //Cifrar chave da sessao e numero aleatório para para utilizar no TGS
    public void cifrar() throws NoSuchAlgorithmException, NoSuchPaddingException, 
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey chaveDES = keygenerator.generateKey();

        Cipher cifraDES;

        // Cria a cifra 
        cifraDES = Cipher.getInstance("DES/ECB/PKCS5Padding");
        
        // Inicializa a cifra para o processo de encriptação
        cifraDES.init(Cipher.ENCRYPT_MODE, chaveDES);
        
        // Texto puro
        byte[] textoPuro = "Exemplo de texto puro".getBytes();

        System.out.println("Texto [Formato de Byte] : " + textoPuro);
        System.out.println("Texto Puro : " + new String(textoPuro));

        // Texto encriptado
        byte[] textoEncriptado = cifraDES.doFinal(textoPuro);

        System.out.println("Texto Encriptado : " + textoEncriptado);
        
        // Inicializa a cifra também para o processo de decriptação
        cifraDES.init(Cipher.DECRYPT_MODE, chaveDES);

        // Decriptografa o texto
        byte[] textoDecriptografado = cifraDES.doFinal(textoEncriptado);

        System.out.println("Texto Decriptografado : " + new String(textoDecriptografado));
    }
    
    public void gerarTicket() {
        /**
           1. a chave de sessão a ser usada na comunicação com o TGS (kc−tgs) 
           e o número aleatório n1,
           ambos cifrados com a chave do cliente kc registrada no AS; 
        */
    }
    
}
