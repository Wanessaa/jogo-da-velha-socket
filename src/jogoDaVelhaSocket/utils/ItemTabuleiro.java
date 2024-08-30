package jogoDaVelhaSocket.utils;

public enum ItemTabuleiro {
	
	
	X("1"), 
	O("0"), 
	VAZIO(" ");

	public final String valorString;

	ItemTabuleiro(String valorStr) {
		this.valorString = valorStr;
	}

	//Mapeia o ID do jogador para um Item do Tabuleiro: X, O ou VAZIO caso não corresponda
	public static ItemTabuleiro mapearValorParaSimbolo(int valor) {
		String valorEmString = String.valueOf(valor);
        for (ItemTabuleiro item : values()) {
            if (item.valorString.equals(valorEmString)) {
                return item;
            }
        }
      
        return VAZIO;  
    }
}
