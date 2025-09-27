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

public class CBroker extends Broker {
// Contains an Annuaire class which contains a Map of all Brokers
// Also contains 2 Map one connect Map et one accept Map

	
// This create the 2 accept et connect Map
// For the accept map the keys are the ports number and the values are a RendezVous 
// For the connect map the keys are the ports number and the values are a list of RendezVous 
  public CBroker(String name) {
    super(name);
    throw new RuntimeException("NYI");
  }

  @Override
  public Channel accept(int port) {
    throw new RuntimeException("NYI");
  }

  @Override
  public Channel connect(String name, int port) {
    throw new RuntimeException("NYI");
  }

}
