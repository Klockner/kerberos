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
public interface InterfaceSS extends Remote {
    public void receberPermissao(InterfaceCliente interfaceCliente, byte [] cT, byte[] ticket, String request)throws RemoteException;
}
