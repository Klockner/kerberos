/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import Cifrador.Cifrador;
import database.Dados;
import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
import interfaces.InterfaceTGS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {
    private final Dados database;
    private byte[] chaveSessao;
    private final Cifrador cifrador;
    
    public ServidorASImplem() throws RemoteException {
        database = new Dados();
        cifrador = new Cifrador();
    }

    //M1 acontecendo
    @Override
    public void autenticar(String idCliente, InterfaceCliente interfaceCliente, 
            InterfaceTGS interfaceTGS, int timeOut, int n1) throws RemoteException {
        if (idCliente.equals(database.getIdCliente())) {
            System.out.println("Usuário autenticado.");
            System.out.println("Identificação do CLIENTE: " + idCliente);
            System.out.println("Tempo de validade em minutos: " + timeOut);
            System.out.println("Numero aleatório N1: " + n1);
            //Se foi autenticado, responde o cliente.. aqui é enviado M2
            respostaAS(interfaceCliente, n1, timeOut);
        } else {
            System.out.println("Usuário não autenticado!");
        }
    }

    /**
     * Envia resposta ao cliente, MENSAGEM M2
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
        byte[] respostaCripto = cifrador.cifrar(database.getChaveCliente(), resposta);
        byte[] TGTCripto = cifrador.cifrar(database.getChaveTGS(), TGT);
        //Cliente recebe M2
        interfaceCliente.esperaRespostaAS(respostaCripto, TGTCripto);
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
            String keySession = chaveSessao.toString() + "12345";
            return keySession;
            
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erro ao gerar chave de sessão: " + e.getMessage());
            return null;
        }
    }
    
    /**
     *   A chave de sessão a ser usada na comunicação com o TGS (kc−tgs) 
     *   e o número aleatório n1,
     *   ambos cifrados com a chave do cliente kc registrada no AS; 
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
