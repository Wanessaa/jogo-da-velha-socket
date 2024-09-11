package jogoDaVelhaSocket.entidade;


public class Jogador {
		
	private int id;
	private String ip;
	private int porta;
	private boolean suaVez;
	
	public Jogador(int id, String ip, int porta) {
		this.id = id;
		this.ip = ip;
		this.porta = porta;
		this.suaVez = false;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPorta() {
		return porta;
	}
	
	public void setPorta(int porta) {
		this.porta = porta;
	}

	public boolean isSuaVez() {
		return suaVez;
	}

	public void setSuaVez(boolean suaVez) {
		this.suaVez = suaVez;
	} 
	
	
}
