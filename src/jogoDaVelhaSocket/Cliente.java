package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//package jogoDaVelhaSocket;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Scanner;
//
public class Cliente {
//	private static int jogadorAtual = 0;
//
	public static void main(String args[]) throws Exception {

		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();

		// IP e porta de destino
		InetAddress ipAddress = InetAddress.getByName("localhost");
		int port = 9876;

		// Ler do teclado a String a ser enviada
		System.out.println("Deseja jogar o Jogo da Velha s/n?");
		String sentence = keyboardReader.readLine();

		// Criar o segmento UDP com a String como payload (campo de dados)
		byte[] sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);

		// Enviar o segmento UDP
		clientSocket.send(sendPacket);

		// Criar o objeto que armazenar� o segmento UDP de resposta
		byte[] receivedData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

		// Receber o segmento UDP
		clientSocket.receive(receivePacket);

		while (true) {
			//receber resposta do servidor
			String serverMessage = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + serverMessage);
			
			//verificar se o servidor informou que o jogo terminou
			if (serverMessage.contains("Fim de jogo") || serverMessage.contains("Vencedor")) {
                System.out.println("Jogo encerrado.");
                break;
            }
//			
//			// Perguntar ao jogador sua jogada
//            System.out.println("Sua jogada (linha e coluna):");
//            String jogada = keyboardReader.readLine();

//            // Enviar a jogada para o servidor
//            sendData = jogada.getBytes();
//            sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
//            clientSocket.send(sendPacket);
		}
		// clientSocket.close();
	}

}
