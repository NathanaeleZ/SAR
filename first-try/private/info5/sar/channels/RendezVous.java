package info5.sar.channels;

import java.util.concurrent.Semaphore;

public class RendezVous {

	 Semaphore rdv = new Semaphore(0);
	    int nexpected, narrived;
	    int connection;

	    Semaphore mutex = new Semaphore(1); // exclusion mutuelle

	    RendezVous(int connection) {
	        this.nexpected = 2;
	        this.connection=connection;
	    }

	    CChannel come(Broker broker) {
	        try {
	            mutex.acquire();
	            narrived++;
	            if (narrived < nexpected) {
	            	CChannel channel1=new CChannel(broker);
	                mutex.release();
	                rdv.acquire();
	                return channel1;
	            } else {
	            	CChannel channel2=new CChannel(broker);
	                rdv.release(nexpected - 1);
	                mutex.release();
	                return channel2;
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
			return null;
	    }
	    
	    String accept_or_connect() {
	    	if(connection==1) {
	    		return "connect";
	    	}
	    	return "accept";
	    }
}
