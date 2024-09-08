package jogoDaVelhaSocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import jogoDaVelhaSocket.utils.Mensagem;
import jogoDaVelhaSocket.utils.Pacote;
import jogoDaVelhaSocket.utils.Transmissao;

//Cliente usa essa classe para se conectar com o servidor

public class Conexao {

	private final Recepcao entrada;
	private final Transmissao saida;
	private final InetAddress ip;
	private final int porta;
	
	
	public Conexao (DatagramSocket socket, InetAddress ip, int porta)throws IOException {
		this.ip = ip;
		this.porta = porta;
		this.saida = new Transmissao (socket);
		this.entrada = new Recepcao (socket);
		this.saida.start();
		this.entrada.start();
		
	}
	
	public synchronized Mensagem receberMensagem(){
		return this.entrada.receberMensagem();
	}
	
	
	public synchronized void enviarMensagem(Mensagem mensagem){
		this.saida.transmitirPacote(mensagem);
		System.out.println("metodo enviar mensagem da classe conexao");
	}
	
	 public void stop(){
	        this.entrada.interrupt();
	        this.saida.interrupt();
	 }
	
	private class Recepcao extends Thread {
		
	    private final DatagramSocket socket;
	    private final Queue<Mensagem> fila;

	    public Recepcao(DatagramSocket socket) {
	        this.socket = socket;
	        this.fila = new ArrayBlockingQueue<>(10);

	        //this.start();
	    }

	    @Override
	    public void run() {
	    	 while (true) {
	                if(Thread.currentThread().isInterrupted()) break;

	                try {
	                    byte[] buffer = new byte[1024];

	                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
	                    this.socket.receive(receivePacket);

	                    ByteArrayInputStream byteInStream = new ByteArrayInputStream(buffer);
	                    ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);
	                    Mensagem msg = (Mensagem) objectInStream.readObject();

	                    this.fila.offer(msg);
	                } catch (IOException | ClassNotFoundException e) {
	                    e.printStackTrace();
	                }
	            }
	    }
	    
	    
	    
	    public synchronized Mensagem receberMensagem() {
	        return this.fila.poll();
	    }
	}
	
	public class Transmissao extends Thread {
		private final DatagramSocket socket;
		private final Queue <Mensagem> queue;
		
		public Transmissao(DatagramSocket socket) {
			this.socket = socket;
			this.queue = new ArrayBlockingQueue<>(10);
			//this.start();
		}
		
		public void transmitirPacote(Mensagem msg) {
			System.out.println("metodo transmitir  da classe conexao");
			this.queue.offer(msg);
		}
		
		@Override
		public void run() {
			while (true) {
                if(Thread.currentThread().isInterrupted()) break;

                if (!queue.isEmpty()) {
                    try {
                        Mensagem msg = queue.remove();
                        System.out.println("mensagem do run de transmissao" + msg.getFields());
                        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
                        objectOutStream.writeObject(msg);
                        byte[] objectData = byteOutStream.toByteArray();

                        DatagramPacket packet = new DatagramPacket(objectData, objectData.length, ip, porta);
                        System.out.println("porta = "+ porta);
                        System.out.println("ip = "+ ip);

                        this.socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
		}
	}

	
}


