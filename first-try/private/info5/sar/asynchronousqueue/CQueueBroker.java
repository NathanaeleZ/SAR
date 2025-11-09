package info5.sar.asynchronousqueue;

public class CQueueBroker extends QueueBroker{

	CQueueBroker(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean bind(int port, AcceptListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean connect(String name, int port, ConnectListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

}
