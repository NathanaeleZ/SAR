package info5.sar.channels;

import java.util.HashMap;
import java.util.Map;

public class Annuaire {
// This is a singleton class which all brokers will have access to.
// The annuaire contains a Map which associate every name (values ) to their broker
// When a Broker is created it check first if the name of the new Broker is available
	
	Map<String, Broker> brokers;
	
	public Annuaire() {
		brokers=new HashMap<>();
	}
	
	public void add(Broker b) {
		brokers.put(b.getName(),b);
	}
	
	public Broker get_broker(String name) {
		return brokers.get(name);
	}

}
