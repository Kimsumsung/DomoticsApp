package com.aae.domotics;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import com.domotics.DomoticsEncryption;
 
 
public class TCPClient {	
 
    private String serverMessage;
    public static final String SERVERIP = "192.168.1.33"; //your computer IP address
    public static final int SERVERPORT = 1470; //your port
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    
    //Declare encryption object
    PrintWriter out;
    BufferedReader in;
 
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }
 
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
    	
    	String strMessage = message+"\r\n";
    	char[] charMessage = strMessage.toCharArray();
    	byte[] byteArray = strMessage.getBytes();
    	DomoticsEncryption enc = new DomoticsEncryption();
    	
    	
    	// Check encrypt/undecrypt or not!!! 
        if(charMessage[2] != 'U' && charMessage[2] != 'V' && charMessage[2] !='N'){
        	
        	byte[] encrypted_msg = enc.Encrypt(byteArray, (byte)'1');
        	String output_encrypted= new String(encrypted_msg);	
        	//End of Encrypted
        	if(out != null && !out.checkError()) {
                //out.println(message);
                out.print(output_encrypted);
                out.flush();
            	} 	
        }
        else{
        	if(out != null && !out.checkError()) {
                //out.println(message);
                out.print(strMessage);	
                out.flush();
            }
        }   
    }

    public void stopClient(){
        mRun = false;
    }
 
    public void run() {
 
        mRun = true;
 
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("TCP Client", "C: Connecting...");
 
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
 
            try {
 
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
 
                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();
 
                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
 
                }
 
 
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {	
 
            Log.e("TCP", "C: Error", e);
 
        }
 
    }
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}