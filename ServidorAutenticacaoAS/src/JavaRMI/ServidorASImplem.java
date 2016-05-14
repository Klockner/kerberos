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
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {
    private final Dados database;
    private String chaveSessao = "essaehachave";
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

            //Criptografar chave sessão
            cifrar(chaveSessao);
            //Criptografar número aleatório
            cifrar(String.valueOf(this.numeroAleatorio));
        } else {
            System.out.println("Usuário não autenticado!");
        }
    }

    //Cifrar chave da sessao e numero aleatório para para utilizar no TGS
    public void cifrar(String textoParaCifrar) {
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey chaveDES = keygenerator.generateKey();

            Cipher cifraDES;

            // Cria a cifra
            cifraDES = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Inicializa a cifra para o processo de encriptação
            cifraDES.init(Cipher.ENCRYPT_MODE, chaveDES);

            //Chave sessão em bytes
            byte[] chaveSessaoByte = textoParaCifrar.getBytes();

            System.out.println("\nTexto [Formato de Byte] : " + chaveSessaoByte);
            System.out.println("Texto Puro : " + new String(textoParaCifrar));

            // Texto encriptado
            byte[] chaveSessaoEncriptada = cifraDES.doFinal(chaveSessaoByte);

            System.out.println("Texto Encriptado : " + chaveSessaoEncriptada);

            // Inicializa a cifra também para o processo de decriptação
            cifraDES.init(Cipher.DECRYPT_MODE, chaveDES);

            // Decriptografa o texto
            byte[] chaveSessaoDecriptografado = cifraDES.doFinal(chaveSessaoEncriptada);

            System.out.println("Texto Decriptografado : " + new String(chaveSessaoDecriptografado));
        } catch (Exception e) {
            System.out.println("Erro ao cifrar: " + e.getMessage());
        }
    }
    
    public void gerarTicket() {
        /**
           1. a chave de sessão a ser usada na comunicação com o TGS (kc−tgs) 
           e o número aleatório n1,
           ambos cifrados com a chave do cliente kc registrada no AS; 
        */
    }
    
}
