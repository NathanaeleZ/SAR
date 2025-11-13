package info5.sar.channels;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class CBroker extends Broker {

	ConcurrentHashMap<Integer, ArrayList<RendezVous>> connection_map;
	Annuaire annuaire;
	Semaphore mutex = new Semaphore(1);

	public CBroker(String name) {
		super(name);
		this.connection_map = new ConcurrentHashMap<>();
		this.annuaire= Annuaire.getInstance();
		this.annuaire.add(this);
	}

	public void set_annuaire(Annuaire a) {
		this.annuaire = a;
	}

	@Override
	public Channel accept(int port) {
		try {
			return getRendezVous(port, 0); // 0 = accept
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Channel connect(String name, int port) {
		CBroker target = (CBroker) annuaire.get_broker(name);
		if (target == null)
			// If broker doesn't exist
			return null;
		try {
			return target.getRendezVous(port, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Channel getRendezVous(int port, int type) throws InterruptedException {
	    mutex.acquire();
	    RendezVous target = null;
	    try {
	    

	    ArrayList<RendezVous> rdvs = connection_map.get(port);
	    if (rdvs != null) {
	        synchronized (rdvs) {
	            for (RendezVous rdv : rdvs) {
	            	if (type == 0 && "accept".equals(rdv.accept_or_connect())) {
                        throw new IllegalArgumentException("Un accept existe déjà sur ce port");
                    }
	            	else if ((type == 0 && "connect".equals(rdv.accept_or_connect())) ||
	                    (type == 1 && "accept".equals(rdv.accept_or_connect()))) {
	                    rdvs.remove(rdv);
	                    target = rdv;
	                    break;
	                }
	                
	            }
	        }
	    }

	    if (target == null) {
	        ArrayList<RendezVous> newList = connection_map.get(port);
	        if (newList == null) {
	            newList = new ArrayList<>();
	            connection_map.put(port, newList);
	        }
	        target = new RendezVous(type);
	        newList.add(target);
	    }
	    }finally {
	    mutex.release(); 
	    }
	    return target.come(this); 
	}

}
