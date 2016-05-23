/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import Cifrador.Cifrador;
import interfaces.InterfaceCliente;
import interfaces.InterfaceSS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceSS*/
class ServidorSSImplem extends UnicastRemoteObject implements InterfaceSS {
    
    private final String idSS;
    private String kcS;
    private String idCliente;
    private final String chaveServico;
    private final Cifrador cifrador;
    
    public ServidorSSImplem() throws RemoteException {
        idSS = "Serviço Secreto";
        chaveServico = "Chave_do_servico";
        cifrador = new Cifrador();
    }

    /**
     * Recebe a mensgem M5 do cliente, e concede a permissão
     * @param refCliente
     * @param cT
     * @param tcS
     * @param request
     * @throws RemoteException 
     */
    @Override
    public void receberPermissao(InterfaceCliente refCliente, byte[] cT, byte[] tcS, String request) throws RemoteException {
        String respostaDecifrada = cifrador.decifrar(tcS, chaveServico);
        System.out.println("Ticket tcS: " + respostaDecifrada);

        String[] itensRespostam5 = respostaDecifrada.split(" - ");
        System.out.println("Cliente requisitando: " + itensRespostam5[0]);
        System.out.println("Tempo: " + itensRespostam5[1]);
        System.out.println("Chave de Sessão kcS: " + itensRespostam5[2]);
        kcS = itensRespostam5[2];

        respostaDecifrada = cifrador.decifrar(cT, kcS);
        String[] itensResposta = respostaDecifrada.split(" - ");

        System.out.println("Cliente: " + itensResposta[0]);
        idCliente = itensResposta[0];
        System.out.println("Data: " + itensResposta[1]);
        
        System.out.println("Cliente " + idCliente + " liberado para utilizar o serviço!");
        byte[] msg;
        msg = cifrador.cifrar(kcS, "Olá, " + idCliente + " acesso ao serviço " + idSS + " liberado!");
        //Resposta do Serviço para o Cliente M6
        refCliente.esperaRespostaSS(msg);
    }
    
}
