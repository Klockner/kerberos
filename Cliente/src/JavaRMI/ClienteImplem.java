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
    
    Registry refSN; //Registry do cliente
    Registry refSN2; //Registry do TGS
    Registry refSN3; //Registry do serviço
    InterfaceAS refAS;
    InterfaceTGS refTGS;
    InterfaceSS refSS;
    private String clientId;
    private final String chaveCliente = "ChaveCliente1234";
    private final String idServico = "ServicoFireInThe";
    private String chaveSessao;
    private final Cifrador cifrador;
    private byte[] ticketTGT;
    private String kcS; //chave de sessão gerada no TGS
    

    public ClienteImplem(Registry refSN, Registry refSN2, Registry refSN3) throws RemoteException, NotBoundException {
        this.refSN = refSN;
        this.refSN2 = refSN2;
        this.refSN3 = refSN3;
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
        if (n1 < 0) {
            n1 = n1 * -1;
        }
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
    public void esperaRespostaAS(byte[] respostaM2, byte[] TGT) {
        System.out.println("Estou autenticado no AS");
        String respostaDecifrada = cifrador.decifrar(respostaM2, chaveCliente);
        System.out.println("RESPOSTA DECIFRADA: " + respostaDecifrada);
        String[] itensRespostaM2 = respostaDecifrada.split(" - ");
        chaveSessao = itensRespostaM2[0];
        System.out.println("Chave de sessão: " + chaveSessao);
        System.out.println("Numero aleatório N1: " + itensRespostaM2[1]);
        this.ticketTGT = TGT;
        
        enviaMensagemClienteTgsM3();
    }
    
    /**
     * Solicitação do ticket ao TGS, Mensagem M3
     */
    public void enviaMensagemClienteTgsM3() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date data = new Date();
        String dataHora = sdf.format(data);

        Random geradorRandom = new Random();
        int n2 = geradorRandom.nextInt();
        if (n2 < 0) {
            n2 = n2 * -1;
        }
        
        try {
            refTGS = (InterfaceTGS) refSN2.lookup("ServerTGS");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Erro ao pegar referencia do TGS: " + ex.getMessage());
        }

        String mensagem = clientId + " - " + dataHora;
        System.out.println("ESSA EH A CHAVE: " + chaveSessao);
        byte[] mensagemCripto = cifrador.cifrar(chaveSessao, mensagem);
            
        try {
            //Envia ao TGS a mensagem criptografada, o ticket TGT, a identidade do serviço e o numero aleatório
            refTGS.recebeMensagemClienteTgsM3(this, mensagemCripto, ticketTGT, idServico, n2);
        } catch (RemoteException ex) {
            System.out.println("Erro ao chamar o método do TGS: " + ex.getMessage());
        }
            
    }

    /**
     * Recebe a mensagem M4, resposta de M3
     * @param respostaM4
     * @param tcS
     * @throws RemoteException 
     */
    @Override
    public void esperaRespostaTGS(byte[] respostaM4, byte[] tcS) throws RemoteException {
        System.out.println("\nESTOU AUTENTICADO NO TGS");
        String respostaDecifrada = cifrador.decifrar(respostaM4, chaveSessao);
         String[] itensRespostam4 = respostaDecifrada.split(" - ");

        System.out.println("Chave de servico kc-s:" + itensRespostam4[0]);
        kcS = itensRespostam4[0];
        System.out.println("Número aleatório N2: " + itensRespostam4[1]);
        
        //Envia mensagem M5 para o Serviço
        solicitarAutenticacaoSS(tcS, kcS);
    }

    /**
     * Envia mensagem M5 para o Serviço
     * @param tcS
     * @param kcS 
     */
    private void solicitarAutenticacaoSS(byte[] tcS, String kcS) {
        try {
            refSS = (InterfaceSS) refSN3.lookup("ServerSS");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Formata a data e hora
        Date data = new Date(); //Pega a data e hora atual  
        String dataFormatada = sdf.format(data);

        String ct = (clientId + " - " + dataFormatada);
        byte[] ctEncriptografado = cifrador.cifrar(kcS, ct);
        try {
            refSS.receberPermissao(this, ctEncriptografado, tcS, "Estou requesitando o serviço X");
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void esperaRespostaSS(byte[] msgServico) throws RemoteException {
        String msg = cifrador.decifrar(msgServico, kcS);
        System.out.println("\nServidor: " + msg);
    }
    
    
    
    
    
}
