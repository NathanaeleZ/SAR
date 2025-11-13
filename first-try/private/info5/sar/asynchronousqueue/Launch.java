package info5.sar.asynchronousqueue;

import info5.sar.channels.Annuaire;
import info5.sar.channels.CBroker;

public class Launch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventPump pump= new CEventPump();
		CBroker b1 = new CBroker("b1");
		QueueBroker qb1 = new CQueueBroker(b1);
		Annuaire annuaire = new Annuaire();
		annuaire.add(b1);
		b1.set_annuaire(annuaire);
		pump.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		}, 0);

		pump.start();
	}	

}
