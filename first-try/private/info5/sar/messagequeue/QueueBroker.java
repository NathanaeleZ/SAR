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

	public MessageQueue accept(int port) {
		Channel channel = broker.accept(port);
		return new MessageQueue(channel);
	}

	public MessageQueue connect(String brokerName, int port) {
		Channel channel = broker.connect(brokerName, port);
		return new MessageQueue(channel);
	}


}
