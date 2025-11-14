package info5.sar.asynchronousqueue;

import info5.sar.asynchronousqueue.MessageQueue.Listener;
import info5.sar.asynchronousqueue.QueueBroker.AcceptListener;
import info5.sar.asynchronousqueue.QueueBroker.ConnectListener;
import info5.sar.channels.CBroker;

public class Launch {

    public static void main(String[] args) {
       test1();
    }

    public static void test1() {

        EventPump pump = CEventPump.getInstance();
        CBroker b1 = new CBroker("b1");
        QueueBroker qb1 = new CQueueBroker(b1);
        // Un seul qbroker envoie 
        Runnable bootstrap1 = () -> qb1.bind(8080, new AcceptListener() {
            @Override
            public void accepted(MessageQueue queue) {
                queue.setListener(new Listener() {
                    @Override
                    public void received(byte[] msg) {
                        System.out.println("Message bien reçu : " + new String(msg));
                    }

                    @Override
                    public void closed() {
                        queue.closed();
                    }
                });
                System.out.println("Connection accepted");
                //queue.close(); Fonctionne mais le message n'a pas le temps de s'écrire
            }
        });

        // --- Client ---
        Runnable bootstrap2 = () -> qb1.connect("b1", 8080, new ConnectListener() {

            @Override
            public void connected(MessageQueue queue) {
                System.out.println("Connection accepted");
                String msg = "Test1";

                int len = msg.length();
                byte[] lenBytes = new byte[] {
                        (byte) (len >> 24), (byte) (len >> 16),
                        (byte) (len >> 8), (byte) len };

                queue.send(lenBytes);
                queue.send(msg.getBytes());
            }

            @Override
            public void refused() { }
        });

        // Lancer les deux
        pump.post(bootstrap1);
        pump.post(bootstrap2);
        pump.start();

        // Unbind après un petit délai
        Runnable unbind = () -> qb1.unbind(8080);
        
        // Laisser le temps d'écrire le message avant de unbind
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        pump.post(unbind);
        // Laisser le temps d'exécuter le test du dessus
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CBroker b2=new CBroker("b2");
        QueueBroker qb2=new CQueueBroker(b2);
        // Test 2 qbroker différentes et close()
        Runnable test2 = () -> {
            qb1.bind(9000, new AcceptListener() {
                public void accepted(MessageQueue queue) {
                System.out.println("Connection accepted from qb1 to qb2");
                queue.setListener(new Listener() {
                    @Override
                    public void received(byte[] msg) {
                        System.out.println("Message bien reçu : " + new String(msg));
                    }

                    @Override
                    public void closed() {
                        queue.closed();
                    }
                });
            }
        });    };
        qb2.connect("b1", 9000, new ConnectListener() {
            @Override
            public void connected(MessageQueue queue) {
                System.out.println("Connection accepted from qb2 to qb1");
                String msg = "Test1";

                int len = msg.length();
                byte[] lenBytes = new byte[] {
                        (byte) (len >> 24), (byte) (len >> 16),
                        (byte) (len >> 8), (byte) len };

                queue.send(lenBytes);
                queue.close();
                //So no send
                queue.send(msg.getBytes());
                
                
            }

            @Override
            public void refused() {
                System.out.println("Connection refused from qb2 to qb1");
            }
        });
        pump.post(test2);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pump.shutdown();
    }
}
