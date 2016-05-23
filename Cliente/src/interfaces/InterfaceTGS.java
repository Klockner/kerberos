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
public interface InterfaceTGS extends Remote {
    public void recebeMensagemClienteTgsM3(InterfaceCliente refCliente, byte[] mensagemCripto,
            byte[] ticketTGT, String idServidor, int n2) throws RemoteException;
}
