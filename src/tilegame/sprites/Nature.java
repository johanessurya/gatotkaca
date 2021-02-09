package tilegame.sprites;

import sound.Sound;
import sound.SoundManager;
import graphics.*;

/**
 * Class <code>Nature</code> adalah abstract class yang menghandle semua 
 * <code>Creature</code> yang merupakan makluk hidup.
 * @author Yohanes Surya
 */
public abstract class Nature extends Creature{
	/**
	 * State HURT
	 */
    public static final int STATE_HURT =3;

    /**
     * Animasi hurt left
     */
    protected Animation hurtLeft;
    
    /**
     * Animasi hurt right
     */
    protected Animation hurtRight;
    
    /**
     * Sound Manager untuk meng-play musik
     */
    protected SoundManager soundManager;
    
    /**
     * Hurt sound
     */
    protected Sound hurtSound;
    
    /**
     * Die sound
     */
    protected Sound dieSound;
    
    protected boolean isImmune;

    /**
     * Abstract method untuk membuat object baru
     * @param left Animasi bergerak kekiri
     * @param right Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @param hurtLeft animasi kesakitan(hurt) kekiri
     * @param hurtRight animasi kesakitan(hurt) kekanan
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public Nature(Animation left, Animation right,
        Animation deadLeft, Animation deadRight,
        Animation hurtLeft, Animation hurtRight,
        SoundManager soundManager, 
        Sound hurtSound, Sound dieSound)
    {
        super(left,right,deadLeft,deadRight);
        this.hurtLeft=hurtLeft;
        this.hurtRight=hurtRight;
        
        //Sound
        this.soundManager=soundManager;
        this.hurtSound=hurtSound;
        this.dieSound=dieSound;
        
        state = STATE_NORMAL;
    }

    public boolean isAlive() {
		return (state!=STATE_DYING && state!=STATE_DEAD);
    }

    public void update(long elapsedTime) {    	
        // select the correct Animation
        Animation newAnim = anim;
        if(state==STATE_NORMAL)
        {
		    if (!isFacingRight()) {
		        newAnim = left;
		    }
		    else{
		        newAnim = right;
		    }
        }
        else if(state==STATE_DYING)
        {
	        if (!isFacingRight()) {
	            newAnim = deadLeft;
	        }
	        else{
	            newAnim = deadRight;
	        }
        }
        else if(state==STATE_HURT){
        	setVelocityX(0);
        	isImmune=true;
        	if(isFacingRight()){
	        	newAnim=hurtRight;
	        	if(hurtRight.isFinised()){
	            	isImmune=false;
	        		hurtRight.start();
	        		newAnim=right;
	        		setVelocityX(getMaxSpeed());
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
	        	newAnim=hurtLeft;
	        	if(hurtLeft.isFinised()){
	            	isImmune=false;
	        		hurtLeft.start();
	        		newAnim=left;
	        		setVelocityX(-getMaxSpeed());
	        		setState(STATE_NORMAL);
	        	}        		
        	}
        }

        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(elapsedTime);
        }

        // update to "dead" state
        // stateTime += elapsedTime;
        if (state == STATE_DYING && anim.isFinised()) {
            setState(STATE_DEAD);
        }
    }
    	
    /**
     * Membuat Nature ini menjadi "kesakitan". Biasanya karena serangan lawan
     */
	public void damaged(){
		this.isImmune=true;
		if(getHp()<=0){
			soundManager.play(dieSound);
    		setState(STATE_DYING);
			
		}
		else {
			soundManager.play(getHurtSound());
			setState(STATE_HURT);			
		}
    	
	}
	
	/**
	 * Check apakah nature ini dalam kondisi kebal
     * @return <code>true</code> jika kebal dan <code>false</code> 
     * jika sebaliknya 
	 */
	public boolean isImmune(){
		return isImmune;
	}

	private Sound getHurtSound(){
		return hurtSound;
	}		
}
