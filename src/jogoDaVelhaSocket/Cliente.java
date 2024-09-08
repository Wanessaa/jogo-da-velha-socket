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


public class Cliente {

	public static void main(String args[]) throws Exception {
		 DatagramSocket socket = new DatagramSocket();
	     Conexao conexao = new Conexao(socket, InetAddress.getByName(ConfiguracoesServidor.ENDERECO_SERVIDOR), ConfiguracoesServidor.PORTA_SERVIDOR);
	     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	     System.out.println("Deseja jogar o jogo da velha s/n?");
		 String sentence = reader.readLine();
		 Mensagem mensagem = null;
			
			if (sentence.equalsIgnoreCase("s")) {
				conexao.enviarMensagem(FabricaDeMensagem.criarMensagemIniciarJogador());
			}else {
				
			}

		while(true) {
			
			// Verifica se a fila não está vazia
            mensagem = conexao.receberMensagem();

            // Certifique-se de que o pacote não é null
            if (mensagem == null) {
                // Pause o loop por um breve momento para não consumir CPU desnecessariamente
                Thread.sleep(500);  // A pausa evita sobrecarregar o processador
               
            }else {
            	switch(mensagem.getFields()[0]) {
            		case 0:
            			System.out.println("Esperando o outro jogador...\n");
            			break;
            	}
            }
			
		}
		
		
		
		
		
		
		
		
		
		
//		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
//		Scanner scanner = new Scanner(System.in);
//		DatagramSocket clientSocket = new DatagramSocket();
//
//		InetAddress ipAddress = InetAddress.getByName("localhost");
//		int port = 80;
//
//		System.out.println("Deseja jogar o jogo da velha s/n?");
//		String sentence = keyboardReader.readLine();
//
//		EnvioDePacote.enviarMensagem(clientSocket, sentence, ipAddress, port);
//		
//		byte[] receivedData = new byte[1024];
//
//		while (true) {
//			//receber resposta do servidor
//			String serverMessage = EnvioDePacote.receberMensagem(clientSocket);
//
//			System.out.println("Servidor:");
//			System.out.println(serverMessage);
//			
//			//verificar se o servidor informou que o jogo terminou
//			if (serverMessage.contains("Fim de jogo") || serverMessage.contains("Vencedor")) {
//                System.out.println("Jogo encerrado.");
//                clientSocket.close();
//               break;
//            }
//			
//			
//			if(serverMessage.contains("Sua vez") || serverMessage.contains("primeiro")  || serverMessage.contains("inválida")) {
//				System.out.println("Por favor, diga em qual linha deseja jogar. ");
//				int linha = scanner.nextInt();
//				System.out.println("Por favor, diga em qual coluna deseja jogar. ");
//				int coluna = scanner.nextInt();
//				
//				Jogada jogada = new Jogada(linha, coluna);
//                EnvioDePacote.enviarMensagem(clientSocket, jogada, ipAddress, port);
//			}
//		}
//		 clientSocket.close();
	}

}