# First step – Static group

- How do processes constitute a group?

There is a helper process.
He is waiting for N process to connect to his broker on port 9999 for example.
So when we create a process we give him the helper broker name and the port number
When he received the connection from N process. He send to every process the List of the process with port number and name.
Then they all connect to each other.
Rules : 

When the helper send the list every process get a unique number from 1 to N so every process have his own number in the group
We don't forget that a process make also a connection with himself
The list contains for every process : 
- the process number
- the broker of the process
- the port number

If some processes have the same broker it can have conflict on the port number so the process try a new port until he finds a free one

When they all receive the process.

The first process make a connect with every process then the second one make connect with everyone but not the first ...
The last one don't have to make any connect because everyone already connected to him but connect with himself






# Second step – TOM – Static Group – No failures

Why does the protocol work? When a process P is about to deliver a message at the top of
the queue of received messages, with all the necessary acknowledgments, why is it correct to
do so? In other words, how can the process P be sure that it will not received any message in
the future that would have a smaller timestamp that the one it is about to deliver?

# Third step – TOM – Process failures – No resurrection
Assume a perfect fault detector
Analyze the effect of process failures
Design a solution to resist process failures while maintaining the multicast atomicity

3 Cases : 
- A process fail before sending his message
nothing to do because he didn't send anything

- A process receive ack but the sender fail before delivering the message
The process have to wait for the failure detector to inform him that the sender fail
Then he can remove from his waiting for ack list the sender 



When a process fail the failure detector send to every process the list of the alive process
When a process receive the new list of alive process he remove from his waiting for ack list the process that are not in the alive list
When a process want to deliver a message he check if the waiting for ack list is empty if yes he deliver the message else he wait for the other ack or for a new alive list



# Fourth step – TOM – Process failures & resurrections
Design a solution to resurrect failed processes