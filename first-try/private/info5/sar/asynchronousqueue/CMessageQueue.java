package info5.sar.asynchronousqueue;

import info5.sar.asynchronousqueue.MessageQueue.Listener;
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
		this.setListener(new Listener() {
			@Override
			public void received(byte[] msg) {
				System.out.println("Message bien reçu:"+new String(msg));
			}
			@Override
			public void closed() {}
		});
	}

	@Override
	void setListener(Listener l) {
		this.listener = l;
	}

	@Override
	synchronized boolean send(byte[] bytes) {
		if (this.closed())
			return false;
		sendTask = new Task("sendTask", null);
		sendTask.start(sendTask(bytes, 0, bytes.length));
		return true;
	}

	@Override
	synchronized boolean send(byte[] bytes, int offset, int length) {
		if (this.closed())
			return false;
		if (sendTask != null)
			return false;
		sendTask = new Task("sendTask", null);
		sendTask.start(sendTask(bytes, offset, length));
		return true;
	}

	@Override
	void close() {
		channel.disconnect();

	}

	@Override
	boolean closed() {
		return channel.disconnected();
	}

	Runnable receiveTask() {
		return new Runnable() {
			@Override
			public void run() {
				while (!closed()) {
					int numberOfBytesRead = 0;
					byte[] lengthBuffer = new byte[4];
					while (numberOfBytesRead < 4) {
						int bytesRead = channel.read(lengthBuffer, numberOfBytesRead, 4 - numberOfBytesRead);
						numberOfBytesRead += bytesRead;
					}

					int messageLength = ((lengthBuffer[0] & 0xFF) << 24) | ((lengthBuffer[1] & 0xFF) << 16)
							| ((lengthBuffer[2] & 0xFF) << 8) | (lengthBuffer[3] & 0xFF);
					byte[] message = new byte[messageLength];
					numberOfBytesRead = 0;
					while (numberOfBytesRead < messageLength) {
						int bytesRead = channel.read(message, numberOfBytesRead, messageLength - numberOfBytesRead);
						numberOfBytesRead += bytesRead;
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
					written += channel.write(bytes, offset + written, length - written);
				}
			};
		};

	}
}
