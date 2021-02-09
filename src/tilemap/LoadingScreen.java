package tilemap;

import graphics.ScreenManager;
import java.awt.Graphics2D;
import java.awt.Image;
import util.GameCore;
import util.ResourceLoader;

/**
 * Class <code>LoadingScreen</code> berguna untuk menampilkan screen loading
 * @author Yohanes Surya
 */
public class LoadingScreen extends GameCore{
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 */
	public LoadingScreen(ScreenManager screen){
		this.screen=screen;
		loading=loadImage("loading.jpg");
	}
	
	public void init(){
        start();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(loading, 0, 0, null);
	}
	
	private Image loadImage(String filename){
		return ResourceLoader.loadImage("images/loading/"+filename);
	}
    
    public void run(){
        init();
        gameLoop();
    }
    
    public void update(long elapsedTime){
    	setRunning(false);
    }
    
    private Image loading;
}
