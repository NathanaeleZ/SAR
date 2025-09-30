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

public class CChannel extends Channel {
	// To make a connection we give a channel to one task and another channel to the other task
	// Both channel now each other so they can communicate
	// A Channel has a circular buffer.
	// When we try to write but the buffer is full it throw an exception
	// The channel is bidirectionnal so both task can write and read.

  protected CChannel(Broker broker, int port) {
    super(broker);
    throw new RuntimeException("NYI");
  }

  // added for helping debugging applications.
  public String getRemoteName() {
    throw new RuntimeException("NYI");
  }

  // This method will write down the data from the circular buffer of this channel.
  // It will writes the given in the given bytes starting from the given offset.
  // If we try to read more than we asked for, no exception. The int return the number of byte written down
  // Before reading check if other channel disconnected
  @Override
  public int read(byte[] bytes, int offset, int length) {
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
    throw new RuntimeException("NYI");
  }

  // This method turn the variable disconnected to true.
  // 
  @Override
  public void disconnect() {
    throw new RuntimeException("NYI");
  }

  // this methode ask the other channel if it is disonnected and send yes if yes.
  // To do this the other channel has a get_disconnected() method 
  @Override
  public boolean disconnected() {
    throw new RuntimeException("NYI");
  }
  
  // This method change the value to true of the boolean disconnect after the other channel ask for a deconnection
  public void get_disconnect() {}
  
  // This method is call by the write method from the other channel
  // It will write inside the buffer 
  public int receive(byte[] bytes, int offset, int length) {
	  throw new RuntimeException("NYI");
  }

}
