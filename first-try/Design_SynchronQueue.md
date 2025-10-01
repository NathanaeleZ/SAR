# QueueBroker 

The QueueBroker is on top of his Broker
When accept he calls his broker to do the accept with the same argument
When connect he calls his broker to do the connect with the same argument

# MessageQueue

## send

the send use his channel to write the data.
While the message is not fully written we rewrite again until all the data is written

## receive

At first we receive the length of the message with 4 octet because an int is 4 octets
So first we read until we receive all 4 bytes.
After that we know the length we receive again and we read with the channel until will receive all the full length message
Once the buffer is given the ownership is also givenback to the messageQueue
MessageQueue has a 4 bytes buffer to store the length
MessageQueue allocate a new buffer for every new message

## Close

Ask his channel to disconnect

## Closed 

Ask his channel disconnected


