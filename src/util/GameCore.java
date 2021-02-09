package util;

import java.awt.Graphics2D;
import graphics.ScreenManager;

/**
 * Class <code>GameCore</code> berguna untuk pembuatan screen gameplay seperti
 * screen main menu, komik, stage, loading, dll.
 * @author Yohanes Surya
 */
public abstract class GameCore {
	/**
	 * Object screen
	 */
    protected ScreenManager screen;

    /**
     * Untuk mengstop permainan
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Untuk memulai permainan
     */
    public void start(){
        isRunning=true;
    }
        
    /**
     * Untuk mengcheck apakah game sedang berjalan
     * @return <code>true</code> jika sedang berjalan dan <code>false</code>
     * jika sebaliknya
     */
    public boolean isRunning(){
    	return isRunning;
    }

    /**
     * Mengset running
     * @param isRunning <code>isRunning()</code> akan bernilai <code>true</code> 
     * dan <code>false</code> jika sebaliknya
     */
    public void setRunning(boolean isRunning){
    	this.isRunning=isRunning;
    }

    /**
     * Memanggil <code>init()</code> dan <code>gameLoop()</code> disertai dengan
     * pengembalian screen menjadi normal
     */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }

    /**
     * Menghentikan VM dari daemon thread.
     */
    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Method <code>init()</code> harus di implementasikan di subclass 
     */
    public abstract void init();

    /**
     * Method ini akan di ulang hingga method <code>stop()</code> di panggil
     */
    public void gameLoop() {
        long startTime = System.nanoTime();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime =
                System.nanoTime() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime/1000000);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
        }
    }

    /**
     * Method abstract yang harus di implement oleh subclass yang menghandel
     * semua update misal: animasi jalan, melompat, pause, pop up, dll
     * @param elapsedTime
     */
    public abstract void update(long elapsedTime);


    /***
     * Menggambar semua komponen mulai dari karakter, tanah, background, 
     * status bar, dll
     * @param g graphics
     */
    public abstract void draw(final Graphics2D g);
    
    private boolean isRunning;
}