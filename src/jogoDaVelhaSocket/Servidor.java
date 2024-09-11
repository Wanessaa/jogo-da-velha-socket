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
				case ESPERANDO_JOGADOR:
					pacote = recepcao.receberMensagem();

					if (pacote == null) {
						// Pause o loop por um breve momento para não consumir CPU desnecessariamente
						Thread.sleep(200); // A pausa evita sobrecarregar o processador
						break;
					}

					if (pacote.message().estaIniciandoOutroJogador() && quantidadeDeJogadores < 2) {
						InetAddress ip = pacote.address();
						int porta = pacote.port();

						// Povoar jogadores mapeados
						int id = quantidadeDeJogadores;
						Jogador jogador = new Jogador(id, ip.getHostAddress(), porta);
						jogo.jogadoresMapeados.put(id, jogador);
						
						quantidadeDeJogadores++;

						transmissao.transmitirPacote(new Pacote(ip, porta,
								FabricaDeMensagem.criarMensagemDeEsperandoJogador(jogo.imprimirTabuleiro())));
					}

					if (quantidadeDeJogadores == 2) {
						
						// Definindo o jogador q vai jogar primeiro
						int num = (int) (Math.random() * 2);
						jogo.jogadoresMapeados.get(num).setSuaVez(true);
						
						
						for (Jogador jogador : jogo.jogadoresMapeados.values()) {
							System.out.println("JOGADOR -> ID" + jogador.getId() + " IP: " + jogador.getIp() + " PORTA: " + jogador.getPorta());

							
							transmissao.transmitirPacote(
									new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(),
											FabricaDeMensagem.getMensagemJogoIniciado(jogo.imprimirTabuleiro())));
						}
						
						

						jogo.status = StatusJogo.JOGO_INICIADO;
					}

					break;
				case JOGO_INICIADO:
					System.out.println("JOGO_INICIADO");
					pacote = recepcao.receberMensagem();


					System.out.println(jogo.quantidadeDejogadas);
					if (jogo.quantidadeDejogadas == 0) {
						
						System.out.println("JOGADOR QUE PODE JOGAR: " + jogo.jogadorQuePodeJogar().getPorta() + " JOGADOR QUE ESPERA: " + jogo.jogadorQueVaiEsperar().getPorta());
						transmissao.transmitirPacote(
								new Pacote(InetAddress.getByName(jogo.jogadorQueVaiEsperar().getIp()), jogo.jogadorQueVaiEsperar().getPorta(),
										FabricaDeMensagem.getMensagemJogadorEspera(jogo.imprimirTabuleiro())));
						
						
						transmissao.transmitirPacote(
								new Pacote(InetAddress.getByName(jogo.jogadorQuePodeJogar().getIp()), jogo.jogadorQuePodeJogar().getPorta(),
										FabricaDeMensagem.getMensagemJogadorFazJogada(jogo.imprimirTabuleiro())));
					}

					
					if (jogo.quantidadeDejogadas < 9 && !jogo.venceu) {

						jogo.status = StatusJogo.JOGO_ESPERANDO_JOGADA;
					} else {
						// jogo encerrado
					}

					break;
				case JOGO_ESPERANDO_JOGADA:
					//System.out.println("JOGO_ESPERANDO_JOGADA");
					pacote = recepcao.receberMensagem();

					if (pacote != null) {

						if (pacote.message().estaEnviandoJogada()
								&& pacote.message().getFields()[1] instanceof Jogada) {
							Jogada jogada = (Jogada) pacote.message().getFields()[1];

							Pacote pacoteJogada = jogo.executarJogada(jogada, pacote, jogo.jogadoresMapeados);
							transmissao.transmitirPacote(pacoteJogada);
							
							 transmissao.transmitirPacote(
										new Pacote(InetAddress.getByName(jogo.jogadorQueVaiEsperar().getIp()), jogo.jogadorQueVaiEsperar().getPorta(),
												FabricaDeMensagem.getMensagemJogadorEspera(jogo.imprimirTabuleiro())));
							
							jogo.status = StatusJogo.JOGO_INICIADO;
						}
					}

					break;

				/*
				 * case esperandoJogador: InetAddress ip; int porta;
				 * 
				 * // Verifica se a fila não está vazia pacote = recepcao.receberMensagem();
				 * 
				 * // Certifique-se de que o pacote não é null if (pacote == null) { // Pause
				 * o loop por um breve momento para não consumir CPU desnecessariamente
				 * Thread.sleep(200); // A pausa evita sobrecarregar o processador break; } else
				 * { System.out.println("Recebeu o pacote: " + pacote.message().getFields()[0]);
				 * }
				 * 
				 * if (pacote.message().estaIniciandoOutroJogador() && quantidadeDeJogadores <
				 * 2) { ip = pacote.address(); porta = pacote.port();
				 * 
				 * povoarJogadoresMapeados(ip.getHostAddress(), porta); quantidadeDeJogadores++;
				 * 
				 * if (quantidadeDeJogadores == 1) { transmissao.transmitirPacote( new
				 * Pacote(ip, porta, FabricaDeMensagem.criarMensagemDeEsperandoJogador())); }
				 * else { transmissao.transmitirPacote( new Pacote(ip, porta,
				 * FabricaDeMensagem.criarMensagemJogadoresProntos())); jogo.status =
				 * StatusJogo.jogoIniciado; } }
				 * 
				 * break;
				 * 
				 * case jogoIniciado: pacote = recepcao.receberMensagem();
				 * 
				 * if (quantidadeDeJogadores == 2) { // Imprimir tabuleiro para os jogadores for
				 * (Jogador jogador : jogadoresMapeados.values()) {
				 * transmissao.transmitirPacote( new
				 * Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(),
				 * FabricaDeMensagem.criarMensagemTabuleiro(jogo.imprimirTabuleiro()))); }
				 * 
				 * for (Jogador jogador : jogadoresMapeados.values()) {
				 * 
				 * if (jogador.getId() == 0) { // Mensagem para o jogador que inicia
				 * jogador.setSuaVez(true); transmissao.transmitirPacote(new
				 * Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(),
				 * FabricaDeMensagem.criarMensagemJogadorInicia())); // String tabuleiroVazio =
				 * jogo.imprimirTabuleiro(); jogo.status = StatusJogo.esperandoJogada; } else {
				 * // vez do proximo jogador transmissao.transmitirPacote(new
				 * Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(),
				 * FabricaDeMensagem.criarMensagemJogadorEspera())); // jogo.status =
				 * StatusJogo.esperandoJogada; } } }
				 * 
				 * if (jogo.quantidadeDejogadas < 9 && !jogo.venceu) {
				 * 
				 * } else { // jogo encerrado } break;
				 * 
				 * case esperandoJogada: System.out.println("esperando jogada"); pacote =
				 * recepcao.receberMensagem();
				 * 
				 * Jogada jogada = null; if (pacote != null) {
				 * 
				 * if (pacote.message().estaEnviandoJogada() && pacote.message().getFields()[1]
				 * instanceof Jogada) { jogada = (Jogada) pacote.message().getFields()[1];
				 * 
				 * Pacote pacoteJogada = jogo.executarJogada(jogada, pacote, jogadoresMapeados);
				 * transmissao.transmitirPacote(pacoteJogada); jogo.status =
				 * StatusJogo.esperandoJogada; } }
				 * 
				 * for (Jogador jogador : jogadoresMapeados.values()) { if (jogador.isSuaVez()
				 * == false) { Mensagem mensagem = new Mensagem(new Object[] {
				 * TipoDeMensagem.aguardeSuaVez }); Pacote messageAguardeSuaVez = new
				 * Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), mensagem);
				 * transmissao.transmitirPacote(messageAguardeSuaVez); }
				 * 
				 * // transmissao.transmitirPacote(new
				 * Pacote(InetAddress.getByName(jogador.getIp()), // jogador.getPorta(),
				 * FabricaDeMensagem.criarMensagemSuaVez()));
				 * 
				 * String tabuleiro = jogo.imprimirTabuleiro(); //
				 * System.out.println(tabuleiro); // System.out.println("qualquer coisa aí 3");
				 * 
				 * transmissao.transmitirPacote(new
				 * Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(),
				 * FabricaDeMensagem.criarMensagemTabuleiro(tabuleiro))); }
				 * 
				 * // Jogada jogada = null; // if (pacote != null) { // // if
				 * (pacote.message().estaEnviandoJogada() // && pacote.message().getFields()[1]
				 * instanceof Jogada) { // jogada = (Jogada) pacote.message().getFields()[1]; //
				 * // Pacote pacoteJogada = jogo.executarJogada(jogada, pacote,
				 * jogadoresMapeados); // transmissao.transmitirPacote(pacoteJogada); //
				 * jogo.status = StatusJogo.jogoIniciado; // // } // // }
				 * 
				 * break; case enviandoTabuleiro:
				 * 
				 * break; case jogoEncerrado:
				 * 
				 * break;
				 */
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Espera de 1 segundo antes de continuar o loop
			Thread.sleep(1000);
		}
	}

	private void povoarJogadoresMapeados(String ip, int porta) {
		
	}
}
