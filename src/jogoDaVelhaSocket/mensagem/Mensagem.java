package jogoDaVelhaSocket.mensagem;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private final Object[] fields;
    public static final int MSG_SIZE = 3;
    //

    public Mensagem(Object[] fields) {
        this.fields = fields;
    }

    public Object[] getFields() {
    	return this.fields;
    }
    
    public String getTabuleiro() {
    	if (this.fields[2] == null)
    		return "";
    	
    	return (String) this.fields[2];
    }

    public boolean estaEsperandoOutroJogador() {
    	return (int) this.fields[0] == TipoDeMensagem.ESPERANDO_JOGADORES.ordinal();
    }

    public boolean estaIniciandoOutroJogador() {
    	return (int) this.fields[0] == TipoDeMensagem.CONECTAR_JOGADOR.ordinal();
    }
    
    public boolean estaEnviandoJogada() {
    	return (int) this.fields[0] == TipoDeMensagem.ENVIANDO_JOGADA.ordinal();
    }
    
    public boolean jogoEncerrouVenceu() {
    	return (int) this.fields[0] == TipoDeMensagem.JOGO_ENCERRADO_VENCEU.ordinal();
    }
    
    public boolean jogoEncerrouEmpatou() {
    	return (int) this.fields[0] == TipoDeMensagem.JOGO_ENCERRADO_EMPATOU.ordinal();
    }
}