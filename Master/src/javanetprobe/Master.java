/*
 * Master.java
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

public class Master  extends Thread {
    
    //Variable declare:
    private String slaveIP = "";      
    private int slavePort;
    private Socket slaveSocket;
    
    /** Creates a new instance of Master */
    public Master(String slaveIP, int slavePort) 
    {
       this.slaveIP = slaveIP;
       this.slavePort = slavePort;
    }
    
    public void run()
    {   
        
        //Create Socket:
        try{
            slaveSocket =  new Socket(InetAddress.getByName(slaveIP), slavePort); 
            Global.output = new ObjectOutputStream(slaveSocket.getOutputStream());
            Global.input = new ObjectInputStream(slaveSocket.getInputStream());
            GUI.TextArea.setText("Connection Established\nWaiting for command\n");
        }catch (IOException x) {GUI.TextArea.append("Couldn't get I/O for: " + slaveIP + "\n");
             try {
                 Global.output.close();
                 Global.input.close();
                 slaveSocket.close();
             }catch (IOException y){}
              catch (NullPointerException n){}
          }
        //Start ECHO Thread:
        new ECHO().start();
    }
    
}
