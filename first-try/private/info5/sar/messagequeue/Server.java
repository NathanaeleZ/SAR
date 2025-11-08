package info5.sar.messagequeue;

import java.util.List;
import info5.sar.channels.Task;

class Server extends Task {
	QueueBroker broker;
	MessageQueue channel;
	List <Integer> ports = new java.util.ArrayList<>();
	int message_length;

	Server(QueueBroker b, String name,List <Integer> ports,int message_length) {
		super(name, b.broker);
		broker = b;
		this.ports = ports;
		this.message_length = message_length;
	}

	public Runnable get_runnable() {
		return new Runnable() {
			public void run() {
				System.out.println("Server accepting...");
				for (Integer port : ports) {
					System.out.println("Server accepting on port "+port);
					MessageQueue channel = broker.accept(port);
					byte[] tab = new byte[message_length];
					try {
						sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tab = channel.receive();
					System.out.println("[Server] Message reçu on port "+port+" : " + new String(tab)); // Affiche nombre de bytes lu seulement
				}
			}
		};
	}

}