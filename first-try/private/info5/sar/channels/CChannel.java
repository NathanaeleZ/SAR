/*
 * Copyright (C) 2023 Pr. Olivier Gruber                                    
 *                                                                       
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, either version 3 of the License, or     
 * (at your option) any later version.                                   
 *                                                                       
 * This program is distributed in the hope that it will be useful,       
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         
 * GNU General Public License for more details.                          
 *                                                                       
 * You should have received a copy of the GNU General Public License     
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package info5.sar.channels;
import info5.sar.utils.*;

public class CChannel extends Channel {
	// To make a connection we give a channel to one task and another channel to the other task
	// Both channel now each other so they can communicate
	// A Channel has a circular buffer.
	// When we try to write but the buffer is full it throw an exception
	// The channel is bidirectionnal so both task can write and read.
	private boolean disconnected;
	private CChannel neighbor_channel;
	private CircularBuffer circular_buffer;

  protected CChannel(Broker broker, int port) {
    super(broker);
    disconnected=false;
    circular_buffer=new CircularBuffer(8);
  }

  // added for helping debugging applications.
  public String getRemoteName() {
    throw new RuntimeException("NYI");
  }
  
  private void add_neighbor(CChannel c) {
	  neighbor_channel=c;
  }

  // This method will write down the data from the circular buffer of this channel.
  // It will writes the given in the given bytes starting from the given offset.
  // If we try to read more than we asked for, no exception. The int return the number of byte written down
  // Before reading check if other channel disconnected
  @Override
  public int read(byte[] bytes, int offset, int length) {
	for(int i=0;i<length;i++) {
		bytes[offset+i]=circular_buffer.pull();
	}
    throw new RuntimeException("NYI");
  }
  // The write method write inside the the other channel buffer.
  // How it works.
  // First both channel know each other the other has a method receive.
  // When we write we sent the bytes to the receive method which belongs to the other channel.
  // This method send an exception if already full
  // If not full it writes down the data in the circular buffer
  // Before writing check if other channel disconnected
  @Override
  public int write(byte[] bytes, int offset, int length) {
	  return this.neighbor_channel.receive(bytes, offset, length);
  }

  // This method turn the variable disconnected to true.
  // 
  @Override
  public void disconnect() {
	  this.disconnected=true;
  }

  // this methode ask the other channel if it is disonnected and send yes if yes.
  // To do this the other channel has a get_disconnected() method 
  @Override
  public boolean disconnected() {
    return this.neighbor_channel.get_disconnected();
  }
  
  // This method change the value to true of the boolean disconnect after the other channel ask for a deconnection
  public boolean get_disconnected() {
	  return this.disconnected;
  }
  
  // This method is call by the write method from the other channel
  // It will write inside the buffer 
  public int receive(byte[] bytes, int offset, int length) {
	  int cpt=0;
	  try {
		  for(int i=0;i<length;i++) {
			  this.circular_buffer.push(bytes[offset+i]);
			  cpt++;
		  }
		  return cpt;
	  }catch (IllegalStateException e) {
		  System.out.println("Full");
		  return cpt;
	  }
  }

}
