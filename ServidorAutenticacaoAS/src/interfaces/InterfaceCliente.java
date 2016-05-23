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
public interface InterfaceCliente extends Remote {
    public void esperaRespostaAS(byte[] respostaM2, byte[] TGT) throws RemoteException;
    public void esperaRespostaTGS(byte[] s_n2, byte[] ticketS) throws RemoteException;
    public void esperaRespostaSS(byte[] msgServico) throws RemoteException;
}
