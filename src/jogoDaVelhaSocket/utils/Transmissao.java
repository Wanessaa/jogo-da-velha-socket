package jogoDaVelhaSocket.utils;

import java.net.DatagramSocket;
import java.util.Queue;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;

public class Transmissao extends Thread {
	private final DatagramSocket socket;
	private final Queue <Pacote> queue;
	
	public Transmissao(DatagramSocket socket) {
		this.socket = socket;
		this.queue = new ArrayBlockingQueue<>(10);
		this.start();
	}
	
	public void transmitirPacote(Pacote pacote) {
		this.queue.offer(pacote);
	}
	
	@Override
	public void run() {
		while(true) {
			if(!queue.isEmpty()) {
				try {
					Pacote pacote = queue.remove();
					
					InetAddress destinationIp = pacote.address();
                    int destinationPort = pacote.port();

                    ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
                    objectOutStream.writeObject(pacote.message());
                    byte[] objectData = byteOutStream.toByteArray();

                    DatagramPacket packet = new DatagramPacket(objectData, objectData.length, destinationIp, destinationPort);

                    this.socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
