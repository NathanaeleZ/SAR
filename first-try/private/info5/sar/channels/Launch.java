package info5.sar.channels;

public class Launch {

	public static void main(String[] args) {
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Client client = new Client(b1, "c1");
		Server server = new Server(b2, "s2");
		System.out.println("Lancement");
		client.start(client.get_runnable());
		server.start(server.get_runnable());

	}

}
