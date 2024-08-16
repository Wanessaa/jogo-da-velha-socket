package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

	static int quantidadeDeJogadores = 0;
	static String[][] jogadores = new String[1][1];
	
	public static void main(String args[]) throws Exception {

		JogoDaVelha jogo = new JogoDaVelha();
	
		
		DatagramSocket serverSocket = new DatagramSocket(9876);
		System.out.println("Olá!");
		
		
		byte[] receivedData = new byte[1024];

		while(true) {
	
			Arrays.fill(receivedData, (byte)0);
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
			
			
			String sentence =  "";
			String response = "";
			InetAddress ipAddress;
			int port;
			
			byte[] sendData; 
			DatagramPacket sendPacket = null;
			
			//serverSocket.receive(receivePacket); 			
			
			while(quantidadeDeJogadores < 2) {
				serverSocket.receive(receivePacket);
				sentence = new String(receivePacket.getData()); 
				response = verificarQuantidadeDeJogadores(sentence, receivePacket);
				
				if(quantidadeDeJogadores != 0) {
					povoarTuplaDeJogadores(receivePacket);
				}
				
				ipAddress = receivePacket.getAddress(); 
				port = receivePacket.getPort();
				sendData = response.getBytes(); 
				sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
				serverSocket.send(sendPacket);
			}
			
			if(quantidadeDeJogadores == 2) {
				JogoDaVelha.imprimirTabuleiro(jogo);
				String jogadorSorteado = JogoDaVelha.sortearOPrimeiroAJogar(jogadores);
			
				for (int i = 0; i < 2; i++) {
                    if (jogadores[i][0].equals(jogadorSorteado)) {
                        response = "Você será o primeiro jogador";
                    } else {
                        response = "Aguarde sua vez";
                    }
                    sendData = response.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(jogadores[i][0]), Integer.parseInt(jogadores[i][1]));
                    serverSocket.send(sendPacket);
                }
				
				JogoDaVelha.iniciar();
			}


//			ipAddress = receivePacket.getAddress(); 
//			port = receivePacket.getPort(); 

//			String capitalizedSentence = sentence.toUpperCase(); 
//
//			byte[] sendData = capitalizedSentence.getBytes(); 
//			sendData = response.getBytes(); 
	//		sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port); 

			
	           String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
	           InetAddress clientAddress = receivePacket.getAddress();
	           int clientPort = receivePacket.getPort();
	           System.out.println(receivedMessage);
			
			
			serverSocket.send(sendPacket); 
		}
	}

	private static String verificarQuantidadeDeJogadores(String sentence, DatagramPacket receivePacket) {
		String response;
		if(sentence.equalsIgnoreCase("s") && quantidadeDeJogadores < 3) {
			quantidadeDeJogadores = quantidadeDeJogadores + 1;
		}
		
		if(quantidadeDeJogadores < 2) {
			response = "Esperando outro jogador";
		} else {
			response = "O jogo irá começar";
		}
		return response;
	}

	private static void povoarTuplaDeJogadores(DatagramPacket receivePacket) {
		
		if(jogadores[0][0].isEmpty()) {
			jogadores[0][0] = receivePacket.getAddress().toString();
			jogadores[0][1] = String.valueOf(receivePacket.getPort());	
		
		} else {
			jogadores[1][0] = receivePacket.getAddress().toString();
			jogadores[1][1] = String.valueOf(receivePacket.getPort());	
		}
		
		
	}
	
	

}
