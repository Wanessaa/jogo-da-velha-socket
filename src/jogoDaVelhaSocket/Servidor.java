package jogoDaVelhaSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import jogoDaVelhaSocket.mensagem.EnvioDePacote;
import jogoDaVelhaSocket.utils.FabricaDeMensagem;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.Recepcao;
import jogoDaVelhaSocket.utils.StatusJogo;
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
                            System.out.println("Está iniciando jogador");
                            ip = pacote.address();
                            porta = pacote.port();

                            quantidadeDeJogadores++;

                            povoarJogadoresMapeados(ip.getHostAddress(), porta);

                            if (quantidadeDeJogadores == 1) {
                                transmissao.transmitirPacote(new Pacote(ip, porta, FabricaDeMensagem.criarMensagemDeEsperandoJogador()));
                            } else {
                                transmissao.transmitirPacote(new Pacote(ip, porta, FabricaDeMensagem.criarMensagemJogadoresProntos()));
                                jogo.status = StatusJogo.jogoIniciado;
                            }
                        }
                        break;

                    case jogoIniciado:
                        for (Jogador jogador : jogadoresMapeados.values()) {
                            if (jogador.getId() == 0) {
                                transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemJogadorInicia()));
                                String tabuleiroVazio = JogoDaVelha.imprimirTabuleiro(jogo);
                                jogador.setSuaVez(true);
                            } else {
                                transmissao.transmitirPacote(new Pacote(InetAddress.getByName(jogador.getIp()), jogador.getPorta(), FabricaDeMensagem.criarMensagemJogadorEspera()));
                            }
                        }
                        // Descomente e ajuste conforme necessário
                        // serverSocket.receive(receivePacket);
                        // JogoDaVelha.iniciar(serverSocket, receivePacket, jogadoresMapeados, jogo);
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
