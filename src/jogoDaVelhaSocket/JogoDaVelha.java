package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class JogoDaVelha {

	int[][] tabuleiro = new int[3][3];
	private static String jogadorSorteado = "";
	private static String[][] jogadorAtual = new String[1][2];
	private static int jogadorZero;
	private static int jogadorUm;
	private static int proximoJogador;
	private static String response;
	private static byte[] sendData;
	private static HashMap<Integer, String> jogadoresMapeadosDeAcordoComEnderecoIP = new HashMap<>();
	private static int jogadorMapeado;
	
	private static String[] jogadorSorteadoTupla = jogadorSorteado.split(":");

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

		Scanner scanner = new Scanner(System.in);
		int jogadas = 0;
		boolean venceu = false;
		
		//Determinar o jogador que está tentando efetuar a jogada
		jogadorAtual[0][0] = receivePacket.getAddress().getHostAddress();
		jogadorAtual[0][1] = String.valueOf(receivePacket.getPort());

//		//Mapear os jogadores para os valores 0 e 1, 0 sendo o jogador sorteado e 1 o não sorteado
//		jogadoresMapeadosDeAcordoComEnderecoIP.put(0, jogadorSorteadoTupla[0]);
//		mapearJogadoresDeAcordoComEnderecoIP(jogadores, jogadoresMapeadosDeAcordoComEnderecoIP);
//		
//		for(HashMap.Entry<Integer, String> entry: jogadoresMapeadosDeAcordoComEnderecoIP.entrySet()) {
//			if(entry.getValue().equals(jogadorAtual[0][0])) {
//				jogadorMapeado = entry.getKey();
//			}
//		}
		
		mapearJogadoresDeAcordoComEnderecoIP(jogadores, jogadorSorteadoTupla);
		
		while (jogadas < 9 && !venceu) {

			String posicao = new String(receivePacket.getData(), 0, receivePacket.getLength());
			int posicaoInt = Integer.parseInt(posicao);
			

			if (jogadaValida(jogo, posicaoInt)) {
				realizarJogada(jogo, posicaoInt);
				
				jogadas++;
				venceu = verificarVitoria(jogo);
				if (!venceu) {
					trocarJogador(jogadores, receivePacket, serverSocket, jogo);
				} else {
					System.out.println("Jogador " + jogadorAtual + " venceu ");
				}
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

	private static void mapearJogadoresDeAcordoComEnderecoIP(String[][] jogadores, String[] jogadorSorteadoTupla) {
	    jogadoresMapeadosDeAcordoComEnderecoIP.put(0, jogadorSorteadoTupla[0]);
	    for (String[] jogador : jogadores) {
	        if (!jogador[0].equals(jogadorSorteadoTupla[0])) {
	            jogadoresMapeadosDeAcordoComEnderecoIP.put(1, jogador[0]);
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

	private static void realizarJogada(JogoDaVelha jogo, int posicao) {
		int jogador = jogadorMapeado;
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
		String jogadorAtualIP = "";
		int jogadorAtualPorta = -1;
		for (int i = 0; i < 1; i++) {
			if (jogadorAtual[0][0].equals(jogadores[i][0]) && jogadorAtual[i][1].equals(jogadores[i][1])) {
				jogadorAtualIP = jogadores[i][0];
				jogadorAtualPorta = Integer.parseInt(jogadores[i][1]);
				break;
			}
//			} else {
//				jogadorAtualIP = jogadores[1][0];
//				jogadorAtualPorta = Integer.parseInt(jogadores[1][1]);
//			}
		}
		if (jogadorAtualPorta == -1) {
	        throw new Exception("Jogador atual não encontrado.");
	    }
		
		if (jogadorSorteadoTupla[0].equals(jogadorAtual[0][0]) && jogadorSorteadoTupla[1].equals(jogadorAtual[0][1])) {
	        proximoJogador = 1; // Jogador não sorteado
	    } else {
	        proximoJogador = 0; // Jogador sorteado
	    }

		if (jogadorSorteadoTupla[0].equals(jogadorAtual[0][0]) && jogadorSorteadoTupla[1].equals(jogadorAtual[0][1])) {
			// mensagem para quem jogou
			proximoJogador = jogadorUm;
			enviarMensagem(proximoJogador, jogadorAtualIP, jogadorAtualPorta, receivePacket, serverSocket, jogo);
		} else {
			// mensagem para quem vai jogar
			proximoJogador = jogadorZero;
			enviarMensagem(proximoJogador, jogadorAtualIP, jogadorAtualPorta, receivePacket, serverSocket, jogo);
		}
	}

	private static void enviarMensagem(int quemEOProximoJogador, String jogadorAtualIP, 
			int jogadorAtualPorta, DatagramPacket receivePacket, DatagramSocket serverSocket, JogoDaVelha jogo) throws Exception {
		
		DatagramPacket sendPacket;
		
		//imprimir tabuleiro para ambos os jogadores
		response = imprimirTabuleiro(jogo);
		sendData = response.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
		serverSocket.send(sendPacket);
		
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(jogadorAtualIP),jogadorAtualPorta);
		serverSocket.send(sendPacket);
		
//		if (quemEOProximoJogador == 1) {
			response = "Aguarde sua vez";
			sendData = response.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
			serverSocket.send(sendPacket);

			proximoJogador = jogadorZero;
			response = "Sua vez";
			sendData = response.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(jogadorAtualIP),jogadorAtualPorta);
			serverSocket.send(sendPacket);
//		}
		
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
