package info5.sar.channels;

class Client extends Task {
	Broker broker;
	Channel channel;

	Client(Broker b, String name) {
		super(name, b);
		broker = b;
	}

	public Runnable get_runnable() {
		return new Runnable() {
			public void run() {
				System.out.println("Client connecting...");
				channel = broker.connect("b2", 1000);
				System.out.println("Client connected");
				channel.write("Coucou".getBytes(), 0, "Coucou".length());
			}
		};
	}

}