package jogoDaVelhaSocket.utils;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private final int[] fields;
    public static final int MSG_SIZE = 2;

    public Mensagem(int[] fields) {
        this.fields = fields;
    }

    public int[] getFields() {
        return this.fields;
    }
    
    public boolean estaEsperandoOutroJogador() {
    	return this.fields[0] == TipoDeMensagem.esperandoJogador.ordinal();
    }
    
    public boolean estaIniciandoOutroJogador() {
    	return this.fields[0] == TipoDeMensagem.iniciarJogador.ordinal();
    }
    
    
    
}