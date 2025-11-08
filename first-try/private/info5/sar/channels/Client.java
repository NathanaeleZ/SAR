package info5.sar.channels;

class Client extends Task {
	Broker broker;
	Channel channel;
	int num_port;
	String broker_name;
	String message;

	Client(Broker b, String name,int num_port,String broker_name,String message) {
		super(name, b);
		broker = b;
		this.num_port=num_port;
		this.broker_name = broker_name;
		this.message = message;
	}

	public Runnable get_runnable() {
		return new Runnable() {
			public void run() {
				System.out.println("Client connecting...");
				channel = broker.connect(broker_name, num_port);
				System.out.println("Client connected");
				channel.write(message.getBytes(), 0, message.length());
			}
		};
	}

}