package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.util.Random;
import java.util.Scanner;

public class JogoDaVelha {

	int[][] tabuleiro = new int[3][3];

	public JogoDaVelha() {
		for (int i = 0; i < this.tabuleiro.length; i++) {
			for (int j = 0; j < this.tabuleiro[i].length; j++) {
				this.tabuleiro[i][j] = -1;

			}

		}
	}

	public static void main(String[] args) {
		iniciar();	
	}

	
	public static void iniciar() {
		
		Scanner scanner = new Scanner(System.in);
		int jogadas = 0;
		boolean venceu = false;
		
		while (jogadas < 9 && !venceu) {
//			// Ler do teclado a String a ser enviada
//			System.out.println("Digite o texto a ser enviado");
////			String sentence = keyboardReader.readLine();
//
//			//imprimirTabuleiro(jogo);
			System.out.println("Jogador " + jogadorAtual + ", escolha uma posição: ");
			int posicao = Integer.parseInt(keyboardReader.readLine());

			// Criar o segmento UDP com a String como payload (campo de dados)
			byte[] sendData = ("" + posicao).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);

			clientSocket.send(sendPacket);

			if (jogadaValida(jogo, posicao)) {
				realizarJogada(jogo, posicao, jogadorAtual);

				String sentence = posicao + "";

				jogadas++;
				venceu = verificarVitoria(jogo);
				if (!venceu) {
					trocarJogador();
				} else {
					System.out.println("Jogador " + jogadorAtual + " venceu ");
				}
			} else {
				System.out.println("Posição inválida. Tente novamente.");
			}
		}

		//imprimirTabuleiro(jogo);

		if (venceu) {
			System.out.println("Jogador " + jogadorAtual + " venceu!");
		} else {
			System.out.println("O jogo terminou em empate.");
		}
	}

	public static void imprimirTabuleiro(JogoDaVelha jogo) {
		System.out.println();
		for (int i = 0; i < jogo.getTabuleiro().length; i++) {
			for (int j = 0; j < jogo.getTabuleiro()[i].length; j++) {
				if (jogo.getTabuleiro()[i][j] == -1) {
					System.out.print(" ");
				} else if (jogo.getTabuleiro()[i][j] == 1) {
					System.out.print("X");
				} else if (jogo.getTabuleiro()[i][j] == 0) {
					System.out.print("O");
				} else {
					System.out.print(jogo.getTabuleiro()[i][j]);
				}

				if (j < 2)
					System.out.print(" | ");
			}
			System.out.println();
			if (i < 2)
				System.out.println("---------");
		}
		System.out.println();
	}
	
	public static String sortearOPrimeiroAJogar(String[][] jogadores) {
		Random random = new Random();
		int linhaSorteada = random.nextInt(2);
		String jogadorSorteado =  jogadores[linhaSorteada][0];
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

	private static void trocarJogador() {
		jogadorAtual = (jogadorAtual == 1) ? 0 : 1;
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
