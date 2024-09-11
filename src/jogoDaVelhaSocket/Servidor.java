package jogoDaVelhaSocket;

import java.net.DatagramSocket;
import java.net.InetAddress;

import jogoDaVelhaSocket.entidade.Jogada;
import jogoDaVelhaSocket.entidade.Jogador;
import jogoDaVelhaSocket.mensagem.FabricaDeMensagem;
import jogoDaVelhaSocket.thread.Recepcao;
import jogoDaVelhaSocket.thread.Transmissao;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.StatusJogo;

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
						
						Thread.sleep(200); 
						break;
					}

					if (pacote.message().estaIniciandoOutroJogador() && quantidadeDeJogadores < 2) {
						InetAddress ip = pacote.address();
						int porta = pacote.port();

						// Povoar jogadores mapeados
						int id = quantidadeDeJogadores;
						Jogador jogador = new Jogador(id, ip.getHostAddress(), porta);
						if(quantidadeDeJogadores == 0) {
							jogador.setSuaVez(true);
						}
						jogo.jogadoresMapeados.put(id, jogador);
						quantidadeDeJogadores++;

						transmissao.transmitirPacote(new Pacote(ip, porta,
								FabricaDeMensagem.criarMensagemDeEsperandoJogador(jogo.imprimirTabuleiro())));
					}

					if (quantidadeDeJogadores == 2) {
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
					pacote = recepcao.receberMensagem();
					if (jogo.quantidadeDejogadas == 0) {
						transmissao.transmitirPacote(
								new Pacote(InetAddress.getByName(jogo.jogadorQueVaiEsperar().getIp()), jogo.jogadorQueVaiEsperar().getPorta(),
										FabricaDeMensagem.criarMensagemJogadorEspera(jogo.imprimirTabuleiro())));
						
						
						transmissao.transmitirPacote(
								new Pacote(InetAddress.getByName(jogo.jogadorQuePodeJogar().getIp()), jogo.jogadorQuePodeJogar().getPorta(),
										FabricaDeMensagem.getMensagemJogadorFazJogada(jogo.imprimirTabuleiro())));
					}
					
					if (jogo.quantidadeDejogadas < 9 && !jogo.venceu) {
						jogo.quantidadeDejogadas += 1;
						jogo.status = StatusJogo.JOGO_ESPERANDO_JOGADA;
					} 

					break;
				case JOGO_ESPERANDO_JOGADA:
					pacote = recepcao.receberMensagem();

					if (pacote != null) {

						if (pacote.message().estaEnviandoJogada()
								&& pacote.message().getFields()[1] instanceof Jogada) {
							Jogada jogada = (Jogada) pacote.message().getFields()[1];

							Pacote pacoteJogada = jogo.executarJogada(jogada, pacote, jogo.jogadoresMapeados);
							transmissao.transmitirPacote(pacoteJogada);
							
							if(pacoteJogada.message().jogoEncerrouVenceu()) {
								transmissao.transmitirPacote(
										new Pacote(InetAddress.getByName(jogo.jogadorQuePodeJogar().getIp()), jogo.jogadorQuePodeJogar().getPorta(),
												FabricaDeMensagem.criarMensagemJogoEncerradoVenceu(jogo.imprimirTabuleiro(), jogo.informarQuemEOVencedor())));
								jogo.status = StatusJogo.JOGO_ENCERRADO;
								break;
								
							}else if(pacoteJogada.message().jogoEncerrouEmpatou()) {
								transmissao.transmitirPacote(
										new Pacote(InetAddress.getByName(jogo.jogadorQuePodeJogar().getIp()), jogo.jogadorQuePodeJogar().getPorta(),
												FabricaDeMensagem.criarMensagemJogoEncerradoEmpatou(jogo.imprimirTabuleiro())));
								jogo.status = StatusJogo.JOGO_ENCERRADO;
								break;
							} else {
								
								transmissao.transmitirPacote(
										new Pacote(InetAddress.getByName(jogo.jogadorQuePodeJogar().getIp()), jogo.jogadorQuePodeJogar().getPorta(),
												FabricaDeMensagem.getMensagemJogadorFazJogada(jogo.imprimirTabuleiro())));
							}
							
							
							jogo.status = StatusJogo.JOGO_INICIADO;
							break;
						}
					}

					break;
				
				case JOGO_ENCERRADO:
					transmissao.interrupt();
					recepcao.interrupt();
					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Thread.sleep(1000);
		}
	}
}
