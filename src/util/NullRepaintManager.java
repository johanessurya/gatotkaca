package util;

import javax.swing.RepaintManager;
import javax.swing.JComponent;

/**
 * Class <code>NullRepaintManager</code> adalah class <code>RepaintManager</code>
 * yang tidak melakukan penggambaran apapun pada layar(<code>repaint()</code>). Ini
 * sangat berguna saat semua penggambaran ke layar dilakukan secara manual.
 */
public class NullRepaintManager extends RepaintManager {
    /**
        Installs the NullRepaintManager.
    */
    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

    public void addInvalidComponent(JComponent c) {
        // do nothing
    }

    public void addDirtyRegion(JComponent c, int x, int y,
        int w, int h)
    {
        // do nothing
    }

    public void markCompletelyDirty(JComponent c) {
        // do nothing
    }

    public void paintDirtyRegions() {
        // do nothing
    }

}
