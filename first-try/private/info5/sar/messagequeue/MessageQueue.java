package info5.sar.messagequeue;

import info5.sar.channels.Channel;

public class MessageQueue {

    private Channel channel;

    public MessageQueue(Channel channel) {
        this.channel = channel;
    }

    void send(byte[] bytes, int offset, int length) {
        int writtenSoFar = 0;
        while (writtenSoFar < length) {
            int remaining = length - writtenSoFar;
            int bytesWritten = channel.write(bytes, offset + writtenSoFar, remaining);
            writtenSoFar += bytesWritten;
        }
    }


//     At first we receive the length of the message with 4 octet because an int is 4 octets
// So first we read until we receive all 4 bytes.
// After that we know the length we receive again and we read with the channel until will receive all the full length message
// Once the buffer is given the ownership is also givenback to the messageQueue
// MessageQueue has a 4 bytes buffer to store the length
// MessageQueue allocate a new buffer for every new message
    byte[] receive() {
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
        return message;
    }

    void close() {
        channel.disconnect();
    }

    void closed() {
        channel.disconnected();
    }

}
