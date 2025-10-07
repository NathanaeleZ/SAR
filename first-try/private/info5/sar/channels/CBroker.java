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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CBroker extends Broker {
// Contains an Annuaire class which contains a Map of all Brokers
// Also contains 2 Map one connect Map et one accept Map
	Map<Integer, ArrayList<RendezVous>> connection_map;
	Annuaire annuaire;
	
// This create the 2 accept et connect Map
// For the accept map the keys are the ports number and the values are a RendezVous 
// For the connect map the keys are the ports number and the values are a list of RendezVous 
  public CBroker(String name) {
    super(name);
    this.connection_map = new HashMap<Integer, ArrayList<RendezVous>>();
  }

  private void get_annuaire(Annuaire a) {
	  annuaire=a;
  }
 // When a task accept a port first the broker looks inside his connect Map.
 // If there is already a task waiting inside a RendezVous then we free this one and both task receive a channel to communicate
  // If no task already waiting then we make this task wait inside a RendezVous and we add the rendezVous and the port number to the Accept Map
  @Override
  public Channel accept(int port) {
	  ArrayList<RendezVous> rdvList = connection_map.get(port);

	    if (rdvList != null) {
	        // Il existe déjà des rendez-vous (donc potentiellement des connect en attente)
	        ArrayList<RendezVous> toRemove = new ArrayList<>();

	        for (RendezVous rdv : rdvList) {
	            // On regarde si c’est un rendez-vous côté connect ou accept
	            if (rdv.accept_or_connect().equals("accept")) {
	                // Déjà un accept → on le jette
	                toRemove.add(rdv);
	            } else if (rdv.accept_or_connect().equals("connect")) {
	                // Il y a un connect qui attend → on connecte les deux
	                Channel channel = rdv.come(this);
	                toRemove.add(rdv);
	                rdvList.removeAll(toRemove);
	                return channel;
	            }
	        }

	        // Si on arrive ici, aucun connect n’était en attente
	        RendezVous newRdv = new RendezVous(0);
	        rdvList.add(newRdv);
	        return newRdv.come(this);
	    } else {
	        // Aucun rendez-vous sur ce port → on crée la liste et le premier accept
	        ArrayList<RendezVous> newList = new ArrayList<>();
	        RendezVous newRdv = new RendezVous(0);
	        newList.add(newRdv);
	        connection_map.put(port, newList);
	        return newRdv.come(this);
	    }
	}

  
  // When a task connect trough her broker the broker looks inside his annuaire.
  // If the name of the asked channel doesn't exist then error
  // If it exists then their is 2 case.
  // First case their is already a task waiting (inside a RendezVous)with an accept with the right port number
  // Second case no task waiting so we make the one asking waiting inside a RendezVous that we add to the list of connect of the Broker
  @Override
  public Channel connect(String name, int port) {
	CBroker target = (CBroker) annuaire.get_broker(name);
	if(target == null) {
		throw new IllegalArgumentException("Broker " + name + " introuvable");
	}
    throw new RuntimeException("NYI");
  }

}
