package jogoDaVelhaSocket.utils;

public class FabricaDeMensagem {
	public static Mensagem criarMensagemDeEsperandoJogador() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.esperandoJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemIniciarJogador() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.iniciarJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogoEncerrado() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogoEncerrado.ordinal();

		return msg;
	}
	
	
	public static Mensagem criarMensagemVezdoJogador() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.vezDoJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemEnviarJogada() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.enviarJogada.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemEntradaInvalida() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.entradaInvalida.ordinal();

		return msg;
	}
	
	
	public static Mensagem criarMensagemJogadoresProntos() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadoresProntos.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorInicia() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorInicia.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorEspera() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorEspera.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogadorVenceu() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogadorVenceu.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemJogoEmpatou() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.jogoEmpatou.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemSuaVez() {
		Mensagem msg = new Mensagem(new Object[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.suaVez.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemAguardeSuaVez() {
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
	

	
}
