/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

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

    public ServidorASImplem() throws RemoteException {
    }

    @Override
    public void chamar(String texto, InterfaceCliente interfaceCliente) throws RemoteException {
        System.out.println(texto);
    }

    @Override
    public void autenticar(String idCliente, InterfaceCliente interfaceCliente, 
            InterfaceTGS interfaceTGS, int tempoValidade, int numeroAleatorio) throws RemoteException {
        System.out.println("Identificação do CLIENTE: " + idCliente);
        System.out.println("Tempo de validade em minutos: " + tempoValidade);
        System.out.println("Numero aleatório: " + numeroAleatorio);
    }
    
    
    
}
