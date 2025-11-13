package info5.sar.asynchronousqueue;

public class CEvent implements Event {
	
	Runnable runnable;
	long delay;
	
	public CEvent(Runnable runnable,int delay) {
		this.runnable= runnable;
		this.delay=System.currentTimeMillis() + delay;
	}

	public CEvent(Runnable runnable) {
		this.runnable= runnable;
		this.delay=0;
	}

	@Override
	public void react() {
		runnable.run();
	}

	public long getDelay() {
		return delay;
	}
	
	

}
