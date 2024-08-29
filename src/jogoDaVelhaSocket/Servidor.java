package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

import jogoDaVelha.mensagem.EnvioDePacote;

public class Servidor {

	static int quantidadeDeJogadores = 0;	
	static HashMap<Integer, Jogador> jogadoresMapeados = new HashMap<>();

	public static void main(String args[]) throws Exception {

		JogoDaVelha jogo = new JogoDaVelha();

		DatagramSocket serverSocket = new DatagramSocket(80);
		System.out.println("Servidor iniciado!");

		byte[] receivedData = new byte[1024];

		while (true) {

			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

			String sentence = "";
			String response = "";
			InetAddress ipAddress;
			int port;

			byte[] sendData;
			
			while (quantidadeDeJogadores < 2) {
				
				sentence = EnvioDePacote.receberMensagem(serverSocket, receivePacket);
				
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
					
					EnvioDePacote.enviarMensagem(serverSocket, response, ipAddress, port);

				}

				if (quantidadeDeJogadores == 2) {
					
					for (Jogador jogador : jogadoresMapeados.values()) {
                        ipAddress = InetAddress.getByName(jogador.getIp());
                        port = jogador.getPorta();
                        EnvioDePacote.enviarMensagem(serverSocket, response, ipAddress, port);
                    }
				}
				receivePacket = new DatagramPacket(receivedData, receivedData.length);
				receivedData = new byte[1024];
			}

			if (quantidadeDeJogadores == 2) {
				
				for(Jogador jogador : jogadoresMapeados.values()) {
					if(jogador.getId() == 0) {
						response = "Você será o primeiro jogador";
						jogador.setSuaVez(true);
					} else {
						response = "Aguarde sua vez";
					}
					
					sendData = response.getBytes();
					EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()), jogador.getPorta());
					
				}

				serverSocket.receive(receivePacket);
				
				JogoDaVelha.iniciar(serverSocket,  receivePacket, jogadoresMapeados, jogo);
			}
		}
	}

	 private static void povoarJogadoresMapeados(DatagramPacket receivePacket) {
	        int id = quantidadeDeJogadores;
	        String ip = receivePacket.getAddress().getHostAddress();
	        int porta = receivePacket.getPort();
	        Jogador jogador = new Jogador(id, ip, porta);
	        jogadoresMapeados.put(id, jogador);
	    }
}
