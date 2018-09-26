package java.lang;

public class Thread implements Runnable {

    private Runnable target;

    public Thread() {
    }

    public Thread(Runnable target) {
	this.target = target;
    }

    public Thread(ThreadGroup group, Runnable target) {
	this.target = target;
    }

    public Thread(Runnable target, String name) {
	this.target = target;
    }

    public Thread(ThreadGroup group, Runnable target, String name) {
	this.target = target;
    }

    public Thread(ThreadGroup group, Runnable target, String name,
                  long stackSize) {
	this.target = target;
    }

    public void join() throws InterruptedException {
    }

    public void join(int millis) throws InterruptedException {
    }

    public void join(int millis, int nanos) throws InterruptedException {
    }

    public synchronized native void start();

    public void run() {
	if (target != null) {
	    target.run();
	}
    }

    public static Thread currentThread() {
	return(null);
    }

    public static void sleep(long milliseconds) {
    }

    public static void sleep(long milliseconds, int nanoseconds) {
    }
}
