package jogoDaVelha.mensagem;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import jogoDaVelhaSocket.Jogador;
import jogoDaVelhaSocket.JogoDaVelha;

public class FabricaDeMensagem {
	
	public static void enviarMensagemDeJogadas(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket,
			JogoDaVelha jogo) throws Exception {
		
		String response;
		enviarTabuleiroAosJogadores(jogadores, serverSocket, jogo);
		
		for (Jogador jogador : jogadores.values()) {
			if (jogador.isSuaVez() == true) {
				response = "Sua vez!";
				EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
						jogador.getPorta());
			} else {
				response = "Aguarde sua vez";
				EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
						jogador.getPorta());
			}
		}
	}

	public static void enviarTabuleiroAosJogadores(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket, JogoDaVelha jogo) throws Exception {
		String response;
		
		for(Jogador jogador : jogadores.values()) {
			response = JogoDaVelha.imprimirTabuleiro(jogo);
			EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
					jogador.getPorta());
		}
	}
	
	public static void enviarMensagemDeFimDeJogo(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket,
			JogoDaVelha jogo, String response) throws Exception {

		for (Jogador jogador : jogadores.values()) {
			EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
					jogador.getPorta());
		}

		enviarTabuleiroAosJogadores(jogadores, serverSocket, jogo);
	}

	
}
