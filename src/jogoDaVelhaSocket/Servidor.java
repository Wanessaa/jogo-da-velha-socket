package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

	public static void main(String args[]) throws Exception {

		DatagramSocket serverSocket = new DatagramSocket(9876);
		System.out.println("UDP server rodando!");
		
		byte[] receivedData = new byte[1024];

		while(true) {
			
			Arrays.fill(receivedData, (byte)0);
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
			
			serverSocket.receive(receivePacket); 

			String sentence = new String(receivePacket.getData()); 

			InetAddress ipAddress = receivePacket.getAddress(); 
			int port = receivePacket.getPort(); 

			String capitalizedSentence = sentence.toUpperCase(); 

			byte[] sendData = capitalizedSentence.getBytes(); 
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port); 

			
	           String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
	           InetAddress clientAddress = receivePacket.getAddress();
	           int clientPort = receivePacket.getPort();
	           System.out.println(receivedMessage);
			
			
			serverSocket.send(sendPacket); 

		}
        
	}

}
