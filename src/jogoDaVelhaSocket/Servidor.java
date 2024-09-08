package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
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
		 System.out.println("servidor iniciado");
	     Recepcao recepcao = new Recepcao(socket);
	     Transmissao transmissao = new Transmissao(socket);
	     JogoDaVelha jogo = new JogoDaVelha();
	     Pacote pacote = null;
	     
	     while(true) {
	    	 try{
	    		 switch(jogo.status) {
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
	                    }else {
	                    	System.out.println("Recebeu o pacote: " + pacote.message().getFields()[0]);
	                    }
	    		 			
	    		 		
	    		 		if(pacote.message().estaIniciandoOutroJogador() && quantidadeDeJogadores < 2) {
	    		 			System.out.println("está inicando jogador");
	    		 			ip = pacote.address();
	    					porta = pacote.port();
	    		 			
	    		 			quantidadeDeJogadores++;
	    		 			//povoarJogadoresMapeados(socket);
	    		 			if (quantidadeDeJogadores == 1) {
	    		 				transmissao.transmitirPacote(new Pacote(ip,porta,FabricaDeMensagem.criarMensagemDeEsperandoJogador()));
	    					} else {
//	    						response = "O jogo está prestes a começar!!!\n";
	    					}

	    		 		}	
	    		 	break;
	    		 }
	    		 		
	    		 
	    	 }
	    	catch(Exception e) {
	    			 e.printStackTrace();
	    			 
	    		 } 
	    	 Thread.sleep(1000);
	    	 } 
	    	 
	     }
		
	     
		
		
		
		
//		JogoDaVelha jogo = new JogoDaVelha();
//
//		DatagramSocket serverSocket = new DatagramSocket(80);
//		System.out.println("Servidor iniciado!");
//
//		byte[] receivedData = new byte[1024];
//
//		while (true) {
//
//			DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
//
//			String sentence = "";
//			String response = "";
//			InetAddress ipAddress;
//			int port;
//
//			byte[] sendData;
//			
//			while (quantidadeDeJogadores < 2) {
//				
//				sentence = EnvioDePacote.receberMensagem(serverSocket, receivePacket);
//				
//				if (sentence.equalsIgnoreCase("s") && quantidadeDeJogadores < 2) {
//					povoarJogadoresMapeados(receivePacket);
//					quantidadeDeJogadores++;
//
//					if (quantidadeDeJogadores == 1) {
//						response = "Esperando o outro jogador...\n";
//					} else {
//						response = "O jogo está prestes a começar!!!\n";
//					}
//
//					ipAddress = receivePacket.getAddress();
//					port = receivePacket.getPort();
//					
//					EnvioDePacote.enviarMensagem(serverSocket, response, ipAddress, port);
//
//				}
//
//				if (quantidadeDeJogadores == 2) {
//					
//					for (Jogador jogador : jogadoresMapeados.values()) {
//                        ipAddress = InetAddress.getByName(jogador.getIp());
//                        port = jogador.getPorta();
//                        EnvioDePacote.enviarMensagem(serverSocket, response, ipAddress, port);
//                    }
//				}
//				receivePacket = new DatagramPacket(receivedData, receivedData.length);
//				receivedData = new byte[1024];
//			}
//
//			if (quantidadeDeJogadores == 2) {
//				
//				for(Jogador jogador : jogadoresMapeados.values()) {
//					if(jogador.getId() == 0) {
//						response = "Você será o primeiro jogador!";
//						String tabuleiroVazio = JogoDaVelha.imprimirTabuleiro(jogo);
//						EnvioDePacote.enviarMensagem(serverSocket, tabuleiroVazio, InetAddress.getByName(jogador.getIp()), jogador.getPorta());
//						jogador.setSuaVez(true);
//					} else {
//						response = "Aguarde, seu oponente começará a partida";
//					}
//					
//					sendData = response.getBytes();
//					EnvioDePacote.enviarMensagem(serverSocket, response, InetAddress.getByName(jogador.getIp()), jogador.getPorta());
//					
//				}
//
//				serverSocket.receive(receivePacket);
//				
//				JogoDaVelha.iniciar(serverSocket, receivePacket, jogadoresMapeados, jogo);
//			}
//		}
	

	 private static void povoarJogadoresMapeados(DatagramPacket receivePacket) {
	        int id = quantidadeDeJogadores;
	        String ip = receivePacket.getAddress().getHostAddress();
	        int porta = receivePacket.getPort();
	        Jogador jogador = new Jogador(id, ip, porta);
	        jogadoresMapeados.put(id, jogador);
	    }
}
