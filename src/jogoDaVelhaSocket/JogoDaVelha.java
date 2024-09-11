package jogoDaVelhaSocket;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.NoSuchElementException;

import jogoDaVelhaSocket.mensagem.EnvioDePacote;
import jogoDaVelhaSocket.utils.FabricaDeMensagem;
//import jogoDaVelhaSocket.mensagem.EnvioDePacote;
//import jogoDaVelhaSocket.mensagem.EnvioDePacote;
//import jogoDaVelhaSocket.mensagem.FabricaDeMensagem;
import jogoDaVelhaSocket.utils.ItemTabuleiro;
import jogoDaVelhaSocket.utils.Mensagem;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.StatusJogo;
import jogoDaVelhaSocket.utils.TipoDeMensagem;


public class JogoDaVelha {

	ItemTabuleiro[][] tabuleiro = new ItemTabuleiro[3][3];
	 HashMap<Integer, Jogador> jogadoresMapeados = new HashMap<>();
	private  String response;
	public  Jogador jogadorMapeado;
	StatusJogo status = StatusJogo.ESPERANDO_JOGADOR;
	int quantidadeDejogadas = 0;
	int jogadas = 0;
	boolean venceu = false;

	public JogoDaVelha() {
		//Linha
		for (int i = 0; i < this.tabuleiro.length; i++) {
			// Coluna
			for (int j = 0; j < this.tabuleiro[i].length; j++) {
				this.tabuleiro[i][j] = ItemTabuleiro.VAZIO;
			}
		}
	}
	
	
	public Pacote executarJogada(Jogada jogada,Pacote dadosCliente, HashMap<Integer, Jogador> jogadores) throws Exception {

		boolean venceu = false;
			if (jogadaValida(jogada)) {

				System.out.println("jogadas " + jogadas);
				
				if (jogadas % 2 == 0) {
					System.out.println("Jogador 'O'");
				    jogadorMapeado = jogadoresMapeados.get(0);  // Jogador 'O'
				} else {
				    jogadorMapeado = jogadoresMapeados.get(1);  // Jogador 'X'
					System.out.println("Jogador 'X'");
				}
				
				realizarJogada(jogada, jogadorMapeado);
				
				jogadas++;
				
				venceu = verificarVitoria();
				
				if (jogadas == 9) {
					response = "============ FIM DE JOGO ==========\n"
							+ "O jogo empatou";
					
					for (Jogador jogador : jogadoresMapeados.values()) {
						Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.jogoEmpatou});
						return new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), mensagem);
					}
					
				} 
				else if(venceu) {
					int jogadorVencedor = informarQuemEOVencedor(jogadoresMapeados);
					String vencedor = jogadorVencedor == 1 ? "X" : "O";
					
					response = "============ FIM DE JOGO ==========\n"
							+ "O jogador " + vencedor + " venceu a partida.\n ==== TABULEIRO FINAL ====";
					
					for (Jogador jogador : jogadoresMapeados.values()) {
						Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.jogadorVenceu, vencedor});
						return new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), mensagem);
					}
				}
				else {
					trocarJogador(jogadoresMapeados);
					
					return new Pacote(InetAddress.getByName(this.jogadorQueVaiEsperar().getIp()), this.jogadorQueVaiEsperar().getPorta(),
									FabricaDeMensagem.getMensagemJogadorEspera(this.imprimirTabuleiro()));
					
				}
			} else {
				Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.jogadaInvalida});
				return new Pacote(dadosCliente.address(), dadosCliente.port(), mensagem);
				
			}
		
			Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.jogadaInvalida});
			return new Pacote(dadosCliente.address(), dadosCliente.port(), mensagem);
	}
	
	public Jogador jogadorQuePodeJogar() {
		return jogadoresMapeados.values()
	            .stream()
	            .filter(Jogador::isSuaVez)
	            .reduce((a, b) -> {
	                throw new IllegalStateException("Mais de um jogador com SuaVez = true");
	            })
	            .orElseThrow(() -> new NoSuchElementException("Nenhum jogador com SuaVez = true"));
	}
	
	public Jogador jogadorQueVaiEsperar() {
		return jogadoresMapeados.values()
	            .stream()
	            .filter(jogador -> !jogador.isSuaVez())
	            .reduce((a, b) -> {
	                throw new IllegalStateException("Mais de um jogador com SuaVez = false");
	            })
	            .orElseThrow(() -> new NoSuchElementException("Nenhum jogador com SuaVez = false"));
	}

	public void enviarMensagemDeFimDeJogo(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket,
			 String response) throws Exception {

		for (Jogador jogador : jogadores.values()) {
			EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
					jogador.getPorta());
		}

		enviarTabuleiroAosJogadores(jogadores, serverSocket);
	}
	
	public void enviarTabuleiroAosJogadores(HashMap<Integer, Jogador> jogadores, DatagramSocket serverSocket) throws Exception {
		String response;
		
		for(Jogador jogador : jogadores.values()) {
			response = this.imprimirTabuleiro();
			EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()),
					jogador.getPorta());
		}
	}
	
	private boolean jogadaValida(Jogada jogada) {	
		System.out.println("jogo:s129 " + this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()]);
		return this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()].equals(ItemTabuleiro.VAZIO) ;
	}

	private void realizarJogada(Jogada jogada, Jogador jogador) {
		System.out.println("j:156 " + jogador.getId());
		this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()] = 
				ItemTabuleiro.mapearValorParaSimbolo(jogador.getId());
		System.out.println(this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()]);
	}

	private boolean verificarVitoria() {
		ItemTabuleiro[][] tabuleiro = this.getTabuleiro();

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

	private int informarQuemEOVencedor(HashMap<Integer, Jogador> jogadores) {
		int jogadorVencedor = -1;
		for(Jogador jogador : jogadoresMapeados.values()) {
			if(jogador.isSuaVez() == true) {
				return jogadorVencedor = jogador.getId();
			}
		}
		return jogadorVencedor;
	}
	
	// PATO
	public String imprimirTabuleiro() {

		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\n");

			for (int i = 0; i < this.getTabuleiro().length; i++) {
				for (int j = 0; j < this.getTabuleiro()[i].length; j++) {
					 String item = this.getTabuleiro()[i][j].getSimbolo();
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
		} catch (Exception e) {
			System.out.println("problema 3");
			return "";
		}
	}
	
	private void trocarJogador(HashMap<Integer, Jogador> jogadores)
			throws Exception {

		for(Jogador jogador : jogadoresMapeados.values()) {

			System.out.println("j:224 " + jogador.isSuaVez());
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
