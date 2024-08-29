package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

public class Servidor {

	static int quantidadeDeJogadores = 0;
	static String[][] jogadores = new String[2][3];
	
	static HashMap<Integer, Jogador> jogadoresMapeados = new HashMap<>();

	public static void main(String args[]) throws Exception {

		JogoDaVelha jogo = new JogoDaVelha();

		DatagramSocket serverSocket = new DatagramSocket(80);
		System.out.println("Servidor iniciado!");

		byte[] receivedData = new byte[1024];

		while (true) {

			//Arrays.fill(receivedData, (byte) 0);
			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

			String sentence = "";
			String response = "";
			InetAddress ipAddress;
			int port;

			byte[] sendData;
			DatagramPacket sendPacket = null;
			
			while (quantidadeDeJogadores < 2) {
				
				sentence = Comunicacao.receberMensagem(serverSocket);

				if (sentence.equalsIgnoreCase("s") && quantidadeDeJogadores < 2) {
					povoarJogadoresMapeados(receivePacket);
					quantidadeDeJogadores++;

					if (quantidadeDeJogadores == 1) {
						response = "Esperando outro jogador";
					} else {
						response = "O jogo irá começar";
					}

					ipAddress = receivePacket.getAddress();
					port = receivePacket.getPort();
					
					Comunicacao.enviarMensagem(serverSocket, response, ipAddress, port);

				}

				if (quantidadeDeJogadores == 2) {
					
					for (Jogador jogador : jogadoresMapeados.values()) {
                        ipAddress = InetAddress.getByName(jogador.getIp());
                        port = jogador.getPorta();
                        Comunicacao.enviarMensagem(serverSocket, response, ipAddress, port);
                    }
					
//					for (int i = 0; i < quantidadeDeJogadores; i++) {
//						ipAddress = InetAddress.getByName(jogadores[i][0]);
//						port = Integer.parseInt(jogadores[i][1]);
//						
//						Comunicacao.enviarMensagem(serverSocket, response, ipAddress, port);
//					}
				}
				receivePacket = new DatagramPacket(receivedData, receivedData.length);
				receivedData = new byte[1024];
			}

			
			
			if (quantidadeDeJogadores == 2) {
				
				Jogador primeiroJogador = jogadoresMapeados.get(0);
				
				for(Jogador jogador : jogadoresMapeados.values()) {
					if(jogador.getId() == 0) {
						response = "Você será o primeiro jogador";
					} else {
						response = "Aguarde sua vez";
					}
					
//				JogoDaVelha.imprimirTabuleiro(jogo);
//				String jogadorSorteado = JogoDaVelha.sortearOPrimeiroAJogar(jogadores);
//				
//				for (int i = 0; i < 2; i++) {
//					String jogador = jogadores[i][0] + ":" + jogadores[i][1];
//					if (jogador.equals(jogadorSorteado)) {
//						response = "Você será o primeiro jogador";
//					} else {
//						response = "Aguarde sua vez";
//					}
					sendData = response.getBytes();
					Comunicacao.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()), jogador.getPorta());
					
				}

				serverSocket.receive(receivePacket);
				
				JogoDaVelha.iniciar(serverSocket,  receivePacket, jogadoresMapeados, jogo);
			}
		}
	}

//	private static void povoarTuplaDeJogadores(DatagramPacket receivePacket) {
//
//		for (int i = 0; i < jogadores.length; i++) {
//			if (jogadores[i][0] == null) {
//				jogadores[i][0] = receivePacket.getAddress().getHostAddress();
//				jogadores[i][1] = String.valueOf(receivePacket.getPort());
//				break;
//			}
//		}
//	}
	
	 private static void povoarJogadoresMapeados(DatagramPacket receivePacket) {
	        int id = quantidadeDeJogadores;
	        String ip = receivePacket.getAddress().getHostAddress();
	        int porta = receivePacket.getPort();
	        Jogador jogador = new Jogador(id, ip, porta);
	        jogadoresMapeados.put(id, jogador);
	    }
}
