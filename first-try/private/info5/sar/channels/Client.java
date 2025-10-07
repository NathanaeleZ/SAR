package info5.sar.channels;

class Client implements Runnable {
	Broker broker;
	Channel channel;

	Client(Broker b) {
		broker = b;
	}

	@Override
	public void run() {
		channel = broker.connect("b2", 1000);
		channel.write("Coucou".getBytes(), 0, 8);
	}

	Broker get_broker() {
		return broker;
	}
}