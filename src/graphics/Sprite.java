package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;


/**
 * Class <code>Sprite</code> menghandle gerakan animasi yang dibuat dengan class
 * <code>Animation</code>
 * @author Yohanes Surya
 */
public class Sprite {
	/**
	 * Animasi yang akan di gerakakan nantinya.
	 */
    protected Animation anim;
    
    // position (pixels)
    private float x;
    private float y;
    
    // velocity (pixels per millisecond)
    private float dx;
    private float dy;
    
    private boolean isFacingRight;
    
    /**
     * Membuat object <code>Sprite</code> dengan spesifikasi animasi
     * @param anim animasi yang akan di gerakkan
     */
    public Sprite(Animation anim) {
        this.anim = anim;
        this.isFacingRight=true;
    }

    /**
     * Mengupdate <code>Sprite</code> berdasarkan waktu dan kecepatan
     * @param elapsedTime waktu yang terlewat
     */
    public void update(long elapsedTime) {
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        anim.update(elapsedTime);
    }

    /**
     * Mendapatkan posisi X <code>Sprite</code>
     * @return posisi X <code>Sprite</code>
     */
    public float getX() {
        return x;
    }

    /**
     * Mendapatkan posisi Y <code>Sprite</code>
     * @return posisi Y <code>Sprite</code>
     */
    public float getY() {
        return y;
    }

    /**
     * Mengset posisi X <code>Sprite</code>
     * @param x posisi X <code>Sprite</code> yang baru
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Mengset posisi Y <code>Sprite</code>
     * @param y posisi Y <code>Sprite</code> yang baru
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Mendapatkan lebar gambar
     * @return lebar gambar
     */
    public int getWidth() {
        return anim.getImage().getWidth(null);
    }

    /**
     * Mendapatkan tinggi gambar
     * @return tinggi gambar
     */
    public int getHeight() {
        return anim.getImage().getHeight(null);
    }

    /**
     * Mendapatkan kecepatan X (pixel/ms)
     * @return kecepatan
     */
    public float getVelocityX() {
        return dx;
    }

    /**
     * Mendapatkan kecepatan Y (pixel/ms)
     * @return kecepatan
     */
    public float getVelocityY() {
        return dy;
    }

    /**
     * Mengset kecepatan X (pixel/ms)
     * @param dx kecepatan
     */
    public void setVelocityX(float dx) {
        this.dx = dx;
    	if(dx!=0)
    	{
    		isFacingRight=this.dx>0;
    	}
    }

    /**
     * Mengset kecepatan Y (pixel/ms)
     * @param dy kecepatan
     */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    /**
     * Mendapatkan gambar
     * @return gambar
     */
    public Image getImage() {
        return anim.getImage();
    }

    /**
     * Menggandakan object <code>Sprite</code>. Tidak termasuk kecepatan dan posisi
     */
    public Object clone() {
        return new Sprite(anim);
    }
    
    /**
     * Mengcheck apakah <code>Sprite</code> menhadap ke kanan
     * @return <code>true</code> jika menghadap ke kanan 
     * <code>false</code> untuk sebaliknya
     */
    public boolean isFacingRight(){
    	return isFacingRight;
    }
           
    /**
     * Mendapatkan kotak yang berguna untuk pengecheckan tumbukan
     * @return kotak
     */
    public Rectangle2D.Float getFitRectangle(){
    	Rectangle temp=anim.getFitRectangleCurrentFrame();
    	Rectangle2D.Float rect=new Rectangle2D.Float(temp.x+getX(), temp.y+getY(),
    			temp.width,temp.height);
    	
    	return rect;
    }
    
    /**
     * Menggambar <code>Sprite</code>
     * @param g <code>Graphics2D</code>
     * @param x posisi x
     * @param y posisi y
     */
    public void draw(Graphics2D g,float x, float y){
    	x=x+this.x;
    	y=y+this.y;
    	
    	g.drawImage(getImage(), (int)x, (int)y, null);
    }
}
