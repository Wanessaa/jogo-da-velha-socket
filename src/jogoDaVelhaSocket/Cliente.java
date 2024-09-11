package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import jogoDaVelhaSocket.entidade.Jogada;
import jogoDaVelhaSocket.mensagem.FabricaDeMensagem;
import jogoDaVelhaSocket.mensagem.Mensagem;
import jogoDaVelhaSocket.mensagem.TipoDeMensagem;
import jogoDaVelhaSocket.thread.Conexao;
import jogoDaVelhaSocket.utils.ConfiguracoesServidor;


public class Cliente {

	public static void main(String args[]) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		Conexao conexao = new Conexao(socket, InetAddress.getByName(ConfiguracoesServidor.ENDERECO_SERVIDOR), ConfiguracoesServidor.PORTA_SERVIDOR);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Scanner scanner = new Scanner(System.in);
		Mensagem mensagem = null;

		System.out.println("Deseja jogar o jogo da velha s/n?");
		String sentence = reader.readLine();

		if (sentence.equalsIgnoreCase("s")) {
			conexao.enviarMensagem(FabricaDeMensagem.criarMensagemIniciarJogador());
		}else {
			System.out.println("Você optou por não jogar no momento.");
			System.exit(0);
		}

		while(true) {
			mensagem = conexao.receberMensagem();

			if (mensagem == null) {
				Thread.sleep(200);  // A pausa evita sobrecarregar o processador

			}else {
				Object obj = mensagem.getFields()[0];
				
				if (obj instanceof Integer) {
					int valor = (Integer) obj;

					TipoDeMensagem tipoDeMensagem = TipoDeMensagem.values()[valor];

					switch (tipoDeMensagem) {

					// Esta esperando os 2 jogadores iniciarem o jogo
					case ESPERANDO_JOGADORES:
						System.out.println("esperando os jogadores se conectarem...");
						break;

						// Os 2 jogadores estao conectados
					case JOGO_INICIADO:
						System.out.println("JOGO INICIADO");
						
						break;

					case JOGADOR_FAZ_JOGADA:
						System.out.println(mensagem.getTabuleiro());
						System.out.println("Vamos lá, sua vez!");
						System.out.println("Por favor, diga em qual linha deseja jogar. ");
						int linha = scanner.nextInt();
						System.out.println("Por favor, diga em qual coluna deseja jogar. ");
						int coluna = scanner.nextInt();
						System.out.println(linha + " " + coluna);
						Jogada jogada = new Jogada(linha, coluna);
						Object[] conteudoMensagem = {TipoDeMensagem.ENVIANDO_JOGADA.ordinal(), jogada};
						Mensagem jogadaMensagem = new Mensagem(conteudoMensagem);
						conexao.enviarMensagem(jogadaMensagem);
						break;

					case JOGADOR_ESPERA:
						System.out.println(mensagem.getTabuleiro());
						System.out.println("Aguarde a sua vez!");

						break;

					case JOGO_ENCERRADO_VENCEU:
						String msg = (String) mensagem.getFields()[1];
						System.out.println("====== Fim de jogo! ====== O jogador " + msg + " venceu!");		
						conexao.stop();
						
						break;
					
					case JOGO_ENCERRADO_EMPATOU:
						System.out.println(mensagem.getTabuleiro());
						System.out.println("====== O jogo deu empate! =======");
						conexao.stop();
						break;
					case JOGADA_INVALIDA:
						System.out.println(mensagem.getTabuleiro());
						System.out.println("====== JOGADA INVALIDA! =======");
						break;
					default:
						System.out.println("ACABOU");
						break;
					}
				} else {
					System.out.println("Tipo inválido para conversão para enum.");
				}


			}

		}



	}

}