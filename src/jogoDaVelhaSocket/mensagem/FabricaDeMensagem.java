package jogoDaVelhaSocket.mensagem;

public class FabricaDeMensagem {
	public static Mensagem criarMensagemDeEsperandoJogador(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.ESPERANDO_JOGADORES.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem criarMensagemIniciarJogador() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.CONECTAR_JOGADOR.ordinal();
		return msg;
	}
	
	public static Mensagem criarMensagemJogoEncerradoVenceu(String tabuleiro, String vencedor) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGO_ENCERRADO_VENCEU.ordinal();
		msg.getFields()[1] = vencedor;
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem criarMensagemJogoEncerradoEmpatou(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGO_ENCERRADO_EMPATOU.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem criarMensagemEnviarJogada(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.ENVIANDO_JOGADA.ordinal();

		return msg;
	}

	public static Mensagem getMensagemJogoIniciado(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGO_INICIADO.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem getMensagemJogadorFazJogada(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGADOR_FAZ_JOGADA.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorEspera(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGADOR_ESPERA.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
}
