package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class JogoDaVelha {

	int[][] tabuleiro = new int[3][3];
	private static String jogadorSorteado = "";
	private static String[][] jogadorAtual = new String[1][2];
	private static int proximoJogador;
	private static String response;
	private static byte[] sendData;
	private static HashMap<Integer, String> jogadoresMapeadosDeAcordoComEnderecoIP = new HashMap<>();
	private static int jogadorMapeado;
	private static String[] jogadorSorteadoTupla;
	private static String[][] dadosDoProximoJogador = new String[1][2];
	

	public JogoDaVelha() {
		for (int i = 0; i < this.tabuleiro.length; i++) {
			for (int j = 0; j < this.tabuleiro[i].length; j++) {
				this.tabuleiro[i][j] = -1;
			}
		}
	}

	public static void main(String[] args) {
		// iniciar();
	}
	
	public static void iniciar(DatagramSocket serverSocket, DatagramPacket receivePacket, String[][] jogadores,
			JogoDaVelha jogo) throws Exception {

		//Scanner scanner = new Scanner(System.in);
		int jogadas = 0;
		boolean venceu = false;
		
		mapearJogadoresDeAcordoComEnderecoIP(jogadores, jogadorSorteadoTupla);
		
		while (jogadas < 9 && !venceu) {

			//Determinar o jogador que está tentando efetuar a jogada
			jogadorAtual[0][0] = receivePacket.getAddress().getHostAddress();
			jogadorAtual[0][1] = String.valueOf(receivePacket.getPort());
			
			String posicao = new String(receivePacket.getData(), 0, receivePacket.getLength());
			int posicaoInt = Integer.parseInt(posicao);
			

			if (jogadaValida(jogo, posicaoInt)) {
				
				//Mapear jogar que está jogando
				if(jogadas < 1) {
					jogadorMapeado = 0;
				} else {
					if(proximoJogador == 1) {
						jogadorMapeado = 0;
					}else {
						jogadorMapeado = 1;
					}
				}
				
				realizarJogada(jogo, posicaoInt, jogadorMapeado);
				
				jogadas++;
				trocarJogador(jogadores, receivePacket, serverSocket, jogo);
				
				venceu = verificarVitoria(jogo);
				
				if (venceu) {
					//enviarMensagem(proximoJogador, receivePacket.getAddress().getHostAddress(), receivePacket.getPort(), receivePacket, serverSocket, jogo, dadosDoProximoJogador);
					
					enviarMensagemDeVencedor(proximoJogador, receivePacket, serverSocket, jogo, dadosDoProximoJogador);
					
					
					serverSocket.close();
				} 
//				else {
//					System.out.println("Jogador " + jogadorAtual + " venceu ");
//				}
			} else {
				response = "Posição inválida. Tente novamente.";
			}
		}


		if (venceu) {
			System.out.println("Jogador " + jogadorAtual[0][0] + " venceu!");
		} else {
			System.out.println("O jogo terminou em empate.");
		}
	}



	/***
	 *Método para enviar mensagem de quem foi o jogador vencedor
	***/
	private static void enviarMensagemDeVencedor(int proximoJogador2, DatagramPacket receivePacket,  DatagramSocket serverSocket, JogoDaVelha jogo,
			String[][] dadosDoProximoJogador2) throws Exception {

		int jogadorVencedor = -1;
		if(proximoJogador == 1) {
			jogadorVencedor = 0;
		} else {
			jogadorVencedor = 1;
		}
		
		response = "O jogador " + jogadorVencedor + "venceu a partida.\n"
				+ "============ FIM DE JOGO ==========";
		sendData = response.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
		serverSocket.send(sendPacket);
		
		String portaEmString = dadosDoProximoJogador[0][1];
		int portaDoProximoJogador = Integer.parseInt(portaEmString);

		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(dadosDoProximoJogador[0][0]),portaDoProximoJogador);
		serverSocket.send(sendPacket);
		serverSocket.receive(receivePacket);
		
	}

	private static void mapearJogadoresDeAcordoComEnderecoIP(String[][] jogadores, String[] jogadorSorteadoTupla) {
	    jogadoresMapeadosDeAcordoComEnderecoIP.put(0, jogadorSorteadoTupla[0]);
	    for (int i = 0; i < jogadores.length; i++) {
	        // Acessar o primeiro valor da linha (endereço IP)
	        String enderecoIP = jogadores[i][0]; 
	        System.out.println("enderecoip " + enderecoIP);
	        // Verificar se o endereço IP é diferente do sorteado
	        if (!enderecoIP.equals(jogadorSorteadoTupla[0])) {
	            jogadoresMapeadosDeAcordoComEnderecoIP.put(1, enderecoIP);
	            System.out.println("Endereco Ip setado");
	        }
	    }
	}

	public static String imprimirTabuleiro(JogoDaVelha jogo) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");

		for (int i = 0; i < jogo.getTabuleiro().length; i++) {
			for (int j = 0; j < jogo.getTabuleiro()[i].length; j++) {
				if (jogo.getTabuleiro()[i][j] == -1) {
					stringBuilder.append(" ");
				} else if (jogo.getTabuleiro()[i][j] == 1) {
					stringBuilder.append("X");
				} else if (jogo.getTabuleiro()[i][j] == 0) {
					stringBuilder.append("O");
				} else {
					stringBuilder.append(jogo.getTabuleiro()[i][j]);
				}

				if (j < 2)
					stringBuilder.append(" | ");
			}
			System.out.println();
			if (i < 2)
				stringBuilder.append("\n---------\n");
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	public static String sortearOPrimeiroAJogar(String[][] jogadores) {
		Random random = new Random();
		int linhaSorteada = random.nextInt(2);
		jogadorSorteado = jogadores[linhaSorteada][0] + ":" + jogadores[linhaSorteada][1];
		jogadorSorteadoTupla = jogadorSorteado.split(":");
		
		return jogadorSorteado;
	}

	private static boolean jogadaValida(JogoDaVelha jogo, int posicao) {
		switch (posicao) {
		case 1:
			return jogo.getTabuleiro()[0][0] == -1;
		case 2:
			return jogo.getTabuleiro()[0][1] == -1;
		case 3:
			return jogo.getTabuleiro()[0][2] == -1;
		case 4:
			return jogo.getTabuleiro()[1][0] == -1;
		case 5:
			return jogo.getTabuleiro()[1][1] == -1;
		case 6:
			return jogo.getTabuleiro()[1][2] == -1;
		case 7:
			return jogo.getTabuleiro()[2][0] == -1;
		case 8:
			return jogo.getTabuleiro()[2][1] == -1;
		case 9:
			return jogo.getTabuleiro()[2][2] == -1;
		default:
			return false;
		}
	}

	private static void realizarJogada(JogoDaVelha jogo, int posicao, int jogador) {
		//int jogador = jogadorMapeado;
		switch (posicao) {
		case 1:
			jogo.getTabuleiro()[0][0] = jogador;
			break;
		case 2:
			jogo.getTabuleiro()[0][1] = jogador;
			break;
		case 3:
			jogo.getTabuleiro()[0][2] = jogador;
			break;
		case 4:
			jogo.getTabuleiro()[1][0] = jogador;
			break;
		case 5:
			jogo.getTabuleiro()[1][1] = jogador;
			break;
		case 6:
			jogo.getTabuleiro()[1][2] = jogador;
			break;
		case 7:
			jogo.getTabuleiro()[2][0] = jogador;
			break;
		case 8:
			jogo.getTabuleiro()[2][1] = jogador;
			break;
		case 9:
			jogo.getTabuleiro()[2][2] = jogador;
			break;
		default:
			break;
		}

	}

	private static void trocarJogador(String[][] jogadores, DatagramPacket receivePacket, DatagramSocket serverSocket, JogoDaVelha jogo)
			throws Exception {
		//String[] jogadorSorteadoTupla = jogadorSorteado.split(":");

		// para saber qual o endereco do outro jogador
		String jogadorAtualIP = receivePacket.getAddress().getHostAddress();
		int jogadorAtualPorta = receivePacket.getPort(); 
	
		if (jogadorAtualPorta == -1) {
	        throw new Exception("Jogador atual não encontrado.");
	    }
		
		if (jogadorSorteadoTupla[0].equals(jogadorAtual[0][0]) && jogadorSorteadoTupla[1].equals(jogadorAtual[0][1])) {
	        proximoJogador = 1; // Jogador não sorteado
	    } else {
	        proximoJogador = 0; // Jogador sorteado
	    }
		System.out.println("O proximo jogador e " +proximoJogador);
		mapearDadosDoProximoJogador(jogadores, proximoJogador);

		enviarMensagem(proximoJogador, jogadorAtualIP, jogadorAtualPorta, receivePacket, serverSocket, jogo, dadosDoProximoJogador);
		
		 
	}

	private static void mapearDadosDoProximoJogador(String[][] jogadores, int proximoJogador) {
		
		//Pegar o endereço IP do próximo jogar a partir da verificação de chave valor no dicionario
		//Se a chave for igual ao valor da variavel proximo jogador pega o valor par da chave no dicionário e seta em dadosDoProximoJogador
		for (Map.Entry<Integer, String> jogadoresMapeados : jogadoresMapeadosDeAcordoComEnderecoIP.entrySet()) {
		    Integer chave = jogadoresMapeados.getKey();
		    System.out.println(chave);
		    String enderecoIP = jogadoresMapeados.getValue();
		    System.out.println(enderecoIP);
		    System.out.println(jogadoresMapeadosDeAcordoComEnderecoIP.size());
		    if(chave.equals(proximoJogador)) {
		    	dadosDoProximoJogador[0][0] = enderecoIP;
		    	System.err.println(dadosDoProximoJogador[0][0]);
		    	//break;
		    }
		}
		
		//Pegar porta do próximo jogador comparando o endereço IP já setado com os endereços IPs presentes na matriz jogadores
		for(int i = 0; i < 2; i++) {
			if(jogadores[i][0].equals(dadosDoProximoJogador[0][0])) {
				dadosDoProximoJogador[0][1] = jogadores[i][1];
			}
		}
	}

	private static void enviarMensagem(int quemEOProximoJogador, String jogadorAtualIP, 
			int jogadorAtualPorta, DatagramPacket receivePacket, DatagramSocket serverSocket, JogoDaVelha jogo, String[][] dadosDoProximoJogador) throws Exception {
		
		DatagramPacket sendPacket;
		
		//Convertendo valor da porta do próximo jogador de String para inteiro
		String portaEmString = dadosDoProximoJogador[0][1];
		int portaDoProximoJogador = Integer.parseInt(portaEmString);
		
		
		//Imprimir tabuleiro para ambos os jogadores 
		response = imprimirTabuleiro(jogo);
		sendData = response.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
		serverSocket.send(sendPacket);
		
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(dadosDoProximoJogador[0][0]),portaDoProximoJogador);
		serverSocket.send(sendPacket);
		
		
		//Enviar mensagens para os respectivos jogadores
			response = "Aguarde sua vez";
			sendData = response.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
			serverSocket.send(sendPacket);

			response = "Sua vez";
			sendData = response.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(dadosDoProximoJogador[0][0]),portaDoProximoJogador);
			serverSocket.send(sendPacket);
			serverSocket.receive(receivePacket);
	}
	

	private static boolean verificarVitoria(JogoDaVelha jogo) {
		int[][] tabuleiro = jogo.getTabuleiro();

		// Verifica linhas
		for (int i = 0; i < 3; i++) {
			if (tabuleiro[i][0] != -1 && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
				return true;
			}
		} 

		// Verifica colunas
		for (int i = 0; i < 3; i++) {
			if (tabuleiro[0][i] != -1 && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
				return true;
			}
		}

		// Verifica diagonais
		if (tabuleiro[0][0] != -1 && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
			return true;
		}

		if (tabuleiro[0][2] != -1 && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
			return true;
		}

		return false;
	}

	public int[][] getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(int[][] tabuleiro) {
		this.tabuleiro = tabuleiro;
	}
}
