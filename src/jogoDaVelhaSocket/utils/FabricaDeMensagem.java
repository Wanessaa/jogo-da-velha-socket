package jogoDaVelhaSocket.utils;

public class FabricaDeMensagem {
	public static Mensagem criarMensagemDeEsperandoJogador() {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.esperandoJogador.ordinal();

		return msg;
	}
	
	public static Mensagem criarMensagemIniciarJogador() {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);

		msg.getFields()[0] = TipoDeMensagem.iniciarJogador.ordinal();

		return msg;
	}
}
