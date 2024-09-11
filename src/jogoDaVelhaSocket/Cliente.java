package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import jogoDaVelhaSocket.mensagem.EnvioDePacote;
import jogoDaVelhaSocket.utils.ConfiguracoesServidor;
import jogoDaVelhaSocket.utils.FabricaDeMensagem;
import jogoDaVelhaSocket.utils.Mensagem;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.TipoDeMensagem;


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
			// TODO
		}

		while(true) {
			mensagem = conexao.receberMensagem();

			if (mensagem == null) {
				// Pause o loop por um breve momento para não consumir CPU desnecessariamente
				Thread.sleep(500);  // A pausa evita sobrecarregar o processador

			}else {
				Object obj = mensagem.getFields()[0];
				System.out.println(mensagem.getFields()[0]);

				if (obj instanceof Integer) {
					int valor = (Integer) obj;

					TipoDeMensagem tipoDeMensagem = TipoDeMensagem.values()[valor];

					switch (tipoDeMensagem) {

					// Est� esperando os 2 jogadores iniciarem o jogo
					case ESPERANDO_JOGADORES:
						System.out.println("ESPERANDO_JOGADORES");
						break;

						// Os 2 jogadores est�o conectados
					case JOGO_INICIADO:
						System.out.println("JOGO_INICIADO");
						System.out.println(mensagem.getTabuleiro());
						break;

					case JOGADOR_FAZ_JOGADA:


						System.out.println("Vamos lá, sua vez! 1");
						System.out.println("Por favor, diga em qual linha deseja jogar. ");
						int linha = scanner.nextInt();
						System.out.println("Por favor, diga em qual coluna deseja jogar. ");
						int coluna = scanner.nextInt();
						System.out.println(linha + " " + coluna);
						Jogada jogada = new Jogada(linha, coluna);
						Object[] conteudoMensagem = {TipoDeMensagem.enviarJogada.ordinal(), jogada};
						Mensagem jogadaMensagem = new Mensagem(conteudoMensagem);
						conexao.enviarMensagem(jogadaMensagem);
						System.out.println(mensagem.getTabuleiro());

						break;

					case JOGADOR_ESPERA:
						System.out.println(mensagem.getTabuleiro());
						System.out.println("Aguarde, o outro jogador será o primeiro");

						break;

						// O jogo foi encerrado
					case JOGO_ENCERRADO:
						System.out.println("JOGO_ENCERRADO");
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