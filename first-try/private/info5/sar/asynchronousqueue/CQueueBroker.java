package info5.sar.asynchronousqueue;

import info5.sar.channels.Broker;
import info5.sar.channels.Channel;
import info5.sar.channels.Task;

public class CQueueBroker extends QueueBroker{

	Broker broker;

	public CQueueBroker(Broker broker) {
		this.broker = broker;
	}

	@Override
	boolean bind(int port, AcceptListener listener) {
		new Task(null, broker) {
			@Override
			public void run() {
				while(!this.unbind(0)){
					Channel channel = broker.accept(port);
					MessageQueue queue = new CMessageQueue(channel);
					listener.accepted(queue);
				}
			}
		}.start();
		return false;
	}

	@Override
	boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean connect(String name, int port, ConnectListener listener) {
		Channel channel = broker.connect(name, port);
		MessageQueue queue = new CMessageQueue(channel);
		listener.connected(queue);
		return false;
	}

}
