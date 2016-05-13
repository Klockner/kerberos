/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Klockner
 */
public interface InterfaceAS extends Remote {
    
    public void chamar(String texto, InterfaceCliente interfaceCliente) throws RemoteException;
    public void autenticar(String idCliente, InterfaceCliente interfaceCliente, 
            InterfaceTGS interfaceTGS, int tempoValidade, int numeroAleatorio) throws RemoteException;
}
