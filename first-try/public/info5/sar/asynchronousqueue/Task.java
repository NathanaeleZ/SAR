package info5.sar.asynchronousqueue;

import info5.sar.channels.Broker;

abstract class Task extends Thread {
	Runnable runnable;
	Broker broker;

	Task(Broker b, Runnable r) {
	}

	static Broker getBroker() {
		return broker;
	}

	static Task getTask() {
		return this;
	}
}
