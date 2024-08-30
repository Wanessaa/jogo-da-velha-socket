package jogoDaVelhaSocket.mensagem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import jogoDaVelhaSocket.Jogada;

public class EnvioDePacote {

		public static void enviarMensagem (DatagramSocket clientSocket, String mensagem, InetAddress ipAddress, int port) throws IOException {
	    	//Criar o segmento UDP com a String como payload (campo de dados)
			byte[] sendData = mensagem.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
	
			//Enviar o segmento UDP
			clientSocket.send(sendPacket);
	    }
	
	 	public static void enviarMensagem (DatagramSocket clientSocket, Jogada jogada, InetAddress ipAddress, int port) throws IOException {
	    	//Criar o segmento UDP com a String como payload (campo de dados)
		 
		 	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		    ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		    objectStream.writeObject(jogada);
		    objectStream.flush();
		    
		    byte[] sendData = byteStream.toByteArray();
			//byte[] sendData = mensagem.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);

			//Enviar o segmento UDP
			clientSocket.send(sendPacket);
			
			//Fecha o fluxo de objetos
			objectStream.close();
	    }

	    public static String receberMensagem (DatagramSocket clientSocket) throws IOException {
			//Criar o objeto que armazenar o segmento UDP de resposta
			byte[] receivedData = new byte[1024]; 
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length); 

			//Receber o segmento UDP
			clientSocket.receive(receivePacket); 
			return new String(receivePacket.getData(), 0, receivePacket.getLength());
	    }
	    
	    public static String receberMensagem (DatagramSocket clientSocket, DatagramPacket receivePacket) throws IOException {
			//Receber o segmento UDP
			clientSocket.receive(receivePacket); 
			return new String(receivePacket.getData(), 0, receivePacket.getLength());
	    }

		public static Jogada receberJogada(DatagramPacket receivePacket) throws Exception {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());
		    ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		    Jogada jogada = (Jogada) objectStream.readObject();
		    return jogada;
		}
	    
	    

}
