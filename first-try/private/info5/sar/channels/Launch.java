package info5.sar.channels;

import java.util.List;

public class Launch {

	public static void main(String[] args) {

		System.out.println("Lancement des tests ...");
		test1();
		test2();
		test3();
		test4();
		test5();
	}

	// Le client se connect avec que le serveur accept
	// Le client écrit coucou et le serveur le reçoit
	// Le serveur et le client ont leur propre broker
	static private void test1() {
		System.out.println("==========TEST 1==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Client client = new Client(b1, "c1", 1000, "b2","Hello from c1");
		Server server = new Server(b2, "s1", List.of(1000), 20);
		client.start(client.get_runnable());
		server.start(server.get_runnable());
		// attendre la fin des deux threads avant de continuer
		try {
			client.join();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Le servveur accept avant que le client connect
	// Le client écrit coucou et le serveur le reçoit
	// Le serveur et le client ont leur propre broker
	static private void test2() {
		System.out.println("==========TEST 2==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Client client = new Client(b1, "c1", 2000, "b2","Hello from c1");
		Server server = new Server(b2, "s1", List.of(2000), 20);
		server.start(server.get_runnable());
		client.start(client.get_runnable());
		try {
			server.join();
			client.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Le serveur accepte sur plusieurs ports
	// Les clients se connectent sur un des ports du serveur
	// Chaque client écrit coucou et le serveur les reçoit
	// Broker unique pour le serveur et les clients
	static private void test3() {
		System.out.println("==========TEST 3==========");
		CBroker b = new CBroker("b");
		Annuaire annuaire = new Annuaire();
		annuaire.add(b);
		b.set_annuaire(annuaire);
		Server server = new Server(b, "s1", List.of(3000, 3001, 3002), 20);
		Client client1 = new Client(b, "c1", 3000, "b","Hello from c1");
		Client client2 = new Client(b, "c2", 3001, "b","Hello from c2");
		Client client3 = new Client(b, "c3", 3002, "b","Hello from c3");
		server.start(server.get_runnable());
		client1.start(client1.get_runnable());
		client2.start(client2.get_runnable());
		client3.start(client3.get_runnable());
		try {
			client1.join();
			client2.join();
			client3.join();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Le serveur accepte sur plusieurs ports
	// Les clients se connectent sur un des ports du serveur
	// Chaque client écrit coucou et le serveur les reçoit
	// Broker différent pour le serveur et les clients
	static private void test4() {
		System.out.println("==========TEST 4==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Server server = new Server(b2, "s1", List.of(4000, 4001, 4002), 20);
		Client client1 = new Client(b1, "c1", 4000, "b2","Hello from c1");
		Client client2 = new Client(b1, "c2", 4001, "b2","Hello from c2");
		Client client3 = new Client(b1, "c3", 4002, "b2","Hello from c3");
		server.start(server.get_runnable());
		client1.start(client1.get_runnable());
		client2.start(client2.get_runnable());
		client3.start(client3.get_runnable());
		try {
			client1.join();
			client2.join();
			client3.join();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Test Buffer plein
	static private void test5() {
		System.out.println("==========TEST 5==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Server server = new Server(b2, "s1", List.of(5000), 15);
		Client client1 = new Client(b1, "c1", 5000, "b2","Hello from c1");
		Client client2 = new Client(b1, "c2", 5000, "b2","Hello from c2");
		Client client3 = new Client(b1, "c3", 5000, "b2","Hello from c3");
		server.start(server.get_runnable());
		client1.start(client1.get_runnable());
		client2.start(client2.get_runnable());
		client3.start(client3.get_runnable());
	}
}
