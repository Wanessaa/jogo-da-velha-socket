package jogoDaVelhaSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import jogoDaVelha.mensagem.EnvioDePacote;


public class Cliente {

	public static void main(String args[]) throws Exception {
		
		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress ipAddress = InetAddress.getByName("localhost");
		int port = 80;

		System.out.println("Deseja jogar o Jogo da Velha s/n?");
		String sentence = keyboardReader.readLine();

		EnvioDePacote.enviarMensagem(clientSocket, sentence, ipAddress, port);
		
		byte[] receivedData = new byte[1024];
		//DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

		while (true) {
			//receber resposta do servidor
			String serverMessage = EnvioDePacote.receberMensagem(clientSocket);

			System.out.println("FROM SERVER:");
			System.out.println(serverMessage);
			
			//verificar se o servidor informou que o jogo terminou
			if (serverMessage.contains("Fim de jogo") || serverMessage.contains("Vencedor")) {
                System.out.println("Jogo encerrado.");
                clientSocket.close();
               break;
            }
			
			
			if(serverMessage.contains("Sua vez") || serverMessage.contains("primeiro") || serverMessage.contains("inválida")) {
				System.out.println("Em qual campo deseja jogar? ");
				String jogada = keyboardReader.readLine();

                EnvioDePacote.enviarMensagem(clientSocket, jogada, ipAddress, port);
			}
		}
		 clientSocket.close();
	}

}