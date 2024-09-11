package jogoDaVelhaSocket.utils;

import java.net.InetAddress;

import jogoDaVelhaSocket.mensagem.Mensagem;


	public record Pacote(InetAddress address, int port, Mensagem message) {}

