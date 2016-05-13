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

/**
 *
 * @author Klockner
 */

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {
    private final Dados database;
    private String ticket;
    
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
        } else {
            System.out.println("Usuário não autenticado!");
        }
    }
    
    public void gerarTicket() {
        //TODO implementar 
        /**
           1. a chave de sessão a ser usada na comunicação com o TGS (kc−tgs) 
           e o número aleatório n1,
           ambos cifrados com a chave do cliente kc registrada no AS; 
        */
    }
    
}
