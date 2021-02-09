package tilegame.background;

import java.awt.Image;

import javax.swing.JFrame;

/**
 * Class <code>Parallax</code> berjalan berdasarkan waktu.
 * @author Yohanes Surya
 * @see tilegame.background.Parallax
 */
public class ParallaxByTime extends Parallax{
	/**
	 * Membuat object ParallaxByTime
	 * @param img gambar yang akan di scrolling
	 * @param speed kecepatan scrolling
	 * @param frame frame
	 */
	public ParallaxByTime(Image img, float speed, JFrame frame){
	    super(img,speed,frame);
	}
	
	/**
	 * Mengupdate posisi gambar dengan memasukkan selisih waktu. Sama halnya dengan
	 * Parallax yang sebelumnya hanya saja ini adalah waktu
	 * @param elapsedTime selisih waktu
	 * @see tilegame.background.Parallax#update(int)
	 */
    public void update(long elapsedTime){
        setX(getX()+getSpeed()*elapsedTime);
    }
}
