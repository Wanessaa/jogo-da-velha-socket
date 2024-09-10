package jogoDaVelhaSocket.utils;

public enum StatusJogo {
	
	esperandoJogador(1),
	esperandoJogada(2),
	jogadorVencedor(3),
	jogoEmpatado(4),
	jogoEncerrado(5),
	jogadaInvalida(6),
	jogoIniciado(7),
	enviandoTabuleiro(8);
	
	

	StatusJogo(int valor) {
		// TODO Auto-generated constructor stub
	}

}
