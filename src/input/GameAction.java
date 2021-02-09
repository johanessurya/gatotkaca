package input;

/**
 * Class <code>GameAction</code> adalah abstrak class yang menginisial action
 * seperti melompat atau berjalan. Class ini bekerja dengan 
 * <code>InpputManager</code>
 * @author Yohanes Surya
 */
public class GameAction {
	/**
	 * Behavior normal. Method <code>isPressed()<code> mengembalikan <code>true</code> selama
	 * tombol yang bersangkutan ditekan terus.
	 */
    public static final int NORMAL = 0;

    /**
     * Method <code>isPressed()</code> mengembalikan <code>true</code> hanya 
     * pertama kali ditekan dan tidak lagi sampai tombol itu di lepaskan dan
     * ditekan kembali
     */
    public static final int DETECT_INITAL_PRESS_ONLY = 1;

    private static final int STATE_RELEASED = 0;
    private static final int STATE_PRESSED = 1;
    private static final int STATE_WAITING_FOR_RELEASE = 2;

    private String name;
    private int behavior;
    private int amount;
    private int state;

    /**
     * Membuat object baru dengan behavior NORMAL
     * @param name nama <code>GameAction</code>
     */
    public GameAction(String name) {
        this(name, NORMAL);
    }

    /**
     * Membuat object baru dengan behavior yang spesifik
     * @param name nama <code>GameAction</code>
     * @param behavior behavior yang akan di set
     */
    public GameAction(String name, int behavior) {
        this.name = name;
        this.behavior = behavior;
        reset();
    }

    /**
     * Mendapatkan nama dari <code>GameAction</code>
     * @return nama <code>GameAction</code>
     */
    public String getName() {
        return name;
    }

    /**
     * Reset <code>GameAction</code> membuat seolah tombol tidak pernah ditekan
     */
    public void reset() {
        state = STATE_RELEASED;
        amount = 0;
    }

    /**
     * Menekan(tap) <code>GameAction</code> ini. Sama halnya memanggil method 
     * <code>press()</code> lalu dilanjutkan dengan <code>release()</code>
     */
    public synchronized void tap() {
        press();
        release();
    }

    /**
     * Melaporan bahwa <code>GameAction</code> ditekan
     */
    public synchronized void press() {
        press(1);
    }

    /**
     * Memberi perintah bahwa <code>GameAction</code> ini ditekan sebanyak 
     * berapa kali
     * @param amount banyak penekanan
     */
    public synchronized void press(int amount) {
        if (state != STATE_WAITING_FOR_RELEASE) {
            this.amount+=amount;
            state = STATE_PRESSED;
        }

    }

    /**
     * Melaporkan bahwa tombol dilepas
     */
    public synchronized void release() {
        state = STATE_RELEASED;
    }

    /**
     * Check apakah tombol telah ditekan.
     * @return <code>true</code> jika tombol telah ditekan <code>false</code>
     * jika sebaliknya
     */
    public synchronized boolean isPressed() {
        return (getAmount() != 0);
    }

    /**
     * Berapa banyak penekanan tombol pada <code>GameAction</code> ini setelah 
     * pengecekan terakhir(<code>isPressed()</code>)
     * @return banyak penekanan
     */
    public synchronized int getAmount() {
        int retVal = amount;
        if (retVal != 0) {
            if (state == STATE_RELEASED) {
                amount = 0;
            }
            else if (behavior == DETECT_INITAL_PRESS_ONLY) {
                state = STATE_WAITING_FOR_RELEASE;
                amount = 0;
            }
        }
        return retVal;
    }
}
