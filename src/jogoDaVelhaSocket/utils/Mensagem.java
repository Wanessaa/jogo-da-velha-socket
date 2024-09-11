package jogoDaVelhaSocket.utils;

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
    	return (int) this.fields[0] == TipoDeMensagem.esperandoJogador.ordinal();
    }

    public boolean estaIniciandoOutroJogador() {
    	return (int) this.fields[0] == TipoDeMensagem.iniciarJogador.ordinal();
    }
    
    public boolean estaEnviandoJogada() {
    	return (int) this.fields[0] == TipoDeMensagem.enviarJogada.ordinal();
    }
}