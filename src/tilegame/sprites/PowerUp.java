package tilegame.sprites;

import sound.Sound;
import sound.SoundManager;
import graphics.*;

/**
 * Class <code>PowerUp</code> adalah class abstract yang menghandle semua 
 * item-item yang ada di game ini. Untuk menggunakannya gunakan subclassnya
 * @author Yohanes Surya
 *
 */
public abstract class PowerUp extends Sprite {
	/**
	 * Sound Manager untuk memainkan musik
	 */
    protected SoundManager soundManager;
    
    /**
     * Sound yang akan dimainkan jika item di ambil(acquire) 
     */
    protected Sound sound;

	/**
	 * Membuat object baru
	 * @param anim Animasi item
	 * @param soundManager Sound Manager
	 * @param sound sound jika di ambil oleh player
	 */
    public PowerUp(Animation anim, SoundManager soundManager,
    		Sound sound) {
        super(anim);
        this.soundManager=soundManager;
        this.sound=sound;
    }
    
    /**
     * Memberikan efek pada pengambilnya
     * @return efek
     */
    public abstract float getEffect();
    
    /**
     * Mengambil banyak score. Karena object ini adalah abstract amak nilai 
     * kembaliannya adalah NOL
     * @return bernilai NOL
     */
    public int getScore(){
    	return 0;
    }
    
    /**
     * Method ini seharusnya dipanggil jika player telah mengambil item yang
     * bersangkutan. Method ini akan memainkan musik
     */
    public void acquire(){
    	soundManager.play(sound);
    }

    /**
     * Class <code>Star</code> berfungsi untuk menambahkan MP 10%
     * @author Yohanes Surya
     *
     */
    public static class Star extends PowerUp {
    	/**
		 * Membuat object baru
		 * @param anim Animasi item
		 * @param soundManager Sound Manager
		 * @param sound sound jika di ambil oleh player
    	 */
        public Star(Animation anim, SoundManager soundManager,
        		Sound sound) {
            super(anim,soundManager,sound);
        }
        
        public float getEffect(){
        	return .1f;
        }
        
        public int getScore(){
        	return 100;
        }
        
        public Star clone(){
        	return new Star(
        			this.anim.clone(),
        			soundManager,
        			sound);
        }
    }


    /**
     * Class <code>Live</code> berfungsi untuk menambahkan nyawa +1
     * @author Yohanes Surya
     *
     */
    public static class Live extends PowerUp {
    	/**
		 * Membuat object baru
		 * @param anim Animasi item
		 * @param soundManager Sound Manager
		 * @param sound sound jika di ambil oleh player
    	 */
        public Live(Animation anim, SoundManager soundManager,
        		Sound sound) {
            super(anim,soundManager,sound);
        }
        
        public float getEffect(){
        	return 1;
        }

        public int getScore(){
        	return 1000;
        }

        public Live clone(){
        	return new Live(        			
        			this.anim.clone(),
        			soundManager,
        			sound);

        }
    }

    /**
     * Class <code>Kendi</code> berfungsi untuk menambahkan HP 20%
     * @author Yohanes Surya
     *
     */
	public static class Kendi extends PowerUp {
		/**
		 * Membuat object baru
		 * @param anim Animasi item
		 * @param soundManager Sound Manager
		 * @param sound sound jika di ambil oleh player
		 */
	    public Kendi(Animation anim, SoundManager soundManager,
	    		Sound sound) {
            super(anim,soundManager,sound);
	    }
	    
	    public float getEffect(){
	    	return .2f;
	    }
	
        public int getScore(){
        	return 100;
        }

        public Kendi clone(){
	    	return new Kendi(        			
	    			this.anim.clone(),
        			soundManager,
        			sound);

	    }
	}
}
