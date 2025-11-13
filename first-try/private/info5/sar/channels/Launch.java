package info5.sar.channels;

import java.util.List;

public class Launch {

	public static void main(String[] args) {

		System.out.println("Lancement des tests ...");
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
	}

	// Les tasks se connectent et acceptent
	// L'une écrit coucou et l'autre le reçoit
	// Les tasks ont leur propre broker
	static private void test1() {
		System.out.println("==========TEST 1==========");
		CBroker b1 = new CBroker("b1");
		 Task task1 = new Task("Task1", b1);
		CBroker b2 = new CBroker("b2");
		Task task2 = new Task("Task2", b2);

		task1.start(new Runnable() {
            @Override
            public void run() {
				Channel channel = b1.connect("b2", 1000);
				channel.write("coucou".getBytes(), 0, "coucou".length());
			}
		});
		task2.start(new Runnable() {
            @Override
            public void run() {
				Channel channel = b2.accept(1000);
				byte[] buffer = new byte[1024];
				int bytesRead = channel.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					String message = new String(buffer, 0, bytesRead);
					System.out.println("Server received: " + message);
				}
			}
		});

		// attendre la fin des deux threads avant de continuer
		try {
			task1.join();
			task2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Les tasks se connectent et acceptent
	// L'une écrit coucou et l'autre le reçoit
	// Les tasks ont le même broker
	static private void test2() {
		System.out.println("==========TEST 2==========");
		CBroker b1 = new CBroker("b1");
		 Task task1 = new Task("Task1", b1);
		Task task2 = new Task("Task2", b1);

		task1.start(new Runnable() {
            @Override
            public void run() {
				Channel channel = b1.connect("b1", 1000);
				channel.write("coucou".getBytes(), 0, "coucou".length());
			}
		});
		task2.start(new Runnable() {
            @Override
            public void run() {
				Channel channel = b1.accept(1000);
				byte[] buffer = new byte[1024];
				int bytesRead = channel.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					String message = new String(buffer, 0, bytesRead);
					System.out.println("Server received: " + message);
				}
			}
		});

		// attendre la fin des deux threads avant de continuer
		try {
			task1.join();
			task2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Le serveur accepte sur plusieurs ports
	// Les clients se connectent sur un des ports du serveur
	// Chaque Task écrit coucou et le serveur les reçoit
	// Broker unique pour le serveur et les clients
	static private void test3() {
		System.out.println("==========TEST 3==========");
		CBroker b = new CBroker("b");
		Task server = new Task("server", b);
		Task client1 = new Task("client1", b);
		Task client2 = new Task("client2", b);
		Task client3 = new Task("client3", b);
		server.start(new Runnable() {
			@Override
			public void run() {
				List<Integer> ports = List.of(3000, 3001, 3002);
				for (int port : ports) {
					Channel channel = b.accept(port);
					byte[] buffer = new byte[1024];
					int bytesRead = channel.read(buffer, 0, buffer.length);
					if (bytesRead > 0) {
						String message = new String(buffer, 0, bytesRead);
						System.out.println("Server received on port " + port + ": " + message);
					}
				}
			}
		});
		client1.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b.connect("b", 3000);
				channel.write("Hello from client1".getBytes(), 0, "Hello from client1".length());
			}
		});
		client2.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b.connect("b", 3001);
				channel.write("Hello from client2".getBytes(), 0, "Hello from client2".length());
			}
		});
		client3.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b.connect("b", 3002);
				channel.write("Hello from client3".getBytes(), 0, "Hello from client3".length());
			}
		});
		try {
			client1.join();
			client2.join();
			client3.join();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Le serveur accepte sur plusieurs ports
	// Les clients se connectent sur un des ports du serveur
	// Chaque client écrit coucou et le serveur les reçoit
	// Broker différent pour le serveur et les clients
	static private void test4() {
		System.out.println("==========TEST 4==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");
		Task server = new Task("server", b2);
		Task client1 = new Task("client1", b1);
		Task client2 = new Task("client2", b1);
		Task client3 = new Task("client3", b1);
		server.start(new Runnable() {
			@Override
			public void run() {
				List<Integer> ports = List.of(4000, 4001, 4002);
				for (int port : ports) {
					Channel channel = b2.accept(port);
					byte[] buffer = new byte[1024];
					int bytesRead = channel.read(buffer, 0, buffer.length);
					if (bytesRead > 0) {
						String message = new String(buffer, 0, bytesRead);
						System.out.println("Server received on port " + port + ": " + message);
					}
				}
			}
		});
		client1.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b1.connect("b2", 4000);
				channel.write("Hello from client1".getBytes(), 0, "Hello from client1".length());
			}
		});
		client2.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b1.connect("b2", 4001);
				channel.write("Hello from client2".getBytes(), 0, "Hello from client2".length());
			}
		});
		client3.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b1.connect("b2", 4002);
				channel.write("Hello from client3".getBytes(), 0, "Hello from client3".length());
			}
		});
		try {
			client1.join();
			client2.join();
			client3.join();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Test Buffer plein  
	static private void test5() {
		System.out.println("==========TEST 5==========");
		CBroker b1 = new CBroker("b1");
		CBroker b2 = new CBroker("b2");

		Task task1 = new Task("Task1", b1);
		Task task2 = new Task("Task2", b2);

		task1.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b1.connect("b2", 5000);
				String largeMessage = "String trop grand pour le buffer:  (Buffer de taille 32)";
				int bytesWritten=channel.write(largeMessage.getBytes(), 0, largeMessage.length());
			
				if (bytesWritten != 31) {
					System.err.println("Devrait avoir écrit 31 bytes avant de bloquer, mais a écrit: " + bytesWritten);
				}
				String testMessage = "testbonus";
				// Doit bloquer ici si le buffer est plein
				bytesWritten = channel.write(testMessage.getBytes(), 0, testMessage.length());
				if(bytesWritten!=4) {
					System.err.println("Erreur d'écriture du message test");
				}

			}
		});
		task2.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b2.accept(5000);
				byte[] buffer = new byte[64];


				try {
					// Laisser le temps d'écrire un long message
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				int bytesRead = channel.read(buffer, 0, 4);
				
			}
		});
		try {
			task1.join();
			task2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	// Test erreur si connect to broker inconnu
	static private void test6() {
		System.out.println("==========TEST 6==========");
		CBroker b1 = new CBroker("b1");
		Task task1 = new Task("Task1", b1);

		task1.start(new Runnable() {
			@Override
			public void run() {
				Channel channel = b1.connect("unknown_broker", 6000);
				if (channel == null) {
					System.out.println("Connection failed as expected to unknown broker.");
				} else {
					System.err.println("Connection should have failed but didn't.");
				}
			}
		});

		try {
			task1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}