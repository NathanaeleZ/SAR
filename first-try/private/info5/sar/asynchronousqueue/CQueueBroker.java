package info5.sar.asynchronousqueue;

import java.util.HashMap;
import java.util.Map;

import info5.sar.channels.Broker;
import info5.sar.channels.Channel;
import info5.sar.channels.Task;

public class CQueueBroker extends QueueBroker {

	Broker broker;
	Task bind;
	Task connect;
	CEventPump pump;
	Map binds;

	public CQueueBroker(Broker broker) {
		this.broker = broker;
		pump = CEventPump.getInstance();
		binds = new HashMap<Integer,Task>();
	}

	@Override
	boolean bind(int port, AcceptListener listener) {
		Task bind = (Task) binds.get(port);
		if (bind != null) {
			// Déjà bind sur ce port
			return false;
		}
		bind = new Task("bind", broker);
		binds.put(port, bind);
		bind.start(new Runnable() {
			@Override
			public void run() {
				while (true) {
					Channel channel = broker.accept(port);
					MessageQueue messageQueue = new CMessageQueue(channel);
					pump.post(() -> listener.accepted(messageQueue));

				}
			}
		});
		return true;
	}

	@Override
	boolean unbind(int port) {
		Task bind= (Task) binds.get(port);
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
				MessageQueue messageQueue = new CMessageQueue(channel);
				pump.post(() -> listener.connected(messageQueue));
				
			}
		});
		return true;
	}

}
