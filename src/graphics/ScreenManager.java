package graphics;


import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Class <code>ScreenManager</code> menghandle semua penggambaran gambar di layar.
 * Class ini juga yang mengset game menjadi full screen maupun tidak(windowed)
 * @author Yohanes Surya
 */
public class ScreenManager {
	/**
	 * Menciptakan object <code>ScreenManager</code> baru
	 * @param title Judul yang muncul di windows atas(title)
	 */
    public ScreenManager(String title){
        frame=new JFrame(title);

        device=GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        
    }
    
    /**
     * Mengset menjadi full screen
     * @param fullScreen <code>true</code> untuk mode full screen dan 
     * <code>false</code> untuk windowed
     */
    public void setFullScreen(boolean fullScreen){
        this.isFullScreen=fullScreen;
        if(fullScreen){
        	//Create custom cursor
        	Cursor invisibleCursor =
        	    Toolkit.getDefaultToolkit().createCustomCursor(
        	    Toolkit.getDefaultToolkit().getImage(""),
        	    new Point(0,0),
        	    "invisible");
        	
        	frame.setCursor(invisibleCursor);
            frame.setUndecorated(true);
            frame.setIgnoreRepaint(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            device.setFullScreenWindow(frame);
            
            DisplayMode best=findGetBestDisplayMode(device.getDisplayModes());
            device.setDisplayMode(best);
            best.toString();
        }
        else
        {
            frame.setSize(width, height);
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        
        frame.createBufferStrategy(2);     
    }
    
	private DisplayMode findGetBestDisplayMode(DisplayMode[] dm){		
		DisplayMode best=null;
		for(int i=0; i<dm.length; i++){
			if(dm[i].getWidth()==width && 
					dm[i].getHeight()==height){
				if(best==null){
					best=dm[i];
				}
				else
				{
					if(best.getBitDepth()<dm[i].getBitDepth() ||
							best.getRefreshRate()<dm[i].getRefreshRate()){
						best=dm[i];
					}
				}
			}
		}
		
		return best;
	}
    
	/**
	 * Mengembalikan screen menjadi seperti semula
	 */
    public void restoreScreen(){
        if(isFullScreen){
            Window window=device.getFullScreenWindow();
            window.dispose();
            device.setFullScreenWindow(null);
        }
        else{
            frame.dispose();
        }
    }
    
    //Getter
    /**
     * Mengambil <code>Graphics2D</code> yang berguna dalam penggambaran dilayar
     */
    public Graphics2D getGraphics(){
        return (Graphics2D)frame.getBufferStrategy().getDrawGraphics();
    }
    
    /**
     * Mendapatkan tinggi dari resolusi
     * @return tinggi resolusi
     */
    public int getHeight(){
        return frame.getHeight();
    }
    
    /**
     * Mendapatkan lebar dari resolusi
     * @return lebar resolusi
     */
    public int getWidth(){
        return frame.getWidth();
    }
    
    /**
     * Mendapatkan frame
     * @return frame game
     */
    public JFrame getFrame(){
        return frame;
    }
    
    /**
     * Mengupdate layar
     */
    public void update(){
        if(!frame.getBufferStrategy().contentsLost())
            frame.getBufferStrategy().show();
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Untuk membuat gambar yang kompetible dengan layar
     * @param w lebar gambar
     * @param h tinggi gambar
     * @param transparancy lihat class Trasparancy
     * @return <code>BufferedImage</code> yang dapat di olah nantinya
     */
	public BufferedImage createCompatibleImage(int w, int h,
	    int transparancy)
	{
	    Window window = frame;
	    if (window != null) {
	        GraphicsConfiguration gc =
	            window.getGraphicsConfiguration();
	        return gc.createCompatibleImage(w, h, transparancy);
	    }
	    return null;
	}

    private JFrame frame;
    private int height=768;
    private int width=1024;
    private GraphicsDevice device;
    private boolean isFullScreen;
}
