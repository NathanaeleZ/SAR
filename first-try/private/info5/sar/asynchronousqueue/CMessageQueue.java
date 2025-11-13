package info5.sar.asynchronousqueue;

import info5.sar.channels.Channel;

public class CMessageQueue extends MessageQueue{

	private Channel channel;
	private Listener listener;

	public CMessageQueue(Channel channel) {
		this.channel = channel;
	}

	@Override
	void setListener(Listener l) {
		this.listener = l;
	}

	@Override
	boolean send(byte[] bytes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean send(byte[] bytes, int offset, int length) {
		channel.write(bytes, offset, length);
		return false;
	}

	@Override
	void close() {
		channel.disconnect();
		
	}

	@Override
	boolean closed() {
		return channel.disconnected();
	}

}
