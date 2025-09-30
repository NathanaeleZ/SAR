package info5.sar.channels;

public class Launch {

	public static void main(String[] args) {
		Broker b1 = new CBroker("b1");
		Broker b2 = new CBroker("b2");
		
		Annuaire annuaire=new Annuaire();
		annuaire.add(b1);
		annuaire.add(b2);
		Task t1 =new Task(b1);
		Task t2=new Task(b1);
		
	}

}
