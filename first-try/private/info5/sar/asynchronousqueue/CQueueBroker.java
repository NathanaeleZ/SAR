package info5.sar.asynchronousqueue;

import java.util.HashMap;
import java.util.Map;

import info5.sar.channels.Broker;
import info5.sar.channels.Channel;
import info5.sar.channels.Task;

public class CQueueBroker extends QueueBroker {

	private Broker broker;
	private Task bind;
	private Task connect;
	private CEventPump pump;
	private Map binds;

	public CQueueBroker(Broker broker) {
		this.broker = broker;
		pump = CEventPump.getInstance();
		binds = new HashMap<Integer,BindTask>();
	}

	@Override
	boolean bind(int port, AcceptListener listener) {
		BindTask bind = (BindTask) binds.get(port);
		if (bind != null) {
			// Déjà bind sur ce port
			return false;
		}
		bind = new BindTask(port, listener);
		binds.put(port, bind);
		bind.start();
		return true;
	}
			

	@Override
	boolean unbind(int port) {
		BindTask bind= (BindTask) binds.get(port);
		if(bind == null) {
			// Pas de bind sur ce port
			return false;
		}
		bind.interrupt();
		binds.remove(port);
		return true;
	}

	@Override
	boolean connect(String name, int port, ConnectListener listener) {
		connect = new Task("connect", broker);
		connect.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = broker.connect(name, port);
				if (channel == null) {
					pump.post(() -> listener.refused());
					return;
				}
				MessageQueue messageQueue = new CMessageQueue(channel);
				pump.post(() -> listener.connected(messageQueue));
				
			}
		});
		return true;
	}

	class BindTask extends Thread{
		private int port;
		private AcceptListener listener;

		public BindTask(int port, AcceptListener listener) {
			this.port = port;
			this.listener = listener;
		}


		@Override
		public void run() {
		    while (!Thread.currentThread().isInterrupted()) {
		        try {
		            Channel channel = broker.accept(port);
		            if (channel == null) continue;

		            MessageQueue messageQueue = new CMessageQueue(channel);
		            pump.post(() -> listener.accepted(messageQueue));

		        } catch (InterruptedException e) {
		        	System.out.println("INTERRUPTED");
		        	return;  

		        } catch (Exception e) {
		            e.printStackTrace();
		            continue; // ne pas tuer le bind !
		        }
		    }
		}

	}

}
