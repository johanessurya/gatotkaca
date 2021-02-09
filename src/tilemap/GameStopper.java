package tilemap;

import graphics.ScreenManager;

import java.awt.Graphics2D;
import sound.SoundManager;
import util.GameCore;

/**
 * Class <code>GameStopper</code> berguna untuk menjadikan layar seperti semula
 * dari full screen menjadi windowed dan menghentikan permainan
 * @author Yohanes Surya
 *
 */
public class GameStopper extends GameCore{
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param soundManager <code>SoundManager</code> yang ditetapkan
	 */
	public GameStopper(ScreenManager screen, SoundManager soundManager){
		this.screen=screen;
		this.soundManager=soundManager;
	}
	
	public void init(){
        start();
	}

	@Override
	public void draw(Graphics2D g) {
		//Do Nothing
	}

	public void stop() {
		soundManager.close();
        super.stop();
    }

	        
    public void update(long elapsedTime){
    	setRunning(false);
    }
    
    private SoundManager soundManager;
}
