package info5.sar.asynchronousqueue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import info5.sar.channels.Annuaire;

public class CEventPump extends EventPump {

	private Queue<Event> events;
	private List<Event> delayedEvents;
	private static CEventPump instance;

	public CEventPump() {
		events = new LinkedList<Event>();
		delayedEvents = new LinkedList<>();
	}

	public static synchronized CEventPump getInstance() {
		if (instance == null) {
			instance = new CEventPump();
		}
		return instance;
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		while (true) {
			// traiter les events normaux
			synchronized (events) {
				while (!events.isEmpty()) {
					Event e = events.poll();
					e.react();
				}
			}
			// traiter les events retardés
			long currentTime = System.currentTimeMillis();
			synchronized (delayedEvents) {
				Iterator<Event> iterator = delayedEvents.iterator();
				while (iterator.hasNext()) {
					CEvent e = (CEvent) iterator.next();
					if (e.getDelay() <= currentTime) {
						e.react();
						iterator.remove();
					}
				}
			}
		}
	}

	@Override
	public void post(Runnable e) {
		synchronized (events) {
			Event event = new CEvent(e);
			events.add(event);
		}
	}

	@Override
	public void post(Runnable e, int delay) {
		synchronized (delayedEvents) {
			Event event = new CEvent(e, delay);
			delayedEvents.add(event);
		}
	}

}
