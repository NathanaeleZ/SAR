package info5.sar.channels;

import java.util.concurrent.Semaphore;

public class RendezVous {

	 Semaphore rdv = new Semaphore(0);
	    int nexpected, narrived;
	    int connection;
	    CChannel firstChannel;

	    Semaphore mutex = new Semaphore(1); // exclusion mutuelle

	    RendezVous(int connection) {
	        this.nexpected = 2;
	        this.connection=connection;
	    }

	    synchronized CChannel  come(Broker broker) {
	        try {
	            mutex.acquire();
	            narrived++;
	            if (narrived < nexpected) {
	            	System.out.println("Arrivé du premier");
	            	firstChannel = new CChannel(broker);
	                mutex.release();
	                rdv.acquire();
	                return firstChannel;
	            } else {
	            	System.out.println("Arrivé du second");
	            	CChannel secondChannel = new CChannel(broker);
	                secondChannel.add_neighbor(firstChannel);
	                firstChannel.add_neighbor(secondChannel);
	                rdv.release(nexpected - 1);
	                mutex.release();
	                return secondChannel;
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
