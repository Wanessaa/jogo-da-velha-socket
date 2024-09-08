package jogoDaVelhaSocket.utils;

import java.net.InetAddress;


	public record Pacote(InetAddress address, int port, Mensagem message) {}

