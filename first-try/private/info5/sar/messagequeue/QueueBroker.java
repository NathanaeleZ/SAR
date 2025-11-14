package info5.sar.messagequeue;

import info5.sar.channels.CBroker;
import info5.sar.channels.Channel;

public class QueueBroker{
	CBroker broker;

	public QueueBroker(CBroker broker) {
		this.broker = broker;
	}

	public String getName() {
		return broker.getName();
	}

	public MessageQueue accept(int port) throws InterruptedException {
		Channel channel=null;
		try {
			channel = broker.accept(port);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(channel==null)
			return null;
		return new MessageQueue(channel);
	}

	public MessageQueue connect(String brokerName, int port) {
		Channel channel = broker.connect(brokerName, port);
		return new MessageQueue(channel);
	}


}
