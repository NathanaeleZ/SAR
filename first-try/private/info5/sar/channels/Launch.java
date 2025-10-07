package info5.sar.channels;

public class Launch {

	public static void main(String[] args) {
		Broker b1 = new CBroker("b1");
		Broker b2 = new CBroker("b2");
		
		Annuaire annuaire=new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		Client t1 =new Client(b1);
		Server t2=new Server(b1);
		
		t1.run();
		t2.run();
		
	}

}
