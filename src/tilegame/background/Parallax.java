package tilegame.background;


import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JFrame;
//Parallax Object moving depend on player position

/**
 * Class <code>Parallax</code> berjalan berdasarkan posisi. Class ini hanya
 * menghandel gambar yang pas dengan besar Frame. Pastikan gambar yang
 * dimasukkan adalah gambar yang jika di "loop" tidak terlihat sambungannya.
 * @author Yohanes Surya
 *
 */
public class Parallax {
	/**
	 * Membuat object <code>Parallax</code> baru
	 * @param img gambar yang akan di scroll
	 * @param speed kecepatan scroll
	 * @param frame frame
	 */
    public Parallax(Image img, float speed, JFrame frame){
        this.img=img;
        this.speed=speed;
        this.frame=frame;
        this.x=0;
    }
    
    /**
     * Mengambil posisi X
     * @return posisi X
     */
    public float getX(){
    	return x;
    }
    
    /**
     * Mengset posisi X
     * @param x posisi X
     */
    public void setX(float x){
    	this.x=x;
    }
    
    /**
     * Mengambil kecepatan scrolling
     * @return kecepatan scrolling
     */
    public float getSpeed(){
    	return speed;
    }
    
    /**
     * Mengupdate berdasarkan posisi atau selisih awal dan akhir. Misal:
     * Posisi player X=100 dan dia berjalan ke X=150 jadi offsetnya adalah 50.
     * Lima puluh lah yang akan dimasukkan.
     * @param offsetX offset X
     */
    public void update(int offsetX){
        x=speed*(offsetX);
        x%=frame.getWidth();
    }
    
    /**
     * Menggambar background ke layar.
     * @param g graphics target
     */
    public void draw(Graphics2D g){
        g.drawImage(img, (int)x, 0, null);
        
        if(x>0){            
            if((int)x>0){
                g.drawImage(img, (int)x-img.getWidth(null), 0, null);
            }

            if(x>img.getWidth(null))x%=frame.getWidth();
        }
        else if(x<0){
            if((int)x<0){
                g.drawImage(img, (int)x+img.getWidth(null), 0, null);
            }

            if(x<img.getWidth(null))x%=frame.getWidth();            
        }
    }
    
    private Image img;
    private JFrame frame;
    private float speed;
    private float x;
}
