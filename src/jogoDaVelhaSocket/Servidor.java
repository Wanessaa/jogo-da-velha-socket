package jogoDaVelhaSocket;
/**
 * 
 * implementar logica para aparecer a opção de jogada para os jogadas por vez 
 * implementar o tabuleiro para aparecer antes da primeira jogada
 * implementar o tabuleiro para os dois jogadores apos cada jogada
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import jogoDaVelhaSocket.mensagem.EnvioDePacote;
import jogoDaVelhaSocket.utils.FabricaDeMensagem;
import jogoDaVelhaSocket.utils.Mensagem;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.Recepcao;
import jogoDaVelhaSocket.utils.StatusJogo;
import jogoDaVelhaSocket.utils.TipoDeMensagem;
import jogoDaVelhaSocket.utils.Transmissao;

public class Servidor {

	static int quantidadeDeJogadores = 0;	
	static HashMap<Integer, Jogador> jogadoresMapeados = new HashMap<>();

	public static void main(String args[]) throws Exception {

		DatagramSocket socket = new DatagramSocket(80);
		System.out.println("Servidor iniciado");
		Recepcao recepcao = new Recepcao(socket);
		Transmissao transmissao = new Transmissao(socket);
		JogoDaVelha jogo = new JogoDaVelha();


		Pacote pacote = null;

		while (true) {
			try {
				switch (jogo.status) {
				case esperandoJogador:
					InetAddress ip;
					int porta;

					// Verifica se a fila não está vazia
					pacote = recepcao.receberMensagem();

					// Certifique-se de que o pacote não é null
					if (pacote == null) {
						// Pause o loop por um breve momento para não consumir CPU desnecessariamente
						Thread.sleep(200);  // A pausa evita sobrecarregar o processador
						break;
					} else {
						System.out.println("Recebeu o pacote: " + pacote.message().getFields()[0]);
					}

					if (pacote.message().estaIniciandoOutroJogador() && quantidadeDeJogadores < 2) {
						ip = pacote.address();
						porta = pacote.port();

						povoarJogadoresMapeados(ip.getHostAddress(), porta);
						quantidadeDeJogadores++;

						
						if (quantidadeDeJogadores == 1) {
							transmissao.transmitirPacote(new Pacote(ip, porta, FabricaDeMensagem.criarMensagemDeEsperandoJogador()));
						} else {
							transmissao.transmitirPacote(new Pacote(ip, porta, FabricaDeMensagem.criarMensagemJogadoresProntos()));
							jogo.status = StatusJogo.jogoIniciado;
						}
					}


					break;

				case jogoIniciado:
					pacote = recepcao.receberMensagem();

					if (quantidadeDeJogadores == 2 ) {
						transmissao.transmitirPacote(new Pacote(pacote.address(),pacote.port(), FabricaDeMensagem.criarMensagemTabuleiro(jogo.imprimirTabuleiro())));
						for (Jogador jogador : jogadoresMapeados.values()) {

							if (jogador.getId() == 0) {
								//quando é sua vez 
								transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemJogadorInicia()));
								String tabuleiroVazio = jogo.imprimirTabuleiro();
								jogador.setSuaVez(true);


							} else {
								//vez do proximo jogador
								transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemJogadorEspera()));
								jogo.status = StatusJogo.esperandoJogada;
							}
						} 

					}

					if(jogo.quantidadeDejogadas < 9 && !jogo.venceu) {
						


					}else {
						//jogo encerrado
					}
					break;
				case esperandoJogada:
					System.out.println("esperando jogada");
					pacote = recepcao.receberMensagem();

					for (Jogador jogador : jogadoresMapeados.values()) {
						transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemSuaVez()));
						                       		String tabuleiro = jogo.imprimirTabuleiro();
						                        		System.out.println(tabuleiro);
						                        		System.out.println("qualquer coisa aí 3");
						                        	 transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemTabuleiro(tabuleiro)));
					}
					
					Jogada jogada = null;
					if (pacote != null) {

						if (pacote.message().estaEnviandoJogada()&& pacote.message().getFields()[1] instanceof Jogada) {
							jogada = (Jogada) pacote.message().getFields()[1];
							
							Pacote pacoteJogada = jogo.executarJogada(jogada, pacote, jogadoresMapeados);
							transmissao.transmitirPacote(pacoteJogada);
							jogo.status = StatusJogo.jogoIniciado;
							
						}

					} 


					break;
				case enviandoTabuleiro:
					
					
					break;
				case jogoEncerrado:

					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Espera de 1 segundo antes de continuar o loop
			Thread.sleep(1000);
		}
	}

	private static void povoarJogadoresMapeados(String ip, int porta) {
		int id = quantidadeDeJogadores;
		Jogador jogador = new Jogador(id, ip, porta);
		jogadoresMapeados.put(id, jogador);
	}
}
