/*
 * ECHO.java
 *
 * Created on March 31, 2007, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package javanetprobe;
import java.io.*;
/**
 *
 * @author wishlog
 */
public class ECHO extends Thread {
    
    /**
     * Creates a new instance of ECHO
     */
    
    public ECHO() {
    }
    
    public void run(){
     
        while (true)
        {
            try{
            String message = (String) Global.input.readObject();
            GUI.TextArea.append(";=-  " + 
                    message + (String) (message.matches("terminate received")?"\nConnection from slave Closed":"") + " \n");  
            }catch (IOException e){}
             catch (ClassNotFoundException x){}
             catch (NullPointerException n){};
        }
        
    }
    
}
