package info5.sar.asynchronousqueue;

abstract class EventPump extends Thread {
public abstract void post(Runnable e);
public abstract void post(Runnable e, int delay);
protected abstract void shutdown();
}
