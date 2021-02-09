package graphics;

import java.awt.Image;

/**
 * Class <code>PauseAnimation</code> adalah class untuk membuat animasi untuk
 * pause game. Class ini akan membuat image menjadi transparan dari 
 * 0 ke 0.5(fade in) dan 0.5 ke 0(fade out)
 * @author Yohanes Surya
 */
public class PauseAnimation {
	/**
	 * Membuat object baru
	 * @param image gambar yang akan di transparankan untuk fade in dan fade out
	 */
	public PauseAnimation(Image image){
		this.image=image;
		
		loadFadeInAnim();
		loadFadeOutAnim();
		
		anim=fadeIn;
	}

	private void loadFadeInAnim(){
		//initial Fade In background animation
		fadeIn=new Animation(false,false);
		
		float transparancy=0f;
		while(transparancy<=.5f){
			fadeIn.addFrame(ImageMan.drawTransparent(image, transparancy), 
					50);
			transparancy +=.1f;
		}
	}

	private void loadFadeOutAnim(){
		//initial Fade Out background animation
		fadeOut=new Animation(false,false);
		
		float transparancy=1;
		while(transparancy>=.5f){
			fadeOut.addFrame(ImageMan.drawTransparent(image, transparancy), 
					50);
			transparancy -=.1f;
		}
	}

	/**
	 * Untuk mengupdate sama seperti <code>Animation</code>
	 * @see graphics.Animation#update
	 * @param elapsedTime waktu untuk mengupdate animasi ini(dalam millisecond)
	 */
	public void update(long elapsedTime){
		anim.update(elapsedTime);
	}
	
    /**
     * Check apakah animasi ini sudah sampai akhir
     * @return <code>true</code> jika selesai <code>false</code> untuk sebaliknya
     */
	public boolean isFinish(){
		return anim.isFinised();
	}
	
	/**
	 * Start fade in animation
	 */
	public void fadeIn(){
		anim=fadeIn;
		anim.start();
	}
	
	/**
	 * Start fade out animation
	 */
	public void fadeOut(){
		anim=fadeOut;
		anim.start();
	}
	
	/**
	 * Mengambil gambar yang sedang aktif
	 * @return null jika tidak ada gambar di animasi ini
	 */
	public Image getImage(){
		return anim.getImage();
	}
	
	private Image image;
	private Animation fadeIn;
	private Animation fadeOut;
	private Animation anim;
}
