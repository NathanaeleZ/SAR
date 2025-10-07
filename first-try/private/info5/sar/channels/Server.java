package info5.sar.channels;


class Server implements Runnable {
	Broker broker;
	Channel channel;

	Server(Broker b) {
		broker = b;
	}

	@Override
	public void run() {
		channel =broker.accept(1000);
		byte[] tab=new byte[8];
		channel.read(tab, 0, 0);
		System.out.println("[Server] Message reçu : " + new String(tab));

	}

	Broker get_broker() {
		return broker;
	}
	
}