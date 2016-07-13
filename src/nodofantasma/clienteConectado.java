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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GESTION
 */
public class clienteConectado implements Runnable {

    static Socket socket = null; //el socket que servira de comunicador 
    ObjectInputStream entrada;
    ObjectOutputStream salida;
    static Message peticion;  //El mensaje que recibira del servidor y/o cliente 
    
    public clienteConectado(Socket client) 
    {
        this.socket = client;
    }
    
    @Override
    public void run() {
        while (true)
        {
            try
            {
                //System.out.println("test");
                socket.setSoLinger(true, 10);
                entrada = new ObjectInputStream(socket.getInputStream());
                salida = new ObjectOutputStream(socket.getOutputStream());
                while (true){
                Object objPeticion = entrada.readObject();
                peticion = (Message) objPeticion;
                System.out.println(peticion.Mensaje);
                if (peticion.Mensaje.equals("conexion"))
                {
                    //ESTE IF ES SI ES LA PRIMERA PERSONA EN CONECTARSE
                    if (NodoFantasma.ultimoConectado.equals("nadie"))
                    {
                        //ENTONCES SE CONECTARA HACIA ESTE NODO FANTASMA
                        peticion.Mensaje = "aceptado";
                        salida.writeObject(peticion);
                        //Y GUARDO A ESE NODO COMO EL ULTIMO
                        NodoFantasma.ultimoConectado = socket.getInetAddress().getHostAddress();
                        //Y ME CONECTO A EL
                        NodoFantasma.conectarClienteFantasma(socket.getInetAddress().getHostAddress());
                        System.out.println("Conectado a " + socket.getInetAddress().getHostAddress());
                    }
                    else
                    {
                        System.out.println("test2");
                        //Si es un nodo que viene ya teniendo uno (o mas conectados)
                        peticion.Mensaje = "redireccion:"+NodoFantasma.ultimoConectado;
                        //Le envio la ip del ultimo del que se conecto para que ahora el sea el ultimo
                        salida.writeObject(peticion);
                        //Y ahora lo asigno a el como el ultimo
                        NodoFantasma.ultimoConectado = socket.getInetAddress().getHostAddress();
                        //Y ahora me conecto yo al ultimo
                        NodoFantasma.reconectarClienteFantasma(socket.getInetAddress().getHostAddress());
                    }
                }
                else
                {
                    String[] comando = peticion.Mensaje.split(":");
                    if (comando[0].equals("redireccion"))
                    {
                        NodoFantasma.reconectarClienteFantasma(comando[1]);
                    }
                    else
                    {               
                        System.out.println(peticion.Mensaje);                  //Si lo que me llego no es una conexion si no cualquier otra cosa, simplemente reenvio al que este conectado
                        NodoFantasma.enviarMensajeFantasma(peticion);
                    }

                }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("cerrado");
                break;

            }
        }
    }
    
}
