package info5.sar.channels;

import java.util.concurrent.Semaphore;

public class RendezVous {

	private Semaphore rdv = new Semaphore(0);
	private int nexpected, narrived;
	private int connection;
	private CChannel firstChannel;

	private Semaphore mutex = new Semaphore(1); // exclusion mutuelle

	RendezVous(int connection) {
		this.nexpected = 2;
		this.connection = connection;
	}

	CChannel come(Broker broker) throws InterruptedException{
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
	}

	String accept_or_connect() {
		if (connection == 1) {
			return "connect";
		}
		return "accept";
	}
}
