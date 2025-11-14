package info5.sar.messagequeue;

import java.util.List;

import info5.sar.channels.Annuaire;
import info5.sar.channels.CBroker;

public class Launch {

	public static void main(String[] args) {
		test1();

	}

	static void test1() {
		CBroker b1 = new CBroker("b1");
		QueueBroker qb1 = new QueueBroker(b1);
		Annuaire annuaire = Annuaire.getInstance();
		annuaire.add(b1);
		Server server1 = new Server(qb1, "server1", List.of(1000), 1024);
		Client client1 = new Client(qb1, "client1", 1000, "b1", "Hello from client1 , message way too long to test the message queue");
		server1.start(server1.get_runnable());
		client1.start(client1.get_runnable());
	}

}
