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
	
	
	
	
}
