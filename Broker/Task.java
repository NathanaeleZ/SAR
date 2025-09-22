abstract class Task extends Thread { 
abstract Task(Broker b, Runnable r);
abstract static Broker getBroker(); 
}