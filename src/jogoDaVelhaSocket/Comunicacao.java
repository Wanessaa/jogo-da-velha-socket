package jogoDaVelhaSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Comunicacao {

	 public static void enviarMensagem (DatagramSocket clientSocket, String mensagem, InetAddress ipAddress, int port) throws IOException {
	    	//Criar o segmento UDP com a String como payload (campo de dados)
			byte[] sendData = mensagem.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);

			//Enviar o segmento UDP
			clientSocket.send(sendPacket);
	    }

	    public static String receberMensagem (DatagramSocket clientSocket) throws IOException {
			//Criar o objeto que armazenar o segmento UDP de resposta
			byte[] receivedData = new byte[1024]; 
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length); 

			//Receber o segmento UDP
			clientSocket.receive(receivePacket); 
			return new String(receivePacket.getData(), 0, receivePacket.getLength());
//			return new String(receivePacket.getData()); 
	    }
	    
	    public static String receberMensagem (DatagramSocket clientSocket, DatagramPacket receivePacket) throws IOException {
			//Criar o objeto que armazenar o segmento UDP de resposta
			byte[] receivedData = new byte[1024]; 
//			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length); 

			//Receber o segmento UDP
			clientSocket.receive(receivePacket); 
			return new String(receivePacket.getData(), 0, receivePacket.getLength());
//			return new String(receivePacket.getData()); 
	    }

}
