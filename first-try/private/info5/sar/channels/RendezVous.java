package info5.sar.channels;

import java.util.concurrent.Semaphore;

public class RendezVous {

	 Semaphore rdv = new Semaphore(0);
	    int nexpected, narrived;

	    Semaphore mutex = new Semaphore(1); // exclusion mutuelle

	    RendezVous(int nexpected) {
	        this.nexpected = nexpected;
	    }

	    void come() {
	        try {
	            mutex.acquire();
	            narrived++;
	            if (narrived < nexpected) {
	                mutex.release();
	                rdv.acquire();
	            } else {
	                rdv.release(nexpected - 1);
	                mutex.release();
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
}
