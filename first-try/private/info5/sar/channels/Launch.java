package info5.sar.channels;

public class Launch {

	public static void main(String[] args) {
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");
		
		Annuaire annuaire=new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		b1.set_annuaire(annuaire);
		b2.set_annuaire(annuaire);
		Client client =new Client(b1);
		Server server=new Server(b1);
		
		Task t1=new Task("b1",b1);
		Task t2=new Task("b2",b2);
		
		t1.start(client);
		t2.start(server);
		
	}

}
