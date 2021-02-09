package input;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Menghandle semua inputan dari keyboard
 * @author Yohanes
 *
 */
public class InputManager implements KeyListener
{
	/**
	 * Mengset cursor menjadi tidak terlihat
	 */
    public static final Cursor INVISIBLE_CURSOR =
        Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0,0),
            "invisible");

    // key codes are defined in java.awt.KeyEvent.
    // most of the codes (except for some rare ones like
    // "alt graph") are less than 600.
    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions =
        new GameAction[NUM_KEY_CODES];

    private Component comp;

    /**
     * Membuat <code>InputManager</code> yang baru dengan target 
     * <code>Component</code>
     * @param comp target <code>Component</code>
     */
    public InputManager(Component comp) {
        this.comp = comp;

        // register key and mouse listeners
        comp.addKeyListener(this);

        // allow input of the TAB key and other keys normally
        // used for focus traversal
        comp.setFocusTraversalKeysEnabled(false);
    }


    /**
     * Set <code>Cursor</code>
     * @param cursor cursor
     */
    public void setCursor(Cursor cursor) {
        comp.setCursor(cursor);
    }

    /**
    Maps a GameAction to a specific key. The key codes are
    defined in java.awt.KeyEvent. If the key already has
    a GameAction mapped to it, the new GameAction overwrites
    it.
     */

    /**
     * Map <code>GameAction</code> ke spesifik key code. Key code didapat dari 
     * java.awt.KeyEvent
     * @param gameAction Object <code>GameAction</code>
     * @param keyCode value KeyCode
     */
    public void mapToKey(GameAction gameAction, int keyCode) {
        keyActions[keyCode] = gameAction;
    }

    /**
     * Memhilangkan <code>GameAction</code> yang sudah di set di object ini
     * @param gameAction object <code>GameAction</code> yang akan di hilangkan
     */
    public void clearMap(GameAction gameAction) {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameAction) {
                keyActions[i] = null;
            }
        }

        gameAction.reset();
    }

    /**
     * Mengambil semua nama dengan memasukkan <code>GameAction</code>
     * @param gameCode <code>GameAction</code>
     * @return List string
     */
    public List<String> getMaps(GameAction gameCode) {
        ArrayList<String> list = new ArrayList<String>();

        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameCode) {
                list.add(getKeyName(i));
            }
        }

        return list;
    }

    /**
     * Mereset semua <code>GameAction</code>
     */
    public void resetAllGameActions() {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] != null) {
                keyActions[i].reset();
            }
        }
    }

    /**
     * Mendapatkan nama dari keyCode(java.awt.KeyEvent)
     * @param keyCode code key
     * @return nama key
     */
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }

    private GameAction getKeyAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            return keyActions[keyCode];
        }
        else {
            return null;
        }
    }

    /**
     * dari KeyListener interface
     */
    public void keyPressed(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
        // make sure the key isn't processed for anything else
        e.consume();
    }


    /**
     * dari KeyListener interface
     */
    public void keyReleased(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
        // make sure the key isn't processed for anything else
        e.consume();
    }


    /**
     * dari KeyListener interface
     */
    public void keyTyped(KeyEvent e) {
        // make sure the key isn't processed for anything else
        e.consume();
    }
}
