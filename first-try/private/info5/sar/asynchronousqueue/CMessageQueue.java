package info5.sar.asynchronousqueue;

import info5.sar.channels.Channel;
import info5.sar.channels.Task;

public class CMessageQueue extends MessageQueue {

	private Channel channel;
	private Listener listener;
	private Task receiveTask;
	private Task sendTask;

	public CMessageQueue(Channel channel) {
		this.channel = channel;
		receiveTask=new Task("receiveTask",null);
		receiveTask.start(receiveTask());
	}

	@Override
	void setListener(Listener l) {
		this.listener = l;
	}

	@Override
	synchronized boolean send(byte[] bytes) {
		return send(bytes, 0, bytes.length);
	}

	@Override
	synchronized boolean send(byte[] bytes, int offset, int length) {
		if (this.closed())
			return false;
		sendTask = new Task("sendTask", null);
		sendTask.start(sendTask(bytes, offset, length));
		return true;
	}

	@Override
	void close() {
		System.out.println("Close no more reception");
		channel.disconnect();
		receiveTask.interrupt();
		while(listener==null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		listener.closed();

	}

	@Override
	boolean closed() {
		return channel.disconnected();
	}

	Runnable receiveTask() {
		return new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted() && !closed()) {
					byte[] message;
					try{
					int numberOfBytesRead = 0;
					byte[] lengthBuffer = new byte[4];
					while (numberOfBytesRead < 4) {
						int bytesRead = channel.read(lengthBuffer, numberOfBytesRead, 4 - numberOfBytesRead);
						numberOfBytesRead += bytesRead;
					}

					int messageLength = ((lengthBuffer[0] & 0xFF) << 24) | ((lengthBuffer[1] & 0xFF) << 16)
							| ((lengthBuffer[2] & 0xFF) << 8) | (lengthBuffer[3] & 0xFF);
					message = new byte[messageLength];
					numberOfBytesRead = 0;
					while (numberOfBytesRead < messageLength) {
						int bytesRead = channel.read(message, numberOfBytesRead, messageLength - numberOfBytesRead);
						numberOfBytesRead += bytesRead;
					}} catch(IllegalStateException e) {
						// Catch close
						return;
					}
					while(listener==null) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
					if(closed()) {
						return;
					}
					listener.received(message);
				}
			}
		};
	}

	Runnable sendTask(byte[] bytes, int offset, int length) {
		return new Runnable() {
			@Override
			public void run() {
				int written = 0;
				while (written < length) {
					try{
					written += channel.write(bytes, offset + written, length - written);
					} catch(IllegalStateException e) {
						// Catch close(
						return;
					}
				}
			};
		};

	}
}
