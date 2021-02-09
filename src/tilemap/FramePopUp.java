package tilemap;

import java.awt.Graphics2D;

import sound.Sound;
import sound.SoundManager;

import graphics.Animation;
import graphics.Sprite;

/**
 * Class <code>FramePopUp</code> adalah <code>Sprite</code> dengan memasukkan 3
 * animasi yaitu fade in, fade out, dan normal(static) beserta suaranya
 * @author Yohanes Surya
 */
public class FramePopUp extends Sprite{
	/**
	 * State FADE IN
	 */
	public static final int STATE_FADE_IN=0;
	
	/**
	 * State NORMAL
	 */
	public static final int STATE_NORMAL=1;
	
	/**
	 * State FADE OUT
	 */
	public static final int STATE_FADE_OUT=2;

	/**
	 * Membuat object baru
	 * @param fadeIn animasi fade in
	 * @param normal animasi normal
	 * @param fadeOut animasi fade out
	 * @param x posisi X
	 * @param y posisi Y
	 * @param soundManager <code>SoundManager</code> yang ditetapkan
	 * @param popUpSound <code>Sound</code> yang ditetapkan
	 */
	public FramePopUp(Animation fadeIn, 
			Animation normal,
			Animation fadeOut,
			float x, float y,
			SoundManager soundManager, Sound popUpSound){
		super(fadeIn);		
		
		this.fadeIn=fadeIn;
		this.fadeOut=fadeOut;
		this.normal=normal;

		this.soundManager=soundManager;
		this.popUpSound=popUpSound;
		
		setY(y);
		setX(x);
		
		state=STATE_FADE_IN;
		this.fadeIn.start();
	}
	
	public void update(long elapsedTime){
		
		Animation newAnim = anim;
        if(state==STATE_FADE_IN){
			newAnim=fadeIn;
			if(fadeIn.isFinised()){
				fadeIn.start();
				state=STATE_NORMAL;
			}
		}
		else if(state==STATE_FADE_OUT){
			newAnim=fadeOut;
			if(fadeOut.isFinised()){
				fadeOut.start();
				state=STATE_NORMAL;
			}
		}
		else
		{
			newAnim=normal;
		}
		
        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(elapsedTime);
        }
	}
	
	/**
	 * Check apakah animasi yang bersangkutan/aktif sudah selesai
	 * @return <code>true</code> jika selesai dan 
	 * <code>false</code> untuk sebaliknya
	 */
	public boolean isFinish(){
		return anim.isFinised();
	}
	
	/**
	 * Menampilkan object ini ke layar
	 * @param g <code>Graphics2D</code> yang ditetapkan
	 */
	public void draw(Graphics2D g){
		g.drawImage(getImage(), (int)getX(), (int)getY(), null);
	}
	
	/**
	 * Mengambil keadaan object ini
	 * @return berupa STATE_FADE_IN, STATE_NORMAL, atau STATE_FADE_OUT
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * Meminta object ini untuk melakukan animasi fade in
	 */
	public void fadeIn(){
		state=STATE_FADE_IN;
		fadeIn.start();
		soundManager.play(popUpSound);
	}
	
	/**
	 * Meminta object ini untuk melakukan animasi fade out
	 */
	public void fadeOut(){
		state=STATE_FADE_OUT;
		fadeOut.start();
		soundManager.play(popUpSound);
	}
	
	private Animation fadeIn;
	private Animation fadeOut;
	private Animation normal;
	private SoundManager soundManager;
	private Sound popUpSound;
	
	private int state;	
}
