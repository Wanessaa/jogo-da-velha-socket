package jogoDaVelhaSocket;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.NoSuchElementException;

import jogoDaVelhaSocket.entidade.Jogada;
import jogoDaVelhaSocket.entidade.Jogador;
import jogoDaVelhaSocket.mensagem.FabricaDeMensagem;
import jogoDaVelhaSocket.mensagem.Mensagem;
import jogoDaVelhaSocket.utils.ItemTabuleiro;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.StatusJogo;
import jogoDaVelhaSocket.utils.TipoDeMensagem;


public class JogoDaVelha {

	ItemTabuleiro[][] tabuleiro = new ItemTabuleiro[3][3];
	 HashMap<Integer, Jogador> jogadoresMapeados = new HashMap<>();
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
				    jogadorMapeado = jogadoresMapeados.get(0);  // Jogador 'O'
				} else {
				    jogadorMapeado = jogadoresMapeados.get(1);  // Jogador 'X'
				}
				
				realizarJogada(jogada, jogadorMapeado);
				trocarJogador(jogadoresMapeados);
				
				jogadas++;
				
				venceu = verificarVitoria();
				
				if (jogadas == 9) {
					
//					for (Jogador jogador : jogadoresMapeados.values()) {
//						Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.JOGO_ENCERRADO_EMPATOU.ordinal()});
//						return new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), mensagem);
//					}
					return new Pacote(InetAddress.getByName(this.jogadorQueVaiEsperar().getIp()), this.jogadorQueVaiEsperar().getPorta(),
									FabricaDeMensagem.criarMensagemJogoEncerradoEmpatou(this.imprimirTabuleiro()));
					
				} 
				else if(venceu) {
					int jogadorVencedor = informarQuemEOVencedor(jogadoresMapeados);
					String vencedor = jogadorVencedor == 1 ? "X" : "0";
					return new Pacote(InetAddress.getByName(this.jogadorQueVaiEsperar().getIp()), this.jogadorQueVaiEsperar().getPorta(),
									FabricaDeMensagem.criarMensagemJogoEncerradoVenceu(this.imprimirTabuleiro(), vencedor));
				}
				else {
					return new Pacote(InetAddress.getByName(this.jogadorQueVaiEsperar().getIp()), this.jogadorQueVaiEsperar().getPorta(),
									FabricaDeMensagem.criarMensagemJogadorEspera(this.imprimirTabuleiro()));
				}
			} else {
				Mensagem mensagem = new Mensagem(new Object[] { TipoDeMensagem.JOGADA_INVALIDA.ordinal()});
				return new Pacote(dadosCliente.address(), dadosCliente.port(), mensagem);
			}
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

	private boolean jogadaValida(Jogada jogada) {	
		return this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()].equals(ItemTabuleiro.VAZIO) ;
	}

	private void realizarJogada(Jogada jogada, Jogador jogador) {
		this.getTabuleiro()[jogada.getLinha()][jogada.getColuna()] = 
				ItemTabuleiro.mapearValorParaSimbolo(jogador.getId());
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
			if(jogador.isSuaVez() == false) {
				return jogadorVencedor = jogador.getId();
			}
		}
		return jogadorVencedor;
	}
	
	public String informarQuemEOVencedor() {
		int vencedor = informarQuemEOVencedor(jogadoresMapeados);
		return vencedor == 1 ? "X" : "O";
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
				if (i < 2)
					stringBuilder.append("\n---------\n");
			}
			stringBuilder.append("\n");
			return stringBuilder.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	private void trocarJogador(HashMap<Integer, Jogador> jogadores)
			throws Exception {

		for(Jogador jogador : jogadoresMapeados.values()) {
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
