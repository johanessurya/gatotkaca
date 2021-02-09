package util;
import java.util.LinkedList;

/**
 * Class <code>ThreadPool</code> adalah kumpulan dari beberapa thread dengan 
 * maksimum thread yang ditentukan
 */
public class ThreadPool extends ThreadGroup {
	/**
	 * Membuat object baru
	 * @param numThreads banyak thread
	 */
    public ThreadPool(int numThreads) {
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);

        isAlive = true;

        taskQueue = new LinkedList<Runnable>();
        for (int i=0; i<numThreads; i++) {
            new PooledThread().start();
        }
    }

    /**
     * Meminta <code>ThreadPool</code> ini untuk menjalankan sebuah class. 
     * @param task <code>Runnable</code> class
     */
    public synchronized void runTask(Runnable task) {
        if (!isAlive) {
            throw new IllegalStateException();
        }
        if (task != null) {
            taskQueue.add(task);
            notify();
        }

    }

    /**
     * Get a task
     * @return <code>Runnable</code> interface
     * @throws InterruptedException ketika thread terganggu(interrupted)
     */
    protected synchronized Runnable getTask()
        throws InterruptedException
    {
        while (taskQueue.size() == 0) {
            if (!isAlive) {
                return null;
            }
            wait();
        }
        return (Runnable)taskQueue.removeFirst();
    }

    /**
     * Menutup <code>ThreadPool</code> ini. Semua <code>Thread</code> akan 
     * terhenti dan semua <code>Thread</code> yang sedang menunggu tidak akan 
     * dijalankan. Setelah <code>Thread</code> dihentikan tidak ada 
     * <code>Thread</code> yang dapat dikerjakan oleh <code>ThreadPool</code> ini
     */
    public synchronized void close() {
        if (isAlive) {
            isAlive = false;
            taskQueue.clear();
            interrupt();
        }
    }

    /**
     * Menutup <code>ThreadPool</code> ini dan menunggu semua <code>Thread</code>
     * dijalankan. Semua <code>Thread</code> yang menunggu akan dijalankan
     */
    public void join() {
        // notify all waiting threads that this ThreadPool is no
        // longer alive
        synchronized (this) {
            isAlive = false;
            notifyAll();
        }

        // wait for all threads to finish
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i=0; i<count; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ex) { }
        }
    }

    /**
     * Memberitahu bahwa <code>PooledThread</code> telah dimulai. Method ini tidak
     * melakukan pekerjaan apapun secara default. Subclass harus override untuk 
     * melakukan sesuatu.
     */
    protected void threadStarted() {
        // do nothing
    }

    /**
     * Memberitahu bahwa <code>PooledThread</code> telah dihentikan. Method ini 
     * tidak melakukan pekerjaan apapun secara default. Subclass harus override 
     * untuk melakukan sesuatu.
     */
    protected void threadStopped() {
        // do nothing
    }


    /**
        A PooledThread is a Thread in a ThreadPool group, designed
        to run tasks (Runnables).
    */
    private class PooledThread extends Thread {


        public PooledThread() {
            super(ThreadPool.this,
                "PooledThread-" + (threadID++));
        }


        public void run() {
            // signal that this thread has started
            threadStarted();

            while (!isInterrupted()) {

                // get a task to run
                Runnable task = null;
                try {
                    task = getTask();
                }
                catch (InterruptedException ex) { }

                // if getTask() returned null or was interrupted,
                // close this thread.
                if (task == null) {
                    break;
                }

                // run the task, and eat any exceptions it throws
                try {
                    task.run();
                }
                catch (Throwable t) {
                    uncaughtException(this, t);
                }
            }
            // signal that this thread has stopped
            threadStopped();
        }
    }

    private boolean isAlive;
    private LinkedList<Runnable> taskQueue;
    private int threadID;
    private static int threadPoolID;
}