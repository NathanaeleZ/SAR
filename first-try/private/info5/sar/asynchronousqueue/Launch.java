package info5.sar.asynchronousqueue;

import info5.sar.asynchronousqueue.QueueBroker.AcceptListener;
import info5.sar.asynchronousqueue.QueueBroker.ConnectListener;
import info5.sar.channels.CBroker;

public class Launch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventPump pump = CEventPump.getInstance();
		CBroker b1 = new CBroker("b1");
		QueueBroker qb1 = new CQueueBroker(b1);
		Runnable bootstrap1 = new Runnable() {
			@Override
			public void run() {
				qb1.bind(8080, new AcceptListener() {
					@Override
					public void accepted(MessageQueue queue) {
						System.out.println("Connection accepted");

					}
				});
				
			}

		};
		Runnable bootstrap2 = new Runnable() {
			@Override
			public void run() {
				qb1.connect("b1",8080, new ConnectListener() {

					@Override
					public void connected(MessageQueue queue) {
						System.out.println("Connection accepted");
						String msg="Coucou";
						int msg_length=msg.length();
						byte[] msg_length_bytes=new byte[] {
						        (byte) (msg_length >> 24),
						        (byte) (msg_length >> 16),
						        (byte) (msg_length >> 8),
						        (byte) msg_length
						    };
						queue.send(msg_length_bytes);
						byte[] bytes=msg.getBytes();
						queue.send(bytes);
						System.out.println("Message et taille bien envoyé !");
					}
				

					@Override
					public void refused() {
						// TODO Auto-generated method stub
						
					}
				});
				//qb1.unbind(8080);
				
			}

		};
		pump.post(bootstrap1);
		pump.post(bootstrap2);
		pump.start();
	}
}
