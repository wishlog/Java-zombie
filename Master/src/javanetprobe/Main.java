/*
 * Main.java
 *
 * Created on March 14, 2007, 5:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package javanetprobe;

/**
 *
 * @author wishlog
 */
import java.text.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                
                //Display the GUI
                GUI frame = new GUI();
            }
            
        });
    }
}
