package jogoDaVelhaSocket;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

	static int quantidadeDeJogadores = 0;
	static String[][] jogadores = new String[2][3];

	public static void main(String args[]) throws Exception {

		JogoDaVelha jogo = new JogoDaVelha();

		DatagramSocket serverSocket = new DatagramSocket(80);
		System.out.println("Servidor iniciado!");

		byte[] receivedData = new byte[1024];

		while (true) {

			Arrays.fill(receivedData, (byte) 0);
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

			String sentence = "";
			String response = "";
			InetAddress ipAddress;
			int port;

			byte[] sendData;
			DatagramPacket sendPacket = null;
			
			while (quantidadeDeJogadores < 2) {
				
				serverSocket.receive(receivePacket);
				sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

				if (sentence.equalsIgnoreCase("s") && quantidadeDeJogadores < 2) {
					povoarTuplaDeJogadores(receivePacket);
					quantidadeDeJogadores++;

					if (quantidadeDeJogadores == 1) {
						response = "Esperando outro jogador";
					} else {
						response = "O jogo irá começar";
					}

					ipAddress = receivePacket.getAddress();
					port = receivePacket.getPort();
					sendData = response.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
					serverSocket.send(sendPacket);

				}

				if (quantidadeDeJogadores == 2) {
					for (int i = 0; i < quantidadeDeJogadores; i++) {
						ipAddress = InetAddress.getByName(jogadores[i][0]);
						port = Integer.parseInt(jogadores[i][1]);
						sendData = response.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
						serverSocket.send(sendPacket);
					}
				}
				receivePacket = new DatagramPacket(receivedData, receivedData.length);
				receivedData = new byte[1024];
			}

			if (quantidadeDeJogadores == 2) {
				JogoDaVelha.imprimirTabuleiro(jogo);
				String jogadorSorteado = JogoDaVelha.sortearOPrimeiroAJogar(jogadores);
				
				for (int i = 0; i < 2; i++) {
					String jogador = jogadores[i][0] + ":" + jogadores[i][1];
					if (jogador.equals(jogadorSorteado)) {
						response = "Você será o primeiro jogador";
					} else {
						response = "Aguarde sua vez";
					}
					sendData = response.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(jogadores[i][0]),
							Integer.parseInt(jogadores[i][1]));
					serverSocket.send(sendPacket);
				}

				serverSocket.receive(receivePacket);
				
				JogoDaVelha.iniciar(serverSocket,  receivePacket, jogadores, jogo);
			}
		}
	}

	private static void povoarTuplaDeJogadores(DatagramPacket receivePacket) {

		for (int i = 0; i < jogadores.length; i++) {
			if (jogadores[i][0] == null) {
				jogadores[i][0] = receivePacket.getAddress().getHostAddress();
				jogadores[i][1] = String.valueOf(receivePacket.getPort());
				break;
			}
		}
	}
}
