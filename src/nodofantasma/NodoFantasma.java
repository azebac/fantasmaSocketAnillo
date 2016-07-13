/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nodofantasma;

import com.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GESTION
 */
public class NodoFantasma {

    static int puerto = 7000; //puerto de comunicacion del servidor
    static ServerSocket server; //el servidor
    public static String ultimoConectado = "nadie";
    static Socket socket=null;
    public static Socket socketCliente = null;
    static ObjectInputStream entrada;
    static ObjectOutputStream salida;
   
    
    public static void main(String[] args) {
     try {
         server = new ServerSocket(puerto);
          while (true)
          {
                //System.out.println("El server acepta conexiones");
                socket = server.accept();
                Runnable nuevaConexion = new clienteConectado(socket);
                Thread hilo = new Thread(nuevaConexion);
                hilo.start();
          }
     }    
     catch (Exception e)
     {
         e.printStackTrace();
     }
    
    }
    
    public static void reconectarClienteFantasma(String ip)
    {
      try
        {
            socketCliente.close();
            salida = null;
            entrada = null;
            System.out.println("desconectado y redirigido a: " + ip);
            if (ip.equals(Inet4Address.getLocalHost().getHostAddress()))
            {
                System.out.println("Anillo vacio");
                NodoFantasma.ultimoConectado = "nadie";
            }
            else
            {
            socketCliente = new Socket(ip, puerto);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            Message peticion = new Message();
            peticion.Mensaje = "conexion";
            salida.writeObject(peticion);
            }
            
        }
      catch (Exception e)
      {
          Logger.getLogger(NodoFantasma.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    
    public static void conectarClienteFantasma(String ip)
    {
      try
        {
            socketCliente = new Socket(ip, puerto);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            Message peticion = new Message();
            peticion.Mensaje = "conexion";
            salida.writeObject(peticion);
            
        }
      catch (Exception e)
      {
          Logger.getLogger(NodoFantasma.class.getName()).log(Level.SEVERE, null, e);
      }
    }
    
    public static void desconectarClienteFantasma()
    {
        try {
            socketCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(NodoFantasma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void enviarMensajeFantasma(Message mensaje)
    {
        try {
            /*salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());*/
            //salida = new ObjectOutputStream(socketCliente.getOutputStream());
            salida.writeObject(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(NodoFantasma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
