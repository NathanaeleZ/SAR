### TD 15 Octobre SAR
#### Design ring ovelay with mutual exclusion

Data consistency ?

Everytime a peer receive the token he send it forward if he doesn't want to enter critical section.
If he does, he keep the token, edit the ressource.
After an edit he sends a notification of edit first and then the token

When a peer receive a notification of edit, he updates his ressource and he sends the notification of edit forwards.
The notification always arrive before the token

If a peer receive his own update we stop forwarding it


If failure?

All the peers know the ring overlay. 
If a peer a dies all peers will know in finite time that this peer his now dead.
So if a peer die the ring change so that the peer before know send to the peer after the dead one
A peer can ask for a token after a timeout. The question goes all around the ring.
If the question comes back then the token is dead so a coordinator create a new one.
If we a receive a question and we don't have the token before send it back we check our canal if empty we send the question if not we wait until the canal is empty. then if a token arrive in via the canal we send the token and not the question



#### Design fault tolerant distributed mutual exclusion

##### Client failure

If a client die in critical section the coordinator free the mutex and give it to the one in queue or just wait for a new one asking.
If a client is in the queue and he dies, we remove it from the queue.
If a client die and was asking for nothing we have nothing to do

##### Server failure

###### Server stateful

The server save the list of his clients(id and port number) at every edit of the list of his clients (connection , deconnection , death)
When a client ask for a connection the server first save the new client and after send the accept ack.
So if the server dies before sending the ack when he respawn he will still communicate with the new client.
When the server respawn(live again) he sends a request to all his client to know where they are. (queue(requesting mutex), mutex or doing nothing)
Some are in the queue so he add them in a new queue randomly.
If a client has the mutex he answer the server that he has it.
The others clients just answer that they are doing nothing
If a client doesn't answer: 
- the client is dead the server will know it thanks to the fault detector
- the client take his time to answer the server wait

Connect and accept.
When the clients detect that the server is dead they try to connect back to the serve until the server respwan
When the server respawn he accept all the connection (with his list previously saved)


###### Server stateless

