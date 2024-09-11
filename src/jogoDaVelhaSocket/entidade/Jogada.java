package jogoDaVelhaSocket.entidade;

import java.io.Serializable;

public class Jogada implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	int linha;
	int coluna;
	
	public Jogada(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	
}
