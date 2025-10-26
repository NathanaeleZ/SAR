package info5.sar.channels;

class Server extends Task {
	Broker broker;
	Channel channel;

	Server(Broker b, String name) {
		super(name, b);
		broker = b;
	}

	public Runnable get_runnable() {
		return new Runnable() {
			public void run() {
				System.out.println("Server accepting...");
				channel = broker.accept(1000);
				byte[] tab = new byte[6];
				try {
					sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int n =channel.read(tab, 0, tab.length);
				System.out.println("[Server] Message reçu : " + new String(tab,0,n)); // Affiche nombre de bytes lu seulement
			}
		};
	}

}