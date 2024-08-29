package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

import jogoDaVelha.mensagem.EnvioDePacote;
import jogoDaVelha.mensagem.FabricaDeMensagem;

public class JogoDaVelha {

	int[][] tabuleiro = new int[3][3];
	private static String response;
	//private static byte[] sendData;
	private static Jogador jogadorMapeado;
	

	public JogoDaVelha() {
		for (int i = 0; i < this.tabuleiro.length; i++) {
			for (int j = 0; j < this.tabuleiro[i].length; j++) {
				this.tabuleiro[i][j] = -1;
			}
		}
	}
	
	
	public static void iniciar(DatagramSocket serverSocket, DatagramPacket receivePacket, HashMap<Integer, Jogador> jogadores,
			JogoDaVelha jogo) throws Exception {

		int jogadas = 0;
		boolean venceu = false;
		
		
		while (jogadas < 9 && !venceu) {
			
			String posicao = new String(receivePacket.getData(), 0, receivePacket.getLength());
			int posicaoInt = Integer.parseInt(posicao);
			

			if (jogadaValida(jogo, posicaoInt)) {
				
				if (jogadas % 2 == 0) {
				    jogadorMapeado = jogadores.get(0);  // Jogador 'O'
				} else {
				    jogadorMapeado = jogadores.get(1);  // Jogador 'X'
				}
				
				realizarJogada(jogo, posicaoInt, jogadorMapeado);
				
				jogadas++;
				
				venceu = verificarVitoria(jogo);
				
				if (jogadas == 9) {
					response = "============ FIM DE JOGO ==========\n"
							+ "O jogo empatou";
					FabricaDeMensagem.enviarMensagemDeFimDeJogo(jogadores, serverSocket, jogo, response);
					serverSocket.close();
				} 
				else if(venceu) {
					int jogadorVencedor = informarQuemEOVencedor(jogadores);
					String vencedor = jogadorVencedor == 1 ? "X" : "O";
					
					response = "============ FIM DE JOGO ==========\n"
							+ "O jogador " + vencedor + " venceu a partida.\n ==== TABULEIRO FINAL ====";
					
					FabricaDeMensagem.enviarMensagemDeFimDeJogo(jogadores, serverSocket, jogo, response);

					serverSocket.close();
				}
				else {
					trocarJogador(jogadores, serverSocket);
					FabricaDeMensagem.enviarMensagemDeJogadas(jogadores, serverSocket, jogo);
					serverSocket.receive(receivePacket);
				}
			} else {
				response = "Posição inválida. Tente novamente.";
				EnvioDePacote.enviarMensagem(serverSocket, response, receivePacket.getAddress(), receivePacket.getPort());
				serverSocket.receive(receivePacket);
			}
		}
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

	private static void realizarJogada(JogoDaVelha jogo, int posicao, Jogador jogador) {
		switch (posicao) {
		case 1:
			jogo.getTabuleiro()[0][0] = jogador.getId();
			break;
		case 2:
			jogo.getTabuleiro()[0][1] = jogador.getId();
			break;
		case 3:
			jogo.getTabuleiro()[0][2] = jogador.getId();
			break;
		case 4:
			jogo.getTabuleiro()[1][0] = jogador.getId();
			break;
		case 5:
			jogo.getTabuleiro()[1][1] = jogador.getId();
			break;
		case 6:
			jogo.getTabuleiro()[1][2] = jogador.getId();
			break;
		case 7:
			jogo.getTabuleiro()[2][0] = jogador.getId();
			break;
		case 8:
			jogo.getTabuleiro()[2][1] = jogador.getId();
			break;
		case 9:
			jogo.getTabuleiro()[2][2] = jogador.getId();
			break;
		default:
			break;
		}

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

	private static int informarQuemEOVencedor(HashMap<Integer, Jogador> jogadores) {
		int jogadorVencedor = -1;
		for(Jogador jogador : jogadores.values()) {
			if(jogador.isSuaVez() == true) {
				return jogadorVencedor = jogador.getId();
			}
		}
		return jogadorVencedor;
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
	
	private static void trocarJogador(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket)
			throws Exception {

		for(Jogador jogador : jogadores.values()) {
			if(jogador.isSuaVez() == true) {
				jogador.setSuaVez(false);
			} else {
				jogador.setSuaVez(true);
			}
		}
	}

	public int[][] getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(int[][] tabuleiro) {
		this.tabuleiro = tabuleiro;
	}
}
