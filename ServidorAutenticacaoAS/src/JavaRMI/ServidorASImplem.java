/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
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
    public void autenticar(String request, InterfaceCliente interfaceCliente) throws RemoteException {
        System.out.println("Servidor: " + request);
    }
    
    
    
}
