package jogoDaVelhaSocket.utils;

public class FabricaDeMensagem {
	public static Mensagem criarMensagemDeEsperandoJogador(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.ESPERANDO_JOGADORES.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	public static Mensagem criarMensagemIniciarJogador() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.iniciarJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogoEncerradoVenceu(String tabuleiro, String vencedor) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGO_ENCERRADO_VENCEU.ordinal();
		msg.getFields()[1] = vencedor;
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
	
	public static Mensagem criarMensagemVezdoJogador(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.vezDoJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemEnviarJogada(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.enviarJogada.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemEntradaInvalida(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.entradaInvalida.ordinal();

		return msg;
	}
	
	
	public static Mensagem criarMensagemJogadoresProntos(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadoresProntos.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorInicia(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorInicia.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorEspera(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorEspera.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorVenceu(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorVenceu.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogoEmpatou(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogoEmpatou.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemSuaVez(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.suaVez.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemAguardeSuaVez(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.aguardeSuaVez.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemTabuleiro(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.tabuleiro.ordinal();
		msg.getFields()[1] = tabuleiro;

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
	
	public static Mensagem getMensagemJogadorEspera(String tabuleiro) {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.JOGADOR_ESPERA.ordinal();
		msg.getFields()[2] = tabuleiro;

		return msg;
	}
	
}
