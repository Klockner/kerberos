/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Klockner
 */
public class ServidorAutenticacaoAS {
    static Registry refSN;
    
    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        try {
            refSN = LocateRegistry.getRegistry(1111);
            refSN.list();
        } catch (RemoteException ex) {
            refSN = LocateRegistry.createRegistry(1111);
        }
        
        ServidorASImplem servASImpl = new ServidorASImplem();
        refSN.rebind("ServerAS", servASImpl);
    }
}
