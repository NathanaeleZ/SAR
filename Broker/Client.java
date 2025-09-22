public class Client implements Runnable{
    

    public void run() {
        Broker broker = Task.getBroker();
        Channel channel = broker.connect("url",12);
        byte[] data=new byte[12];
        channel.read(data, 0, 12);
        String text=data.toString();
        System.out.println(text);
        channel.disconnect();
    }}
