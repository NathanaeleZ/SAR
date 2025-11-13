package info5.sar.asynchronousqueue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CEventPump extends EventPump{

	List<Event> events;
	List<Event> delayedEvents;
	
	public CEventPump(){
		events = new LinkedList<Event>();
		delayedEvents = new LinkedList<>();
	}

	public void start(){
		Thread t = new Thread(this);
		t.start();
		this.run();
	}

	public void run(){
		while(true){
			// traiter les events normaux
			while(!events.isEmpty()){
				Event e = events.remove(0);
				e.react();
			}
			// traiter les events retardés
			long currentTime = System.currentTimeMillis();
			Iterator<Event> iterator = delayedEvents.iterator();
			while(iterator.hasNext()){
				Event e = iterator.next();
				if(e.getDelay() <= currentTime){
					e.react();
					iterator.remove();
				}
			}
		}
	}
	@Override
	public void post(Runnable e) {
		Event event = new CEvent(e);
		events.add(event);
	}

	@Override
	public void post(Runnable e, int delay) {
		Event event = new CEvent(e, delay);
		delayedEvents.add(event);
	}

}
