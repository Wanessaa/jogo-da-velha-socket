package jogoDaVelhaSocket;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

import jogoDaVelhaSocket.mensagem.EnvioDePacote;
import jogoDaVelhaSocket.mensagem.FabricaDeMensagem;
import jogoDaVelhaSocket.utils.ItemTabuleiro;

public class JogoDaVelha {

	ItemTabuleiro[][] tabuleiro = new ItemTabuleiro[3][3];
	private static String response;
	private static Jogador jogadorMapeado;
	

	public JogoDaVelha() {
		for (int i = 0; i < this.tabuleiro.length; i++) {
			for (int j = 0; j < this.tabuleiro[i].length; j++) {
				this.tabuleiro[i][j] = ItemTabuleiro.VAZIO;
			}
		}
	}
	
	
	public static void iniciar(DatagramSocket serverSocket, DatagramPacket receivePacket, HashMap<Integer, Jogador> jogadores,
			JogoDaVelha jogo) throws Exception {

		int jogadas = 0;
		boolean venceu = false;
		
		
		while (jogadas < 9 && !venceu) {
			
			//String posicao = new String(receivePacket.getData(), 0, receivePacket.getLength());
			Jogada jogada = EnvioDePacote.receberJogada(receivePacket);
			
//			int posicaoInt = Integer.parseInt(posicao);
//			

			if (jogadaValida(jogo, jogada)) {
				
				if (jogadas % 2 == 0) {
				    jogadorMapeado = jogadores.get(0);  // Jogador 'O'
				} else {
				    jogadorMapeado = jogadores.get(1);  // Jogador 'X'
				}
				
				realizarJogada(jogo, jogada, jogadorMapeado);
				
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

	private static boolean jogadaValida(JogoDaVelha jogo, Jogada jogada) {
		
		return jogo.getTabuleiro()[jogada.getLinha()][jogada.getColuna()].equals(ItemTabuleiro.VAZIO) ;		

	}

	private static void realizarJogada(JogoDaVelha jogo, Jogada jogada, Jogador jogador) {
		
		jogo.getTabuleiro()[jogada.getLinha()][jogada.getColuna()] = 
				ItemTabuleiro.mapearValorParaSimbolo(jogador.getId());

	}

	private static boolean verificarVitoria(JogoDaVelha jogo) {
		ItemTabuleiro[][] tabuleiro = jogo.getTabuleiro();

		// Verifica linhas
		for (int i = 0; i < 3; i++) {
			if (tabuleiro[i][0] != ItemTabuleiro.VAZIO && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
				return true;
			}
		} 

		// Verifica colunas
		for (int i = 0; i < 3; i++) {
			if (tabuleiro[0][i] != ItemTabuleiro.VAZIO && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
				return true;
			}
		}

		// Verifica diagonais
		if (tabuleiro[0][0] != ItemTabuleiro.VAZIO && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
			return true;
		}

		if (tabuleiro[0][2] != ItemTabuleiro.VAZIO && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
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
				 String item = jogo.getTabuleiro()[i][j].getSimbolo();
		            stringBuilder.append(item);
				

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

	public ItemTabuleiro[][] getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(ItemTabuleiro[][] tabuleiro) {
		this.tabuleiro = tabuleiro;
	}
}
