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
            	System.out.println("itemtabuleiro "+valor );
                return item;
            }
        }
        return VAZIO;  
    }
	
	
	public String getSimbolo() {
        switch (this) {
            case X: return "X";
            case O: return "O";
            default: return " "; // Espaço para VAZIO
        }
    }
	
}
