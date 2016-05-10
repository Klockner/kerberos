/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import interfaces.InterfaceAS;
import interfaces.InterfaceCliente;
import interfaces.InterfaceSS;
import interfaces.InterfaceTGS;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 *
 * @author JamilliF
 */

/*Implementa a interface InterfaceCliente*/
public class ClienteImplem implements InterfaceCliente {
    
    Registry refSN;
    InterfaceAS refAS;
    InterfaceTGS refTGS;
    InterfaceSS refSS;

    public ClienteImplem(Registry refSN) throws RemoteException, NotBoundException {
        this.refSN = refSN;
        //Retorna a referência do servidor AS
        this.refAS = (InterfaceAS) refSN.lookup("ServerAS");
        //Retorna a referência do servidor TGS
        this.refTGS = (InterfaceTGS) refSN.lookup("ServerTGS");
        //Retorna a referência do servidor SS
        this.refSS = (InterfaceSS) refSN.lookup("ServerSS");
        
    }
    
    
}
