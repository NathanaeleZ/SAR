public class Serveur implements Runnable{
    

    public void run() {
        Broker broker = Task.getBroker();
        Channel[] channels = new Channel[3];
        for(int i=0;i<3;i++){
                    channels[i]=broker.accept(12);
        }
        byte[] data=new byte[12];
        data="Hello Wolrd!".getBytes();
        for(int i=0;i<3;i++){
            channels[i].write(data, 0, data.length);
        }
        
    }
    
}
