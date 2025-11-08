package info5.sar.messagequeue;

import info5.sar.channels.Task;

class Client extends Task {
	QueueBroker broker;
	MessageQueue channel;
	int num_port;
	String broker_name;
	String message;

	Client(QueueBroker b, String name, int num_port, String broker_name, String message) {
		super(name, b.broker);
		this.broker = b;
		this.num_port = num_port;
		this.broker_name = broker_name;
		this.message = message;
	}

	public Runnable get_runnable() {
		return new Runnable() {
			public void run() {
				System.out.println("Client connecting...");
				channel = broker.connect(broker_name, num_port);
				System.out.println("Client connected");
				byte[] length_bytes = new byte[4];
				length_bytes[0] = (byte) (message.length() >> 24);
				length_bytes[1] = (byte) (message.length() >> 16);
				length_bytes[2] = (byte) (message.length() >> 8);
				length_bytes[3] = (byte) (message.length());
				channel.send(length_bytes, 0, 4);
				channel.send(message.getBytes(), 0, message.length());
			};
		};

	}
}