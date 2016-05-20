/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidortickettgs;

import Cifrador.Cifrador;
import interfaces.InterfaceTGS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceTGS*/
class ServidorTGSImplem extends UnicastRemoteObject implements InterfaceTGS {
    private byte[] ticketTGT;
    private Cifrador cifrador;
    private String chaveSessaoServico = "chaveSessaoServi";
    
    
    public ServidorTGSImplem() throws RemoteException {
    }

    @Override
    public void recebeMensagemM3(byte[] mensagem, byte[] ticketTGT, String idServidor, int n2) throws RemoteException {
        System.out.println("");

        
//        String respostaDecifrada = cifrador.decifrar(mensagem, );
//        System.out.println("RESPOSTA DECIFRADA: " + respostaDecifrada);
//        String[] itensRespostaM2 = respostaDecifrada.split(" - ");
//        System.out.println("Chave de sessão: " + itensRespostaM2[0]);
//        chaveSessao = itensRespostaM2[0];
//        System.out.println("Numero aleatório N1: " + itensRespostaM2[1]);
//        this.ticketTGT = TGT;
    }
    
    
}
