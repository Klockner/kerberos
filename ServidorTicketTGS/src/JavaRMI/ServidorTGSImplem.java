/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import Cifrador.Cifrador;
import interfaces.InterfaceCliente;
import interfaces.InterfaceTGS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceTGS*/
class ServidorTGSImplem extends UnicastRemoteObject implements InterfaceTGS {
    InterfaceCliente refCliente;
    private Cifrador cifrador;
    private String chaveSessao;
    private final String chaveTGS = "ChaveTGS12341234";
    private String kcS;
    private String chaveServico = "Chave_do_servico";
    private String idServico = "IdentidadeDoServ";
    
    
    public ServidorTGSImplem() throws RemoteException {
        cifrador = new Cifrador();
    }

    
    /**
     * Recebeu mensagem M3 do Cliente
     * Após verificar a validade do ticket TGT, o TGS devolve ao
     * cliente uma mensagem m4
     * @param mensagem
     * @param ticketTGT
     * @param idServidor
     * @param n2
     * @throws RemoteException 
     */
    @Override
    public void recebeMensagemClienteTgsM3(InterfaceCliente refCliente, byte[] mensagemCripto, byte[] ticketTGT, String idServidor, int n2) throws RemoteException {
        String respostaDecifrada = cifrador.decifrar(ticketTGT, chaveTGS);

        System.out.println("Ticket TGT: " + respostaDecifrada);
        String[] arrayRespostaM3 = respostaDecifrada.split(" - ");
        System.out.println("Id cliente: " + arrayRespostaM3[0]);
        System.out.println("Tempo de validade: " + arrayRespostaM3[1]);
        System.out.println("Chave de sessão: " + arrayRespostaM3[2]);
        chaveSessao = arrayRespostaM3[2];

        respostaDecifrada = cifrador.decifrar(mensagemCripto, chaveSessao);

        String[] arrayCtM3 = respostaDecifrada.split(" - ");
        System.out.println("Id cliente: " + arrayCtM3[0]);
        System.out.println("Data solicitação: " + arrayCtM3[1]);

        //Se o id do cliente for o mesmo, TicketTGT válido
        if (arrayRespostaM3[0].equals(arrayCtM3[0])) {
            System.out.println("\nTicket TGT válido.");
            //Responde ao cliente, M4
            //referencia do cliente, numero aletaorio, chave de sessao, id cliente, data de solicitação, validade
            respostaTgsM4(refCliente, n2, chaveSessao, arrayCtM3[0], arrayCtM3[1], arrayRespostaM3[1]);
        } else {
            System.out.println("Ticket TGT inválido.");
        }
    }
    
    /**
     * Envia a resposta M4 para o cliente
     * @param refCliente
     * @param n2
     * @param chaveSessao
     * @param idCliente
     * @param data
     * @param tempo 
     */
    public void respostaTgsM4(InterfaceCliente refCliente, int n2, String chaveSessao, String idCliente, String data, String tempo) {
        try {
            kcS = geraChaveSessao();
            String resposta = kcS + " - " + n2;
            byte[] respostaCripto = cifrador.cifrar(chaveSessao, resposta); //Encripta a resposta com a chave de sessão do TGS


            String tcS = idCliente + " - " + tempo + " - " + kcS;
            byte[] tcSCriptografado = cifrador.cifrar(chaveServico, tcS); //Encripta o ticket Tc-s com a chave do servidor, o id do cleinte e o tempo cedido
            try {
                refCliente.esperaRespostaTGS(respostaCripto, tcSCriptografado);
            } catch (RemoteException ex) {
                Logger.getLogger(ServidorTGSImplem.class.getName()).log(Level.SEVERE, null, ex);
            }

            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ServidorTGSImplem.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    /* Gera uma chave de sessão baseado no id do serviço concatenado a data e hora atual*/
    public String geraChaveSessao() throws NoSuchAlgorithmException {
        byte[] keySession;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss"); //Formata a data e hora
        Date data = new Date(); //Pega a data e hora atual
        String dataHora = sdf.format(data);
        MessageDigest md = MessageDigest.getInstance("MD5");
        String dataHoraCliente = dataHora + idServico; //Concatena a hora e data com o id do cliente
        md.update(dataHoraCliente.getBytes()); //Pega  chave de sessão para a criptografia
        keySession = md.digest();
        String keySessao = keySession.toString() + "12345";
        return keySessao;
    }
    
}
