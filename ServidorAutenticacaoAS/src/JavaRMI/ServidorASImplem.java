/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorautenticacaoas;

import interfaces.InterfaceAS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author JamilliF
 */

/*Implementa a interface InterfaceAS */
public class ServidorASImplem extends UnicastRemoteObject implements InterfaceAS {

    public ServidorASImplem() throws RemoteException {
    }
    
    
    
}
