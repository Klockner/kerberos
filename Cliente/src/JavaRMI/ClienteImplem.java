/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import Cifrador.Cifrador;
import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
import interfaces.InterfaceSS;
import interfaces.InterfaceTGS;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klockner
 */
public class ClienteImplem extends UnicastRemoteObject implements InterfaceCliente {
    
    private Registry refSN;
    private final InterfaceAS refAS;
    private InterfaceTGS refTGS;
    private InterfaceSS refSS;
    private String clientId;
    private final String chaveCliente = "ChaveCliente1234";
    private final String serverId = "ServicoFireInThe";
    private String chaveSessao;
    private final Cifrador cifrador;
    private byte[] ticketTGT;
    

    public ClienteImplem(Registry refSN) throws RemoteException, NotBoundException {
        this.refSN = refSN;
        //Retorna a referência do servidor AS
        this.refAS = (InterfaceAS) refSN.lookup("ServerAS");
        cifrador = new Cifrador();
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
     * Recebe do AS a mensagem M2 > resposta da M1
     * 
     * @param respostaM2
     * @param TGT 
     */
    @Override
    public void esperaResposta(byte[] respostaM2, byte[] TGT) {
        System.out.println("Estou autenticado no AS");
        String respostaDecifrada = cifrador.decifrar(respostaM2, chaveCliente);
        System.out.println("RESPOSTA DECIFRADA: " + respostaDecifrada);
        String[] itensRespostaM2 = respostaDecifrada.split(" - ");
        System.out.println("Chave de sessão: " + itensRespostaM2[0]);
        chaveSessao = itensRespostaM2[0];
        System.out.println("Numero aleatório N1: " + itensRespostaM2[1]);
        this.ticketTGT = TGT;
        
        enviaMensagemM3();
    }
    
    /**
     * Solicitação TGS
     */
    public void enviaMensagemM3() {
        try {
            refSN = LocateRegistry.getRegistry("localhost", 1112);
            //Retorna a referência do servidor AS
            this.refTGS = (InterfaceTGS) refSN.lookup("ServerTGS");
            
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date data = new Date();
            String dataHora = sdf.format(data);
            
            Random geradorRandom = new Random();
            int n2 = geradorRandom.nextInt();
            
            String resposta = dataHora + " - " + clientId;
            byte[] mensagem = cifrador.cifrar(chaveSessao, resposta);
            
            //Envia mensagem la no TGS
            refTGS.recebeMensagemM3(mensagem, ticketTGT, serverId, n2);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
